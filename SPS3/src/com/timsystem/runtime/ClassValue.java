package com.timsystem.runtime;

import com.timsystem.ast.ClassDeclaration;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassValue implements Value {

    public String name;
    public Map<String, Value> fields;
    public List<String> fieldsName;
    private Function toString;
    private boolean isStatic;

    public ClassValue(String name, List<String> fieldsName) {
        this.name = name;
        this.fieldsName = fieldsName;
        this.fields = new HashMap<>();
    }
    public ClassValue(String name, List<String> fieldsName, boolean isStatic) {
        this.name = name;
        this.fieldsName = fieldsName;
        this.fields = new HashMap<>();
        this.isStatic = isStatic;
    }
    public boolean isStatic(){
        return isStatic;
    }
    public void setStatic(boolean aStatic){
        isStatic = aStatic;
    }
    public int getArgsCount() {
        return fieldsName.size();
    }

    public String getArg(int index) {
        return fieldsName.get(index);
    }

    public void setField(String key, Value value) {
        if (key.equals("toString")) {
            if (!(value instanceof FunctionValue)) throw new SPKException("BadToStringException", "Expected function as toString");
            toString = ((FunctionValue) value).getValue();
        }
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
        if (toString != null)
            return toString.execute(new Value[] {}).toString();
        return "#Class<" + hashCode() + ">";
    }
}
