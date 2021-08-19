package com.timsystem.lib;

import com.timsystem.runtime.ArrayValue;
import com.timsystem.runtime.FunctionValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class MapValue implements Value {

    public static final MapValue EMPTY = new MapValue(1);
    private final Map<Value, Value> map;

    public MapValue(int size) {
        this.map = new LinkedHashMap<>(size);
    }

    public MapValue(Map<Value, Value> map) {
        this.map = map;
    }

    public static MapValue merge(MapValue map1, MapValue map2) {
        final MapValue result = new MapValue(map1.size() + map2.size());
        result.map.putAll(map1.map);
        result.map.putAll(map2.map);
        return result;
    }

    public boolean ifPresent(String key, Consumer<Value> consumer) {
        return ifPresent(new StringValue(key), consumer);
    }

    public boolean ifPresent(Value key, Consumer<Value> consumer) {
        if (map.containsKey(key)) {
            consumer.accept(map.get(key));
            return true;
        }
        return false;
    }

    public ArrayValue toPairs() {
        final int size = map.size();
        final ArrayValue result = new ArrayValue(size);
        int index = 0;
        for (Map.Entry<Value, Value> entry : map.entrySet()) {
            result.set(index++, new ArrayValue(new Value[]{
                    entry.getKey(), entry.getValue()
            }));
        }
        return result;
    }

    public int size() {
        return map.size();
    }

    public boolean containsKey(Value key) {
        return map.containsKey(key);
    }

    public Value get(Value key) {
        return map.get(key);
    }

    public void set(String key, Value value) {
        set(new StringValue(key), value);
    }

    public void set(String key, Function function) {
        set(new StringValue(key), new FunctionValue(function));
    }

    public void set(Value key, Value value) {
        map.put(key, value);
    }

    public Map<Value, Value> getMap() {
        return map;
    }

    @Override
    public Object raw() {
        return map;
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeException", "Cannot cast map to integer");
    }

    @Override
    public boolean asBool() {
        return map.isEmpty();
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeException", "Cannot cast map to number");
    }

    @Override
    public String asString() {
        return map.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final MapValue other = (MapValue) obj;
        return Objects.equals(this.map, other.map);
    }

    @Override
    public String toString() {
        return asString();
    }
}
