package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public final class VariableExpression implements Expression {

    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (!Variables.isExists(name)) throw new SPKException("UndoundVariableExpression", "Constant does not exists");
        return Variables.get(name);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
