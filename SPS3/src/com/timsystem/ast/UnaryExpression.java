package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

/**
 * @author aNNiMON
 */
public final class UnaryExpression implements Expression {

    private final Expression expr1;
    private final char operation;

    public UnaryExpression(char operation, Expression expr1) {
        this.operation = operation;
        this.expr1 = expr1;
    }

    @Override
    public Value eval() {
        switch (operation) {
            case '-':
                return NumberValue.of(-expr1.eval().asNumber());
            case '!':
                return NumberValue.fromBoolean(!expr1.eval().asBool());
            case '+':
            default:
                return expr1.eval();
        }
    }

    public Expression getExpr1() {
        return expr1;
    }

    public char getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return String.format("%c %s", operation, expr1);
    }
}
