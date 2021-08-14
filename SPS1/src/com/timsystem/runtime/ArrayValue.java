package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.Arrays;

public class ArrayValue implements Value{
    private final Value[] elements;

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

    public Value get(int index) {
        return elements[index];
    }

    public void set(int index, Value value) {
        elements[index] = value;
    }

    @Override
    public Object raw() {
        return asString();
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeError","Cannot cast array to int");
    }

    @Override
    public boolean asBool() {
        return false;
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeError","Cannot cast array to number");
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
