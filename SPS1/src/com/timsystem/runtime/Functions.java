package com.timsystem.runtime;

import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Functions {
    public static final Map<String, Function> functions;

    static {
        functions = new HashMap<>();
    }

    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static Function get(String key) {
        if (!isExists(key))
            throw new SPKException("UnboundFunctionException", String.format("Unbound function '%s'", key));
        return functions.get(key);
    }

    public static void set(String key, Function function) {
        functions.put(key, function);
    }

    public static void clear() {
        functions.clear();
    }
}}
