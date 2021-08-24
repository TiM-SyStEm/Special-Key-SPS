package com.timsystem;

import com.timsystem.ast.*;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ClassValue;
import com.timsystem.lib.Token;
import com.timsystem.lib.TokenType;
import com.timsystem.runtime.FunctionValue;
import com.timsystem.runtime.UserDefinedFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parser {
    private static final Token EOF = new Token(TokenType.EOF, "");

    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public BlockStatement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }
        return result;
    }

    private Statement block(){
        final BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.add(statement());
        }
        return block;
    }
    private Statement statement() {
        Token current = get(0);
        if (match(TokenType.OUT)) {
            return outStatement();
        }
        else if (match(TokenType.INPUT)) {
            return inputStatement();
        }
        else if (match(TokenType.ADD)) {
            return new AddStatement(consume(TokenType.WORD).getText());
        }
        else if (match(TokenType.VAR)) {
            return assignmentStatement();
        }
        else if(match(TokenType.FUN)){
            return functionCreate();
        }
        else if (match(TokenType.IF)) {
            return ifElse();
        }
        else if(match(TokenType.WHILE)){
            return whileStatement();
        }
        else if(match(TokenType.DO)){
            return doStatement();
        }
        else if (match(TokenType.FOR)) {
            return forStatement();
        }
        else if (match(TokenType.STOP)) {
            return new StopStatement();
        }
        else if (match(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }
        else if (match(TokenType.RETURN)) {
            return new ReturnStatement(expression());
        }
        else if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN) {
            return new ExprStatement(functionChain(qualifiedName()));
        } else if (match(TokenType.STRUCT)) {
            return klass();
        }
        return reAssignmentStatement();
    }

    private Statement klass() {
        Map<String, Expression> targets = new HashMap<>();
        String name = consume(TokenType.WORD).getText();
        targets.put("__class__", new ValueExpression(name));
        List<String> argNames = arguments();
        boolean extended = false;
        String extension = "";
        if (match(TokenType.EXTENDS)) {
            extended = true;
            extension = consume(TokenType.WORD).getText();
        }
        ClassValue result = new ClassValue(name, argNames);
        consume(TokenType.LBRACE);
        do {
            if (match(TokenType.FUN)) {
                String fun_name = consume(TokenType.WORD).getText();
                List<String> args = arguments();
                targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(args, statementOrBlock()))));
                continue;
            }
            if (match(TokenType.VAR)) {
                String var_name = consume(TokenType.WORD).getText();
                consume(TokenType.EQ);
                targets.put(var_name, expression());
                continue;
            }
        } while(!match(TokenType.RBRACE));
        return extended ? new ExtendedClassDeclaration(result, name, argNames, targets, extension) : new ClassDeclaration(result, name, argNames, targets);
    }

    private Statement doStatement() {
        final Statement statement = statementOrBlock();
        return new DoStatement(statement);
    }

    private Statement reAssignmentStatement() {
        final Token current = get(0);
        if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.EQ) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }

        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET)) {
            final ArrayAccessExpression array = (ArrayAccessExpression) element();
            consume(TokenType.EQ);
            return new ArrayAssignmentStatement(array, expression());
        }

        return new ExprStatement(expression());
    }

    private Statement outStatement() {
        consume(TokenType.COLON);
        return new OutStatement(expression());
    }
    private Statement inputStatement() {
        consume(TokenType.COLON);
        return new StdInput(expression());
    }
    private Statement assignmentStatement() {
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.EQ);
        return new AssignmentStatement(variable, expression());
    }
    private Statement statementOrBlock(){
        if(get(0).getType() == TokenType.LBRACE) return block();
        else return statement();
    }
    private Statement ifElse() {
        final Expression conditional = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }
        return new IfStatement(conditional, ifStatement, elseStatement);
    }
    private Statement whileStatement(){
        final Expression conditional = expression();
        final Statement statement = statementOrBlock();
        return new WhileStatement(conditional, statement);
    }
    private Statement forStatement(){
        match(TokenType.LPAREN);
        consume(TokenType.VAR);
        final Statement initialization = assignmentStatement();
        consume(TokenType.COMMA);
        final Expression termination = expression();
        consume(TokenType.COMMA);
        final Statement increment = assignmentStatement();
        match(TokenType.RPAREN);
        final Statement statement = statementOrBlock();
        return new ForStatement(initialization, termination, increment, statement);
    }
    private FunctionalDefineStatement functionCreate() {
        final String name = consume(TokenType.WORD).getText();
        ArrayList<String> argNames = arguments();
        final Statement body = statementOrBlock();
        return new FunctionalDefineStatement(name, argNames, body);
    }

    private FunctionalExpression function(Expression qualifiedNameExpr) {
        // function(arg1, arg2, ...)
        consume(TokenType.LPAREN);
        final FunctionalExpression function = new FunctionalExpression(qualifiedNameExpr);
        while (!match(TokenType.RPAREN)) {
            function.addArgument(expression());
            match(TokenType.COMMA);
        }
        return function;
    }

    private Expression expression() {
        return suffix();
    }

    private Expression suffix() {
        Expression left = logicIn();

        while (true) {
            if (match(TokenType.DEC)) {
                left = new SuffixExpression('-', left);
            }

            if (match(TokenType.INC)) {
                left = new SuffixExpression('+', left);
            }


            break;
        }

        return left;
    }

    private Expression logicIn() {
        Expression expr = logicOr();

        if (match(TokenType.NOT) && match(TokenType.IN)) {
            return new UnaryExpression('!', new InExpression(expr, logicOr()));
        }

        if (match(TokenType.IN)) {
            return new InExpression(expr, expression());
        }
        return expr;
    }

    private Expression logicOr() {
        Expression result = logicAnd();
        while (true) {
            if (match(TokenType.OR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicAnd());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression logicAnd() {
        Expression result = equality();
        while (true) {
            if (match(TokenType.AND)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
                continue;
            }
            break;
        }
        return result;
    }

    private Expression equality() {
        Expression result = conditional();
        if (match(TokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }

        if (match(TokenType.NOTEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        } else return result;
    }

    private Expression conditional() {
        Expression result = additive();

        while (true) {
            if (match(TokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive());
                continue;
            } else if (match(TokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive());
                continue;
            } else if (match(TokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                continue;
            } else if (match(TokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }
            else if (match(TokenType.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = remains();

        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }
            else if (match(TokenType.SLASH)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            else if (match(TokenType.POW)) {
                result = new BinaryExpression('^', result, unary());
                continue;
            }
            break;
        }
        return result;
    }
    private Expression remains() {
        Expression result = unary();

        while (true) {
            if (match(TokenType.REMAINDER)) {
                result = new BinaryExpression('%', result, unary());
                continue;
            }
            break;
        }
        return result;
    }
    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }

        if (match(TokenType.NOT)) {
            return new UnaryExpression('!', primary());
        }

        return primary();
    }
    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.FUN)) {
            ArrayList<String> args = arguments();
            return new ValueExpression(new FunctionValue(new UserDefinedFunction(args, statementOrBlock())));
        } else if (match(TokenType.HEX_NUMBER)) {
            return new ValueExpression(Long.parseLong(current.getText(), 16));
        } else if(match(TokenType.INPUT)) {
            return inputExpression();
        }

        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
            return functionChain(new ValueExpression(consume(TokenType.WORD).getText()));
        }

        Expression qualifiedNameExpr = qualifiedName();
        if (qualifiedNameExpr != null) {
            if (lookMatch(0, TokenType.LPAREN)) {
                return functionChain(qualifiedNameExpr);
            }

            return qualifiedNameExpr;
        }
        return value();
    }

    private Expression qualifiedName() {
        // var || var.key[index].key2
        final Token current = get(0);
        if (!match(TokenType.WORD)) return null;

        final List<Expression> indices = variableSuffix();
        if (indices == null || indices.isEmpty()) {
            return new VariableExpression(current.getText());
        }
        return new ContainerAccessExpression(current.getText(), indices);
    }

    private List<Expression> variableSuffix() {
        // .key1.arr1[expr1][expr2].key2
        if (!lookMatch(0, TokenType.DOT) && !lookMatch(0, TokenType.LBRACKET)) {
            return null;
        }
        final List<Expression> indices = new ArrayList<>();
        while (lookMatch(0, TokenType.DOT) || lookMatch(0, TokenType.LBRACKET)) {
            if (match(TokenType.DOT)) {
                final String fieldName = consume(TokenType.WORD).getText();
                final Expression key = new ValueExpression(fieldName);
                indices.add(key);
            }
            if (match(TokenType.LBRACKET)) {
                indices.add(expression());
                consume(TokenType.RBRACKET);
            }
        }
        return indices;
    }

    private Expression functionChain(Expression qualifiedNameExpr) {
        // f1()()() || f1().f2().f3() || f1().key
        final Expression expr = function(qualifiedNameExpr);
        if (lookMatch(0, TokenType.LPAREN)) {
            return functionChain(expr);
        }
        if (lookMatch(0, TokenType.DOT)) {
            final List<Expression> indices = variableSuffix();
            if (indices == null || indices.isEmpty()) {
                return expr;
            }

            if (lookMatch(0, TokenType.LPAREN)) {
                // next function call
                return functionChain(new ContainerAccessExpression(expr, indices));
            }
            // container access
            return new ContainerAccessExpression(expr, indices);
        }
        return expr;
    }

    private Expression inputExpression() {
        consume(TokenType.COLON);
        return new StdInput(expression());
    }

    private Expression value() {
        Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(createNumber(current.getText()));
        }else if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        } else if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        } else if (match(TokenType.STRING)) {
            return new ValueExpression(current.getText());
        } else if (match(TokenType.LPAREN)) {
            Expression result = expression();
            match(TokenType.RPAREN);
            return result;
        }
        throw new SPKException("ExpressionError", String.format("unknown expression '%s'", current.getType()));
    }
    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }
    private Expression element() {
        final String variable = consume(TokenType.WORD).getText();
        List<Expression> indices = new ArrayList<>();
        do{
            consume(TokenType.LBRACKET);
            indices.add(expression());
            consume(TokenType.RBRACKET);
        }
        while(lookMatch(0, TokenType.LBRACKET));
        return new ArrayAccessExpression(variable, indices);
    }

    private ArrayList<String> arguments() {
        ArrayList<String> args = new ArrayList<>();
        match(TokenType.LPAREN);
        while (!(match(TokenType.RPAREN))) {
            args.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        return args;
    }

    private Number createNumber(String text) {
        // Double
        if (text.contains(".")) {
            return Double.parseDouble(text);
        }
        // Integer
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException nfe) {
            return Long.parseLong(text);
        }
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            throw new SPKException("TokenError", "Token '" + current.getType() + "' doesn't match " + type);
        pos++;
        return current;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }
    private boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }
}