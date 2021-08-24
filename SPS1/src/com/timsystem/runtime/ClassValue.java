package com.timsystem.runtime;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassValue implements Value {

    public String name;
    public Map<String, Value> fields;
    public List<String> fieldsName;

    public ClassValue(String name, List<String> fieldsName) {
        this.name = name;
        this.fieldsName = fieldsName;
        this.fields = new HashMap<>();
    }

    public int getArgsCount() {
        return fieldsName.size();
    }

    public String getArg(int index) {
        return fieldsName.get(index);
    }

    public void setField(String key, Value value) {
        fields.put(key, value);
    }

    public Value getField(String key) {
        Value result = fields.get(key);
        if (result == null) throw new SPKException("UnboundStructField", "Unbound field '" + key + "'");
        return result;
    }

    @Override
    public Object raw() {
        return asString();
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeException", "Cannot cast struct to a number");
    }

    @Override
    public boolean asBool() {
        throw new SPKException("TypeException", "Cannot cast struct to a bool");
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeException", "Cannot cast struct to a number");
    }

    @Override
    public String asString() {
        return "#Struct<" + hashCode() + ">";
    }
}
