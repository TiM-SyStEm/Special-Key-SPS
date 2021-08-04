package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public final class ReAssignmentStatement implements Statement {

    private final String variable;
    private final Expression expression;

    public ReAssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        if (!Variables.isExists(variable)) throw new SPKException("ReAssignmentError", "Trying to re-assign variable that doesnt exists");
        final Value result = expression.eval();
        Variables.set(variable, result);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
