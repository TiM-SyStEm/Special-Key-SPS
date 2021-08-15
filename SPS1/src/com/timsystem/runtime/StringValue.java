package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.nio.charset.StandardCharsets;

public class StringValue implements Value {

    private final byte[] string;

    public StringValue(String str) {
        this(str.getBytes(StandardCharsets.UTF_8));
    }

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
    public String asString() {
        return string.toString();
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeError", "Cannot cast string to number");
    }

    public String decode() {
        return new String(string);
    }

    @Override
    public boolean asBool() {
        return decode() != "" ? true : false;
    }

    @Override
    public String toString() {
        return new String(string);
    }
}
