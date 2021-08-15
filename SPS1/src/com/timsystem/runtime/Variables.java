package com.timsystem.runtime;

import com.timsystem.lib.SPKException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public final class Variables {

    private static final Stack<Map<String, Value>> stack;
    private static Map<String, Value> variables;

    static {
        stack = new Stack<>();
        variables = new HashMap<>();
        Variables.set("True", new NumberValue(1));
        Variables.set("False", new NumberValue(0));
        Variables.set("Null", new StringValue("\0"));
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

    public static void clear() {
        variables.clear();
    }
    public static void push() {
        stack.push(new HashMap<>(variables));
    }

    public static void pop() {
        variables = stack.pop();
    }
}
