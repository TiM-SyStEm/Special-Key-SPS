package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ArrayValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public final class ForeachArrayStatement implements Statement {

    public final String variable;
    public final Expression container;
    public final Statement body;

    public ForeachArrayStatement(String variable, Expression container, Statement body) {
        this.variable = variable;
        this.container = container;
        this.body = body;
    }

    @Override
    public void execute() {
        final Value previousVariableValue = Variables.isExists(variable) ? Variables.get(variable) : null;

        final Value containerValue = container.eval();
        if (containerValue instanceof ArrayValue) {
            iterateArray((ArrayValue) containerValue);
        } else if (containerValue instanceof StringValue) {
            iterateString(((StringValue) containerValue).decode());
        } else throw new SPKException("BadIterator", "Cannot iterate " + containerValue);

        // Restore variables
        if (previousVariableValue != null) {
            Variables.set(variable, previousVariableValue);
        } else {
            Variables.del(variable);
        }
    }

    private void iterateString(String str) {
        for (char ch : str.toCharArray()) {
            Variables.set(variable, new StringValue(String.valueOf(ch)));
            try {
                body.execute();
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    private void iterateArray(ArrayValue containerValue) {
        for (Value value : containerValue.array()) {
            Variables.set(variable, value);
            try {
                body.execute();
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }


    @Override
    public String toString() {
        return String.format("for %s : %s %s", variable, container, body);
    }
}
