package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayValue implements Value {

    private List<Value> elements;

    public ArrayValue(int size) {
        this.elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            elements.add(null);
        }
    }

    public ArrayValue(Value[] elements) {
        this.elements = Arrays.asList(elements);
    }

    public ArrayValue(ArrayValue array) {
        this(array.array());
    }

    public static ArrayValue of(String[] array) {
        final int size = array.length;
        final ArrayValue result = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            result.set(i, new StringValue(array[i]));
        }
        return result;
    }

    public static ArrayValue of(char[] array) {
        final int size = array.length;
        final ArrayValue result = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            String _char = Character.toString(array[i]);
            result.set(i, new StringValue(_char));
        }
        return result;
    }

    public Value get(int index) {
        return elements.get(index);
    }

    public void append(Value expr) {
        elements.add(expr);
    }

    public void set(int index, Value value) {
        elements.set(index, value);
    }

    public int length() {
        return elements.size();
    }

    public Value[] array() {
        return elements.toArray(new Value[] {});
    }

    @Override
    public Object raw() {
        return toString();
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeError", "Cannot cast array to int");
    }

    @Override
    public boolean asBool() {
        return false;
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeError", "Cannot cast array to number");
    }

    @Override
    public String asString() {
        return elements.toString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
