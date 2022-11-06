package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public class FieldStatement implements Statement{

    private final String variable;

    public FieldStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {
        Variables.set(variable, NumberValue.MINUS_ONE);
    }

    @Override
    public String toString() {
        return String.format("field %s", variable);
    }

    public String getVariable() {
        return variable;
    }
}
