package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;

import java.util.Arrays;

public class InExpression implements Expression {
    private final Expression obj;
    private final Expression container;

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
        } else if (target instanceof ArrayValue) {
            return new NumberValue(Arrays.asList(((ArrayValue) target).array()).contains(object));
        } else if (target instanceof EnumValue) {
            return new NumberValue(((EnumValue) target).get(obj.eval().toString()) == null);
        } else
            throw new SPKException("InOperatorException", "Operator 'in' is supported only by strings, enums and lists");
    }
}
