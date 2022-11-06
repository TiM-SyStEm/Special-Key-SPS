package com.timsystem.runtime;

import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;

public class FunctionValue implements Value, Function {

    public static final FunctionValue EMPTY = new FunctionValue(args -> NumberValue.ZERO);

    private Function function;
    private boolean isPrivate;
    private boolean isProtected;

    public FunctionValue(Function function) {
        this.function = function;
    }

    public FunctionValue(Function function, boolean isPrivate, boolean isProtected) {
        this.function = function;
        this.isPrivate = isPrivate;
        this.isProtected = isProtected;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
    public boolean isProtected() {
        return isProtected;
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public void setProtected(boolean aProtected) {
        isPrivate = aProtected;
    }

    public Function getValue() {
        return function;
    }

    @Override
    public Value execute(Value... args) {
        return function.execute();
    }

    @Override
    public Object raw() {
        return asString();
    }

    @Override
    public int asInt() {
        throw new SPKException("CastError", "Unable cast function to number");
    }

    @Override
    public boolean asBool() {
        throw new SPKException("CastError", "Unable cast function to boolean");
    }

    @Override
    public double asNumber() {
        throw new SPKException("CastError", "Unable cast function to number");
    }

    @Override
    public String asString() {
        return "#Function<" + hashCode() + ">";
    }

    @Override
    public String toString() {
        return asString();
    }
}