package com.timsystem.ast;

import com.timsystem.runtime.ArrayValue;
import com.timsystem.runtime.DictValue;
import com.timsystem.runtime.Value;

import java.util.ArrayList;
import java.util.List;

public final class DictExpression implements Expression {

    private final List<Expression[]> elements;

    public DictExpression(List<Expression[]> arguments) {
        this.elements = arguments;
    }

    @Override
    public Value eval() {
        final int size = elements.size();
        List<Value[]> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(new Value[]{elements.get(i)[0].eval(), elements.get(i)[1].eval()});
        }
        return new DictValue(values);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
