package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

public final class ConditionalExpression implements Expression {
    private final Expression expr1, expr2;
    private final Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public NumberValue eval() {
        return eval(expr1.eval(), expr2.eval());
    }

    private NumberValue eval(Value value1, Value value2) {
        // System.out.println(value1 + " | " + value2);
        boolean result;
        if (value1 instanceof StringValue) {
            return eval((StringValue) value1, value2);
        } else {
            double value1n = value1.asNumber();
            double value2n = value2.asNumber();

            switch (operation) {
                case LT:
                    result = value1n < value2n;
                    break;
                case LTEQ:
                    result = value1n <= value2n;
                    break;
                case GT:
                    result = value1n > value2n;
                    break;
                case GTEQ:
                    result = value1n >= value2n;
                    break;
                case NOT_EQUALS:
                    result = value1n != value2n;
                    break;
                case AND:
                    result = (value1n != 0) && (value2n != 0);
                    break;
                case OR:
                    result = (value1n != 0) || (value2n != 0);
                    break;
                case EQUALS:
                default:
                    result = value1n == value2n;
                    break;
            }
        }
        return new NumberValue(result);
    }

    private NumberValue eval(StringValue value1, Value value2) {
        String value = value1.decode();
        switch (operation) {
            case EQUALS:
                return new NumberValue(value.equals(value2.toString()));
            case LT:
                return new NumberValue(value.compareTo(value2.toString()) < 0);
            case LTEQ:
                return new NumberValue(value.compareTo(value2.toString()) <= 0);
            case GT:
                return new NumberValue(value.compareTo(value2.toString()) > 0);
            case GTEQ:
                return new NumberValue(value.compareTo(value2.toString()) >= 0);
            case NOT_EQUALS:
                return new NumberValue(value.compareTo(value2.toString()) != 0);
            default:
                throw new SPKException("UnsupportedOperationException", "Unsupported operation '" + operation + " for strings");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", expr1, operation.name(), expr2);
    }

    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        LT,
        LTEQ,
        GT,
        GTEQ,
        AND,
        OR
    }
}
