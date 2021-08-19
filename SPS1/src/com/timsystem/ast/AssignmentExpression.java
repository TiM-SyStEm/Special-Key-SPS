package com.timsystem.ast;

import com.timsystem.lib.GettableSettable;
import com.timsystem.runtime.Value;

public final class AssignmentExpression implements Expression, Statement {

    public final GettableSettable target;
    public final Expression expression;

    public AssignmentExpression(GettableSettable target, Expression expr) {
        this.target = target;
        this.expression = expr;
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        // Simple assignment
        target.set(expression.eval());
        return expression.eval();
    }

    @Override
    public String toString() {
        return String.format("%s = %s", target, expression);
    }
}
