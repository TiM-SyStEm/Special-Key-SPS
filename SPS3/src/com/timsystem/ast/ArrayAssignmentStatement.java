package com.timsystem.ast;

public final class ArrayAssignmentStatement implements Statement {
    private final ArrayAccessExpression array;
    private final Expression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }

    @Override
    public void execute() {
        array.getArray().set(array.lastIndex(), expression.eval());
    }

    public ArrayAccessExpression getArray() {
        return array;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", array, expression);
    }
}