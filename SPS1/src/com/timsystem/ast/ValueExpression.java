package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.nio.charset.StandardCharsets;

public final class ValueExpression implements Expression {

    private final Value value;

    public ValueExpression(String value) {
        this.value = new StringValue(value.getBytes(StandardCharsets.UTF_8));
    }

    public ValueExpression(Number value) {
        this.value = NumberValue.of(value);
    }

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value.raw());
    }
}
