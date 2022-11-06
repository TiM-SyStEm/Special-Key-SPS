package com.timsystem.ast;

import com.timsystem.lib.GettableSettable;
import com.timsystem.runtime.*;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ClassValue;

import java.util.List;

public final class ContainerAccessExpression implements Expression, GettableSettable {

    public final Expression root;
    public final List<Expression> indices;
    private boolean rootIsVariable;

    public ContainerAccessExpression(String variable, List<Expression> indices) {
        this(new VariableExpression(variable), indices);
    }

    public ContainerAccessExpression(Expression root, List<Expression> indices) {
        rootIsVariable = root instanceof VariableExpression;
        this.root = root;
        this.indices = indices;
    }

    public boolean rootIsVariable() {
        return rootIsVariable;
    }

    public Expression getRoot() {
        return root;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public Value get() {
        Value container = getContainer();
        for (int i = 0; i < indices.size(); i++) {
            Value lastIndex = indices.get(i).eval();
            if (container instanceof ArrayValue)
                container = ((ArrayValue) container).get(lastIndex.asInt());
            else if (container instanceof ClassValue) {
                // StringValue key = (StringValue) lastIndex;
                container = ((ClassValue) container).getField(lastIndex.toString());
            } else if (container instanceof MapValue) {
                container =(((MapValue) container).get(lastIndex));
            } else if (container instanceof EnumValue) {
                container = ((EnumValue) container).get(lastIndex.toString());
            }
        }
        return container;
    }

    @Override
    public void set(Value value) {
        final Value container = getContainer();
        final Value lastIndex = lastIndex();
        if (container instanceof ArrayValue) {
            final int arrayIndex = lastIndex.asInt();
            ((ArrayValue) container).set(arrayIndex, value);
        }
    }

    public Value getContainer() {
        Value container = root.eval();
        final int last = indices.size() - 1;
        for (int i = 0; i < last; i++) {
            final Value index = index(i);
            if (container instanceof ArrayValue) {
                final int arrayIndex = index.asInt();
                container = ((ArrayValue) container).get(arrayIndex);
            }
        }
        return container;
    }

    public Value lastIndex() {
        return index(indices.size() - 1);
    }

    private Value index(int index) {
        return indices.get(index).eval();
    }

    @Override
    public String toString() {
        return root.toString() + indices;
    }
}
