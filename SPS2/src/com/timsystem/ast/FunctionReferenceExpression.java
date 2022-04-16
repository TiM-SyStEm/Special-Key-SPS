package com.timsystem.ast;

import com.timsystem.runtime.FunctionValue;
import com.timsystem.runtime.Functions;

public final class FunctionReferenceExpression implements Expression {

    public final String name;

    public FunctionReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public FunctionValue eval() {
        return new FunctionValue(Functions.get(name));
    }

    @Override
    public String toString() {
        return "::" + name;
    }
}
