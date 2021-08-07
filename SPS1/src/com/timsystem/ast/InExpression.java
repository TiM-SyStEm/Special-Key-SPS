package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

public class InExpression implements Expression {
    private Expression obj;
    private Expression container;

    public InExpression(Expression obj, Expression container) {
        this.obj = obj;
        this.container = container;
    }

    @Override
    public Value eval() {
        return eval(obj.eval(), container.eval());
    }

    private Value eval(Value object, Value target) {
        if (target instanceof StringValue) {
            return new NumberValue(((StringValue) target).decode().contains(object.toString()));
        } else
            throw new SPKException("InOperatorException", "Operator 'in' is supported only by strings");
    }
}
