package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayValue implements Value {
    private Value[] elements;

    public ArrayValue(int size) {
        this.elements = new Value[size];
    }

    public ArrayValue(Value[] elements) {
        this.elements = new Value[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }
    public ArrayValue(ArrayValue array) {
        this(array.elements);
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
        return elements[index];
    }

    public void append(Value expr) {
        elements = Arrays.copyOf(elements, elements.length + 1);
        elements[elements.length - 1] = expr;
    }

    public void set(int index, Value value) {
        elements[index] = value;
    }

    public int length() {
        return elements.length;
    }

    public Value[] array() {
        return elements;
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
        return Arrays.toString(elements);
    }

    @Override
    public String toString() {
        return asString();
    }
}
