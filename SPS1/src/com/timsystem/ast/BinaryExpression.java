package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.nio.charset.StandardCharsets;


public final class BinaryExpression implements Expression {

    private final Expression expr1, expr2;
    private final char operation;

    public BinaryExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        return eval(expr1.eval(), expr2.eval());
    }

    private Value eval(Value value1, Value value2) {
        switch (operation) {
            case '+':
                return add(value1, value2);
            case '-':
                return subtract(value1, value2);
            case '*':
                return multiply(value1, value2);
            case '/':
                return divide(value1, value2);
            case '^':
                return pow(value1, value2);
            case '%':
                return remains(value1, value2);
            default:
                return value1;
        }
    }

    private Value add(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return add((NumberValue) value1, value2);
        else return new StringValue((value1.toString() + value2.toString()).getBytes(StandardCharsets.UTF_8));
    }

    private Value add(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof Number) {
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.intValue() + number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() + number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() + number2.longValue());
            }
            return NumberValue.of(number1.intValue() + number2.intValue());
        }

        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() + value2.asNumber());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() + value2.asNumber());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() + value2.asInt());
        }
        return NumberValue.of(number1.intValue() + value2.asInt());
    }

    private Value subtract(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return subtract((NumberValue) value1, value2);
        else
            throw new SPKException("UnsupportedOperationException", "Operation '-' is supported only for numbers");
    }

    private Value subtract(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof NumberValue) {
            // number1 - number2
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() - number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() - number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() - number2.longValue());
            }
            return NumberValue.of(number1.intValue() - number2.intValue());
        }
        // number1 - other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() - value2.asNumber());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() - value2.asNumber());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() - value2.asInt());
        }
        return NumberValue.of(number1.intValue() - value2.asInt());
    }

    private Value multiply(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return multiply((NumberValue) value1, value2);
        else if (value1 instanceof StringValue)
            return multiply((StringValue) value1, value2);
        else
            throw new SPKException("UnsupportedOperationException", "Operation '*' is supported only for numbers and strings");
    }

    private Value multiply(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof NumberValue) {
            // number1 * number2
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() * number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() * number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() * number2.longValue());
            }
            return NumberValue.of(number1.intValue() * number2.intValue());
        }

        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() * value2.asNumber());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() * value2.asNumber());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() * value2.asInt());
        }
        return NumberValue.of(number1.intValue() * value2.asInt());
    }

    private Value multiply(StringValue value1, Value value2) {
        final String string1 = value1.toString();
        final int iterations = value2.asInt();
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            buffer.append(string1);
        }
        return new StringValue(buffer.toString().getBytes(StandardCharsets.UTF_8));
    }

    private Value divide(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return divide((NumberValue) value1, value2);
        else
            throw new SPKException("UnsupportedOperationException", "Operation '/' supported only for numbers");
    }

    private Value divide(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof NumberValue) {
            // number1 / number2
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() / number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() / number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() / number2.longValue());
            }
            return NumberValue.of(number1.intValue() / number2.intValue());
        }
        // number1 / other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() / value2.asNumber());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() / value2.asNumber());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() / value2.asInt());
        }
        return NumberValue.of(number1.intValue() / value2.asInt());
    }

    private Value pow(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return pow2((NumberValue) value1, (NumberValue) value2);
        else
            throw new SPKException("UnsupportedOperationException", "Operation '^' supported only for numbers");
    }

    private Value pow2(NumberValue value1, NumberValue value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof NumberValue) {
            // number1 / number2
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(Math.pow(number1.doubleValue(), number2.doubleValue()));
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(Math.pow(number1.floatValue(), number2.floatValue()));
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(Math.pow(number1.longValue(), number2.longValue()));
            }
            return NumberValue.of(Math.pow(number1.intValue(), number2.intValue()));
        }
        // number1 / other
        else if (number1 instanceof Double) {
            return NumberValue.of(Math.pow(number1.doubleValue(), value2.asNumber()));
        } else if (number1 instanceof Float) {
            return NumberValue.of(Math.pow(number1.floatValue(), value2.asNumber()));
        } else if (number1 instanceof Long) {
            return NumberValue.of(Math.pow(number1.longValue(), value2.asInt()));
        }
        return NumberValue.of(Math.pow(number1.intValue(), value2.asInt()));
    }

    private Value remains(Value value1, Value value2) {
        if (value1 instanceof NumberValue)
            return remains2((NumberValue) value1, (NumberValue) value2);
        else
            throw new SPKException("UnsupportedOperationException", "Operation '%' supported only for numbers");
    }

    private Value remains2(NumberValue value1, NumberValue value2) {
        final Number number1 = value1.raw();
        if (value2 instanceof NumberValue) {
            // number1 / number2
            final Number number2 = (Number) value2.raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() % number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() % number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() % number2.longValue());
            }
            return NumberValue.of(number1.intValue() % number2.intValue());
        }
        // number1 / other
        else if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() % value2.asNumber());
        } else if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() % value2.asNumber());
        } else if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() % value2.asInt());
        }
        return NumberValue.of(number1.intValue() % value2.asInt());
    }

    @Override
    public String toString() {
        return String.format("%s %c %s", expr1, operation, expr2);
    }
}
