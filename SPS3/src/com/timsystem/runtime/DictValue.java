package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.*;

public class DictValue implements Value {
    private Map<Value, Value> elements;

    public DictValue(HashMap<Value, Value> map){
        this.elements = map;
    }
    public DictValue(List<Value[]> key_val) {
        elements = new HashMap<>();
        for(int i = 0; i < key_val.size(); i++){
            for(int j = 0; j < key_val.get(i).length; j++){
                elements.put(key_val.get(i)[0], key_val.get(i)[1]);
            }
        }
    }

    public int length() {
        return elements.size();
    }
    public Value getValue(Value key){
        Set<Map.Entry<Value, Value>> entrySet=elements.entrySet();
        for (Map.Entry<Value, Value> pair : entrySet) {
            if (key.equals(pair.getKey())) {
                return pair.getValue();
            }
        }
        return NumberValue.MINUS_ONE;
    }
    public Value getKey(Value value){
        for (Map.Entry<Value, Value> entry : elements.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return NumberValue.MINUS_ONE;
    }
    public Value[] keys(){
        Value[] keys = elements.keySet().toArray(new Value[]{});
        return keys;
    }
    public Value[] values(){
        Value[] values = elements.values().toArray(new Value[]{});
        return values;
    }
    public Value clear(){
        elements.clear();
        return NumberValue.MINUS_ONE;
    }
    public Value copy(){
        return new DictValue(new HashMap<>(elements));
    }
    public Value update(Value key, Value value){
        elements.put(key, value);
        return NumberValue.MINUS_ONE;
    }
    public Value remove(Value key){
        elements.remove(key);
        return NumberValue.MINUS_ONE;
    }
    public Value isEmpty(){
        boolean res = elements.isEmpty();
        if(res)
            return NumberValue.ONE;
        else
            return NumberValue.ZERO;
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
        return elements.toString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
