package com.timsystem.ast;

import com.timsystem.lib.GettableSettable;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Value;

public class SuffixExpression implements Expression {

    private char operation;
    private Expression expr;

    public SuffixExpression(char operation, Expression expr) {
        this.operation = operation;
        this.expr = expr;
    }

    @Override
    public Value eval() {
        Value value = expr.eval();
        switch (operation) {
            case '-': {
                if (expr instanceof GettableSettable) {
                    ((GettableSettable) expr).set(decrement(value));
                }
                return decrement(value);
            }
            case '+': {
                if (expr instanceof GettableSettable) {
                    ((GettableSettable) expr).set(increment(value));
                    return value;
                }
                return increment(value);
            }
            default:
                return expr.eval();
        }
    }

    private Value increment(Value value) {
        if (value instanceof NumberValue) {
            final Number number = (Number) value.raw();
            if (number instanceof Double) {
                return NumberValue.of(number.doubleValue() + 1);
            }
            if (number instanceof Float) {
                return NumberValue.of(number.floatValue() + 1);
            }
            if (number instanceof Long) {
                return NumberValue.of(number.longValue() + 1);
            }
        }
        return NumberValue.of(value.asInt() + 1);
    }

    private Value decrement(Value value) {
        if (value instanceof NumberValue) {
            final Number number = (Number) value.raw();
            if (number instanceof Double) {
                return NumberValue.of(number.doubleValue() - 1);
            }
            if (number instanceof Float) {
                return NumberValue.of(number.floatValue() - 1);
            }
            if (number instanceof Long) {
                return NumberValue.of(number.longValue() - 1);
            }
        }
        return NumberValue.of(value.asInt() - 1);
    }

    public char getOperation() {
        return operation;
    }

    public Expression getExpr() {
        return expr;
    }
}
