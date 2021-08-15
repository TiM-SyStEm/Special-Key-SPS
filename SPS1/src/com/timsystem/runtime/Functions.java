package com.timsystem.runtime;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Functions {
    public static final Map<String, Function> functions;

    static {
        functions = new HashMap<>();
        functions.put("length", (Value... args) -> {
            Arguments.check(1, args.length);
            if(args[0] instanceof ArrayValue){
                return new NumberValue(((ArrayValue) args[0]).length());
            }
            else if(args[0] instanceof StringValue){
                return new NumberValue(args[0].toString().length());
            }
            throw new SPKException("TypeError", "Array or String expected");
        });
        Functions.functions.put("Array", (args) -> {
            if (args.length == 0) return new ArrayValue(new Value[] {});
            ArrayValue result = new ArrayValue(new Value[] {});
            for (Value x : args) {
                result.append(x);
            }
            return result;
        });
        Functions.functions.put("toStr", (args) -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().getBytes(StandardCharsets.UTF_8));
        });
        Functions.functions.put("toInt", (args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(args[0].asInt());
        });
        Functions.functions.put("toFloat", (args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(args[0].asNumber());
        });
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
}
