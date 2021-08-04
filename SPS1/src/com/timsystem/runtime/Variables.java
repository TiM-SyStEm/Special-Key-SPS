package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.HashMap;
import java.util.Map;


public final class Variables {

    private static final Map<String, Value> variables;

    static {
        variables = new HashMap<>();
    }

    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static Value get(String key) {
        if (!isExists(key))
            throw new SPKException("UnboundVariableException", String.format("Unbound local or global variable '%s'", key));
        return variables.get(key);
    }

    public static void set(String key, Value value) {
        variables.put(key, value);
    }
}
