package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.HashMap;

public class YAMLValue implements Value {

    private HashMap<String, Object> complex;
    private HashMap<String, String> data;

    public YAMLValue(HashMap<String, Object> complex, HashMap<String, String> data) {
        this.complex = complex;
        this.data = data;
    }

    @Override
    public Object raw() {
        return data;
    }

    @Override
    public int asInt() {
        throw new SPKException("CastException", "YAML Class is not castable!");
    }

    @Override
    public boolean asBool() {
        throw new SPKException("CastException", "YAML Class is not castable!");
    }

    @Override
    public double asNumber() {
        throw new SPKException("CastException", "YAML Class is not castable!");
    }

    @Override
    public String asString() {
        return data.toString();
    }

    public int size() {
        return data.size();
    }

    public String get(Object key) {
        return data.get(key);
    }

    public Object complexPut(String key, Object value) {
        return complex.put(key, value);
    }

    public Object complexGet(String key) {
        return complex.get(key);
    }

    public int complexSize() {
        return complex.size();
    }

    public String put(String key, String value) {
        return data.put(key, value);
    }

    public HashMap<String, Object> getComplex() {
        return complex;
    }

    public void setComplex(HashMap<String, Object> complex) {
        this.complex = complex;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
