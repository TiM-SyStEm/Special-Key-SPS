package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
        return Double.parseDouble(toString());
    }

    @Override
    public String asString() {
        return string.toString();
    }

    @Override
    public int asInt() {
        return Integer.parseInt(toString());
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final StringValue other = (StringValue) obj;
        return Objects.equals(this.decode(), other.toString());
    }

}