package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

public class StringValue implements Value {

    private final byte[] string;

    public StringValue(byte[] string) {
        this.string = string;
    }

    @Override
    public Object raw() {
        return new String(string);
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeError", "Cannot cast string to number");
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeError", "Cannot cast string to number");
    }

    @Override
    public String toString() {
        return new String(string);
    }
}
