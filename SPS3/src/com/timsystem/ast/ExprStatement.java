package com.timsystem.ast;

public class ExprStatement implements Statement {

    private Expression expr;

    public ExprStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute() {
        expr.eval();
    }

    @Override
    public String toString() {
        return expr.toString();
    }

    public Expression getExpr() {
        return expr;
    }
}
