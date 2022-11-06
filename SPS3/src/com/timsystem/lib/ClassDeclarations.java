package com.timsystem.lib;

import com.timsystem.ast.ClassStatement;

import java.util.HashMap;
import java.util.Map;

public final class ClassDeclarations {

    private static final Map<String, ClassStatement> declarations;

    static {
        declarations = new HashMap<>();
    }

    private ClassDeclarations() {
    }

    public static void clear() {
        declarations.clear();
    }

    public static Map<String, ClassStatement> getAll() {
        return declarations;
    }

    public static boolean isExists(String key) {
        return declarations.containsKey(key);
    }

    public static ClassStatement get(String key) {
        if (!isExists(key)) throw new SPKException("UnboundFunctionException", "Unbound function '" + key + "'");
        return declarations.get(key);
    }

    public static void set(String key, ClassStatement classDef) {
        declarations.put(key, classDef);
    }
}
