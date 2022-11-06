package com.timsystem;

import com.timsystem.ast.*;
import com.timsystem.lib.*;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "", -1);

    private final Map<String, Integer> macros;

    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
        this.macros = new HashMap<>();
    }

    public BlockStatement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }
        return result;
    }
    public Expression parseExpr() {
        return expression();
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
        if (lookMatch(0, TokenType.WORD) && macros.containsKey(get(0).getText())) {
            return macroUsage();
        }
        if (match(TokenType.OUT)) {
            return outStatement();
        }
        else if (match(TokenType.INPUT)) {
            return inputStatement();
        }
        else if (match(TokenType.ADD)) {
            return new AddStatement(consume(TokenType.WORD).getText());
        }
        else if (match(TokenType.VAR) ) {
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
        else if (match(TokenType.FIELD)){
            return fieldStatement();
        }
        else if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LPAREN) {
            return new ExprStatement(functionChain(qualifiedName()));
        } else if (match(TokenType.STRUCT)) {
            return klass();
        } else if (match(TokenType.LIGHTSTRUCT)) {
            return struct();
        } else if (match(TokenType.AT)) {
            if (match(TokenType.SPEC)) {
                return specification();
            }
        } else if (match(TokenType.ENUM)) {
            return enums();
        } else if (match(TokenType.DEFMACRO)) {
            return defmacro();
        } else if (match(TokenType.THROW)) {
            return throwSt();
        } else if (match(TokenType.CASE)) {
            return match();
        }
        else if(match(TokenType.TRY)){
            return tryCatch();
        }
        return reAssignmentStatement();
    }

    private MatchExpression match() {
        final Expression expression = expression();
        consume(TokenType.LBRACE);
        final List<MatchExpression.Pattern> patterns = new ArrayList<>();
        do {
            MatchExpression.Pattern pattern = null;
            consume(TokenType.OF);
            final Token current = get(0);
            if (match(TokenType.NUMBER)) {
                // case 0.5:
                pattern = new MatchExpression.ConstantPattern(
                        NumberValue.of(createNumber(current.getText()))
                );
            } else if (match(TokenType.HEX_NUMBER)) {
                // case #FF:
                pattern = new MatchExpression.ConstantPattern(
                        NumberValue.of(createNumber(current.getText()))
                );
            } else if (match(TokenType.STRING)) {
                // case "text":
                pattern = new MatchExpression.ConstantPattern(
                        new StringValue(current.getText())
                );
            } else if (match(TokenType.WORD)) {
                // case value:
                pattern = new MatchExpression.VariablePattern(current.getText());
            } else if (match(TokenType.LBRACKET)) {
                // case [x :: xs]:
                final MatchExpression.ListPattern listPattern = new MatchExpression.ListPattern();
                while (!match(TokenType.RBRACKET)) {
                    listPattern.add(consume(TokenType.WORD).getText());
                    match(TokenType.CC);
                }
                pattern = listPattern;
            } else if (match(TokenType.LPAREN)) {
                // case (1, 2):
                final MatchExpression.TuplePattern tuplePattern = new MatchExpression.TuplePattern();
                while (!match(TokenType.RPAREN)) {
                    if ("_".equals(get(0).getText())) {
                        tuplePattern.addAny();
                        consume(TokenType.WORD);
                    } else {
                        tuplePattern.add(expression());
                    }
                    match(TokenType.COMMA);
                }
                pattern = tuplePattern;
            }

            if (pattern == null) {
                throw new SPKException("BadMatchException", "Wrong pattern in match expression: " + current);
            }
            if (match(TokenType.IF)) {
                // case e if e > 0:
                pattern.optCondition = expression();
            }

            consume(TokenType.COLON);
            if (lookMatch(0, TokenType.LBRACE)) {
                pattern.result = block();
            } else {
                pattern.result = new ReturnStatement(expression());
            }
            patterns.add(pattern);
        } while (!match(TokenType.RBRACE));

        return new MatchExpression(expression, patterns);
    }

    private Statement tryCatch() {
        final Statement tryStatement = statementOrBlock();
        final Statement catchStatement;
        if(match(TokenType.CATCH)){
            catchStatement = statementOrBlock();
        }
        else{
            throw new SPKException("CatchBlockError", "the catch block was not found");
        }
        return new TryCatchStatement(tryStatement, catchStatement);
    }

    private Statement throwSt() {
        String type = consume(TokenType.WORD).getText();
        Expression expr = expression();
        return new ThrowStatement(type, expr);
    }

    private Statement macroUsage() {
        String name = consume(TokenType.WORD).getText();
        ArrayList<Expression> exprs = new ArrayList<>();
        for (int i = 0; i < macros.get(name); i++) {
            exprs.add(expression());
        }
        FunctionalExpression func = new FunctionalExpression(new VariableExpression(name), exprs);
        return func;
    }

    private Statement defmacro() {
        String name = consume(TokenType.WORD).getText();
        ArrayList<String> args = arguments();
        Statement block = statementOrBlock();
        macros.put(name, args.size());
        return new FunctionalDefineStatement(name, args, block);
    }

    private Statement enums() {
        String name = consume(TokenType.WORD).getText();
        Map<String, StringValue> enums = new HashMap<>();
        consume(TokenType.LBRACE);
        while (!(match(TokenType.RBRACE))) {
            String en = consume(TokenType.WORD).getText();
            match(TokenType.COMMA);
            enums.put(en, new StringValue(en));
        }
        return new AssignmentStatement(name, new ValueExpression(new EnumValue(enums)));
    }

    private Statement specification() {
        ArrayList<SpecificationType> specs = new ArrayList<>();
        String function = consume(TokenType.WORD).getText();
        consume(TokenType.CC);
        while (!(match(TokenType.DOT))) {
            String type = consume(TokenType.WORD).getText().toLowerCase();
            if (type.equals("int")) specs.add(SpecificationType.NUMBER);
            if (type.equals("float")) specs.add(SpecificationType.NUMBER);
            if (type.equals("str")) specs.add(SpecificationType.STRING);
            if (type.equals("arr")) specs.add(SpecificationType.ARRAY);
            if (type.equals("fun")) specs.add(SpecificationType.FUNCTION);
            if (type.equals("Class")) specs.add(SpecificationType.CLASS);
            if (type.equals("bool")) specs.add(SpecificationType.NUMBER);
            match(TokenType.COMMA);
        }
        Specifications.put(function, new Specification(specs));
        return new ExprStatement(new ValueExpression(0));
    }

    private Statement fieldStatement() {
        final Token current = get(0);
        if (get(0).getType() == TokenType.WORD) {
            final String variable = current.getText();
            return new FieldStatement(variable);
        }
        throw new SPKException("StatementError", "unknown statement");
    }

    private Statement klass() {
        Map<String, Expression> targets = new HashMap<>();
        String name = consume(TokenType.WORD).getText();
        targets.put("__class__", new ValueExpression(name));
        List<String> argNames = lookMatch(0, TokenType.LPAREN) ? arguments() : new ArrayList<>();
        boolean extended = false;
        List<String> extension = new ArrayList<String>();
        ArrayList<Statement> childClasses = new ArrayList<>();
        if (match(TokenType.EXTENDS)) {
            extended = true;
            extension = extends_classes();
        }
        ClassValue result = new ClassValue(name, argNames);
        consume(TokenType.LBRACE);
        do {
            if (match(TokenType.STRUCT)) {
                childClasses.add(klass());
                continue;
            }
            if (match(TokenType.PRIVATE)) {
                if (match(TokenType.FUN)) {
                    String fun_name = consume(TokenType.WORD).getText();
                    List<String> args = arguments();
                    targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(fun_name, args, statementOrBlock()), true, false)));
                    continue;
                }
            }
            if (match(TokenType.PROTECTED)) {
                if (match(TokenType.FUN)) {
                    String fun_name = consume(TokenType.WORD).getText();
                    List<String> args = arguments();
                    targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(fun_name, args, statementOrBlock()), false, true)));
                    continue;
                }
            }
            if (match(TokenType.FUN)) {
                String fun_name = consume(TokenType.WORD).getText();
                List<String> args = arguments();
                targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(fun_name, args, statementOrBlock()), false, false)));
                continue;
            }
            if (match(TokenType.VAR)) {
                String var_name = consume(TokenType.WORD).getText();
                consume(TokenType.EQ);
                targets.put(var_name, expression());
                continue;
            }
            if(match(TokenType.FIELD)){
                final String variable = consume(TokenType.WORD).getText();
                Variables.set(variable, NumberValue.MINUS_ONE);
                continue;
            }
            throw new SPKException("BadClassException", "Class '" + name + "' has doesn't have valid body");
        } while(!match(TokenType.RBRACE));
        return extended ? new ExtendedClassDeclaration(result, name, argNames, targets, extension, childClasses) : new ClassDeclaration(result, name, argNames, targets, childClasses);
    }
    private Statement struct() {
        Map<String, Expression> targets = new HashMap<>();
        String name = consume(TokenType.WORD).getText();
        //targets.put("__class__", new ValueExpression(name));

        LStructValue result = new LStructValue(name);
        consume(TokenType.LBRACE);
        do {
            if (match(TokenType.PRIVATE)) {
                if (match(TokenType.FUN)) {
                    String fun_name = consume(TokenType.WORD).getText();
                    List<String> args = arguments();
                    targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(fun_name, args, statementOrBlock()), true, false)));
                    continue;
                }
            }
            if (match(TokenType.FUN)) {
                String fun_name = consume(TokenType.WORD).getText();
                List<String> args = arguments();
                targets.put(fun_name, new ValueExpression(new FunctionValue(new UserDefinedFunction(fun_name, args, statementOrBlock()), false, false)));
                continue;
            }
            if (match(TokenType.VAR)) {
                String var_name = consume(TokenType.WORD).getText();
                consume(TokenType.EQ);
                targets.put(var_name, expression());
                continue;
            }
            if(match(TokenType.FIELD)){
                final String variable = consume(TokenType.WORD).getText();
                Variables.set(variable, NumberValue.MINUS_ONE);
                continue;
            }
            throw new SPKException("BadStructException", "Struct '" + name + "' has doesn't have valid body");
        } while(!match(TokenType.RBRACE));
        return new LStructDeclaration(result, name, targets);
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
        else if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.PLUSEQ){
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.PLUSEQ);
            return new AssignmentStatement(variable, expression(), 1);
        }
        else if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.MINUSEQ){
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.MINUSEQ);
            return new AssignmentStatement(variable, expression(), 2);
        }
        else if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.STAREQ){
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.STAREQ);
            return new AssignmentStatement(variable, expression(), 3);
        }
        else if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.SLASHEQ){
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.SLASHEQ);
            return new AssignmentStatement(variable, expression(), 4);
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
        Expression target = qualifiedName();
        consume(TokenType.EQ);
        return new AssignmentExpression((GettableSettable) target, expression());
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
        if (lookMatch(1, TokenType.IN)) {
            return foreach();
        }
        final Statement initialization = assignmentStatement();
        consume(TokenType.COMMA);
        final Expression termination = expression();
        consume(TokenType.COMMA);
        final Statement increment = statement();
        match(TokenType.RPAREN);
        final Statement statement = statementOrBlock();
        return new ForStatement(initialization, termination, increment, statement);
    }

    private Statement foreach() {
        String name = consume(TokenType.WORD).getText();
        consume(TokenType.IN);
        Expression expr = expression();
        match(TokenType.RPAREN);
        Statement block = statementOrBlock();
        return new ForeachArrayStatement(name, expr, block);
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
        Expression result = unary();

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
            else if (match(TokenType.REMAINDER)) {
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
        if (match(TokenType.REF)) {
            return new FunctionReferenceExpression(consume(TokenType.WORD).getText());
        }
        return primary();
    }
    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.CASE)) {
            return match();
        }
        if (match(TokenType.FUN)) {
            ArrayList<String> args = arguments();
            return new ValueExpression(new FunctionValue(new UserDefinedFunction("no_name", args, statementOrBlock())));
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
        }  else if (lookMatch(0, TokenType.LBRACE)) {
            FunctionValue fun = new FunctionValue(new UserDefinedFunction("anonymous", new ArrayList<>(), statementOrBlock()));
            return new ValueExpression(fun);}
        else if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        } else if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        } else if (match(TokenType.STRING)) {
            return new StringExpression(current.getText(), get(0).getLine(), "", 0);
        }else if (match(TokenType.LPAREN)) {
            Expression expr = expression();
            if (match(TokenType.COLON)) return dict(expr);
            match(TokenType.RPAREN);
            return expr;
        }
        throw new SPKException("ExpressionError", String.format("unknown expression '%s' at line %s", current.getType(), current.getLine()));
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
    private Expression dict(Expression expr) {
        final List<Expression[]> elements = new ArrayList<>();
        int ind = 0;
        while (!match(TokenType.RPAREN)) {
            if(ind != 0){
                Expression key = expression();
                match(TokenType.COLON);
                Expression val = expression();
                elements.add(new Expression[]{key, val});
                match(TokenType.COMMA);
            }
            else{
                Expression val = expression();
                elements.add(new Expression[]{expr, val});
                match(TokenType.COMMA);
            }
            ind++;
        }
        return new DictExpression(elements);
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
    private ArrayList<String> extends_classes() {
        ArrayList<String> args = new ArrayList<>();
        while (!lookMatch(0, TokenType.LBRACE)) {
            args.add(consume(TokenType.WORD).getText());
            if(get(0).getType() != TokenType.LBRACE) consume(TokenType.COMMA);
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
            return Long.parseLong(text, 31);
        }
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            throw new SPKException("TokenError", "Token '" + current.getType() + "' doesn't match " + type + " at line " + current.getLine());
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