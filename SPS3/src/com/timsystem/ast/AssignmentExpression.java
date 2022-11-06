package com.timsystem.ast;

import com.timsystem.lib.GettableSettable;
import com.timsystem.lib.Handler;
import com.timsystem.runtime.ClassValue;
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
        Value res = Handler.setInstenced(expression.eval());
        target.set(res);
        return res;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", target, expression);
    }
}