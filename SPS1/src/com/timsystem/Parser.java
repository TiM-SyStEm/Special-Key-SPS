package com.timsystem;

import com.timsystem.ast.*;
import com.timsystem.lib.SPKException;
import com.timsystem.lib.Token;
import com.timsystem.lib.TokenType;

import java.util.ArrayList;
import java.util.List;


public final class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "");

    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public List<Statement> parse() {
        final List<Statement> result = new ArrayList<>();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }
        return result;
    }

    private Statement statement() {
        if (match(TokenType.OUT)) {
            return outStatement();
        }
        if (match(TokenType.ADD)) {
            return new AddStatement(consume(TokenType.WORD).getText());
        }
        return assignmentsStatement();
    }

    private Statement outStatement() {
        consume(TokenType.COLON);
        return new OutStatement(expression());
    }

    private Statement assignmentsStatement() {
        final Token current = get(0);
        if (current.getType() == TokenType.WORD && get(1).getType() == TokenType.EQ) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(variable, expression());
        }
        throw new SPKException("StatementError", String.format("unknown statement '%s'", current.getText()));
    }


    private Expression expression() {
        return additive();
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
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
            if (match(TokenType.SLASH)) {
                result = new BinaryExpression('/', result, unary());
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
        if (match(TokenType.PLUS)) {
            return primary();
        }
        return primary();
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(createNumber(current.getText(), 32));
        } else if (match(TokenType.HEX_NUMBER)) {
            return new ValueExpression(createNumber(current.getText(), 32));
        } else if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        } else if (match(TokenType.STRING)) {
            return new ValueExpression(current.getText());
        } else if (match(TokenType.LPAREN)) {
            Expression result = expression();
            match(TokenType.RPAREN);
            return result;
        }
        throw new SPKException("ExpressionError", String.format("unknown expression '%s'", current.getText()));
    }

    private Number createNumber(String text, int radix) {
        // Double
        if (text.contains(".")) {
            return Double.parseDouble(text);
        }
        // Integer
        try {
            return Integer.parseInt(text, radix);
        } catch (NumberFormatException nfe) {
            return Long.parseLong(text, radix);
        }
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            throw new SPKException("TokenError", "Token '" + current.getText() + "' doesn't match " + type);
        //    throw new RuntimeException("Token " + current + " doesn't match " + type);
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
}