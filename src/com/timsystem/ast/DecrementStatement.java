package com.timsystem.ast;

public class DecrementStatement implements Statement {
    private final String variable;

    public DecrementStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {

    }
}
