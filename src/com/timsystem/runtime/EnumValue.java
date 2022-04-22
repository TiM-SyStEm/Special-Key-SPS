package com.timsystem.runtime;

import java.util.Map;

public class EnumValue implements Value {

    private Map<String, StringValue> enums;

    public EnumValue(Map<String, StringValue> enums) {
        this.enums = enums;
    }

    public StringValue get(String enm) {
        return enums.get(enm);
    }

    @Override
    public Object raw() {
        return enums;
    }

    @Override
    public int asInt() {
        return enums.size();
    }

    @Override
    public boolean asBool() {
        return enums.isEmpty();
    }

    @Override
    public double asNumber() {
        return enums.size();
    }

    @Override
    public String asString() {
        return enums.toString();
    }
}
