package com.timsystem.ast;

public class IncrementStatement implements Statement {
    private final String variable;

    public IncrementStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public void execute() {

    }
}
