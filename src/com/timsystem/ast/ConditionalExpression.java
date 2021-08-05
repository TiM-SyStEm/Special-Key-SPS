package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.nio.charset.StandardCharsets;

public final class ConditionalExpression implements Expression {

    private final Expression expr1, expr2;
    private final char operation;

    public ConditionalExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public NumberValue eval() {
        return eval(expr1.eval(), expr2.eval());
    }

    private NumberValue eval(Value value1, Value value2) {
        if (value1 instanceof StringValue) {
            return eval((StringValue) value1, value2);
        } else {
            double value1n = value1.asNumber();
            double value2n = value2.asNumber();
            switch (operation) {
                case '<':
                    return new NumberValue(value1n < value2n);
                case '>':
                    return new NumberValue(value1n > value2n);
                case '=':
                default:
                    return new NumberValue(value1n == value2n);
            }
        }
    }

    private NumberValue eval(StringValue value1, Value value2) {
        String value = value1.decode();
        switch (operation) {
            case '=':
                return new NumberValue(value.equals(value2.toString()));
            case '>':
                return new NumberValue(value.compareTo(value2.toString()) > 0);
            case '<':
                return new NumberValue(value.compareTo(value2.toString()) < 0);
            default:
                throw new SPKException("UnsupportedOperationException", "Unsupported operation '" + operation + " for strings");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %c %s", expr1, operation, expr2);
    }
}
