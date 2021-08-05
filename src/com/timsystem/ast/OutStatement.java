package com.timsystem.ast;

public class OutStatement implements Statement {
    private final Expression expr;

    public OutStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute() {
        System.out.println(expr.eval().raw());
    }

    @Override
    public String toString() {
        return "out : " + expr;
    }
}
