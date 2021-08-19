package com.timsystem.lib;


import com.timsystem.runtime.UserDefinedClass;

import java.util.HashMap;
import java.util.Map;

public final class Classes {

    private static final Map<String, UserDefinedClass> classes;

    static {
        classes = new HashMap<>();
    }

    private Classes() {
    }

    public static void clear() {
        classes.clear();
    }

    public static Map<String, UserDefinedClass> getFunctions() {
        return classes;
    }

    public static boolean isExists(String key) {
        return classes.containsKey(key);
    }

    public static UserDefinedClass get(String key) {
        if (!isExists(key)) throw new SPKException("UnknownInstantiable", "Unknown class or instantiable object");
        return classes.get(key);
    }

    public static void set(String key, UserDefinedClass classDef) {
        classes.put(key, classDef);
    }
}
