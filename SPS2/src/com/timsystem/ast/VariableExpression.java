package com.timsystem.ast;

import com.timsystem.lib.GettableSettable;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public final class VariableExpression implements Expression, GettableSettable {

    public final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        return Variables.get(name);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

    @Override
    public Value get() {
        return eval();
    }

    @Override
    public void set(Value expr) {
        Variables.set(name, expr);
    }
}
