package com.timsystem.runtime;

import com.timsystem.lib.SPKException;
import com.timsystem.lib.UnboundVariableException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class Variables {

    private static final Object lock = new Object();
    public static volatile Scope scope;

    static {
        Variables.clear();
    }

    private Variables() {
    }

    public static Map<String, Value> variables() {
        return scope.variables;
    }

    public static void clear() {
        scope = new Scope();
        scope.variables.clear();
        scope.variables.put("True", NumberValue.ONE);
        scope.variables.put("False", NumberValue.ZERO);
        scope.variables.put("Null", NumberValue.MINUS_ONE);
    }

    public static void push() {
        synchronized (lock) {
            scope = new Scope(scope);
        }
    }

    public static void pop() {
        synchronized (lock) {
            if (scope.parent != null) {
                scope = scope.parent;
            }
        }
    }
    public static void del(String name) {
        synchronized (lock) {
            findScope(name).scope.variables.remove(name);
        }
    }
    public static boolean isExists(String key) {
        synchronized (lock) {
            return findScope(key).isFound;
        }
    }

    public static Value get(String key) {
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(key);
            if (scopeData.isFound) {
                return scopeData.scope.variables.get(key);
            }
        }
        throw new UnboundVariableException("UnboundVariableException", "Unbound variable '" + key + "'", key);
    }

    public static void set(String key, Value value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }

    public static void define(String key, Value value) {
        synchronized (lock) {
            if (scope.parent != null) {
                scope.parent.variables.put(key, value);
                return;
            }
            scope.variables.put(key, value);
        }
    }

    private static ScopeFindData findScope(String variable) {
        final ScopeFindData result = new ScopeFindData();

        Scope current = scope;
        do {
            if (current.variables.containsKey(variable)) {
                result.isFound = true;
                result.scope = current;
                return result;
            }
        } while ((current = current.parent) != null);

        result.isFound = false;
        result.scope = scope;
        return result;
    }

    private static class Scope {
        final Scope parent;
        final Map<String, Value> variables;

        Scope() {
            this(null);
        }

        Scope(Scope parent) {
            this.parent = parent;
            variables = new ConcurrentHashMap<>();
        }

        @Override
        public String toString() {
            return "Scope{" +
                    "parent=" + parent +
                    ", variables=" + variables +
                    '}';
        }
    }

    private static class ScopeFindData {
        boolean isFound;
        Scope scope;
    }
}
