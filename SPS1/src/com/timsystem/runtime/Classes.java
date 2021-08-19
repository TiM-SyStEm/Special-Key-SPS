package com.timsystem.runtime;
import com.timsystem.lib.SPKClass;
import com.timsystem.lib.SPKException;

import java.util.HashMap;
import java.util.Map;

public class Classes {
    public static final Map<String, SPKClass> classes;
    static {
        classes = new HashMap<>();
    }
    public static boolean isExists(String key) {
        return classes.containsKey(key);
    }

    public static SPKClass get(String key) {
        if (!isExists(key))
            throw new SPKException("UnboundFunctionException", String.format("Unbound function '%s'", key));
        return classes.get(key);
    }

    public static void set(String key, SPKClass spkClass) {
        classes.put(key, spkClass);
    }

    public static void clear() {
        classes.clear();
    }
}
