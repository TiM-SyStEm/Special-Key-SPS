package com.timsystem.ast;

import com.timsystem.runtime.Value;

public class ReturnStatement extends RuntimeException implements Statement{
    private final Expression expression;
    private Value returnValue;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }
    public Value getReturnValue(){
        return returnValue;
    }
    @Override
    public void execute() {
        returnValue = expression.eval();
        throw this;
    }
    @Override
    public String toString() {
        return "return";
    }
}
