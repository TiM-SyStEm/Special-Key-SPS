package com.timsystem.ast;

public class ContinueStatement extends RuntimeException implements Statement {
    @Override
    public void execute() {
        throw this;
    }

    @Override
    public String toString() {
        return "continue";
    }
}
