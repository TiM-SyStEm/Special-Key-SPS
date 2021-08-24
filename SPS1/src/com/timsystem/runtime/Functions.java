package com.timsystem.runtime;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Functions {
    public static final Map<String, Function> functions;

    static {
        functions = new HashMap<>();
        initFunctions();
    }

    public static void initFunctions() {
        Map<String, Value> str = new HashMap<>();
        functions.put("length", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0] instanceof ArrayValue) {
                return new NumberValue(((ArrayValue) args[0]).length());
            } else if (args[0] instanceof StringValue) {
                return new NumberValue(args[0].toString().length());
            }
            throw new SPKException("TypeError", "Array or String expected");
        });
        Functions.functions.put("Array", (Value... args) -> {
            if (args.length == 0) return new ArrayValue(new Value[]{});
            ArrayValue result = new ArrayValue(new Value[]{});
            for (Value x : args) {
                result.append(x);
            }
            return result;
        });
        Functions.set("toStr", (args) -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().getBytes(StandardCharsets.UTF_8));
        });
        Functions.set("toInt", (args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(args[0].asInt());
        });
        Functions.set("toFloat", (args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(args[0].asNumber());
        });
        str.put("replace", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            final String input = args[0].toString();
            final String regex = args[1].toString();
            final String replacement = args[2].toString();

            return new StringValue(input.replaceAll(regex, replacement));
        })));
        str.put("split", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            final String input = args[0].asString();
            final String regex = args[1].asString();
            final String replacement = args[2].asString();

            return new StringValue(input.replaceAll(regex, replacement));
        })));
        Functions.set("toByte", args -> NumberValue.of((byte) args[0].asInt()));
        Functions.set("toShort", args -> NumberValue.of((short) args[0].asInt()));
        Functions.set("toLong", args -> NumberValue.of((long) args[0].asNumber()));
        Functions.set("toDouble", args -> NumberValue.of(args[0].asNumber()));
        newClass("str", new ArrayList<>(), str);
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
    private static void newClass(String name, List<String> structArgs, Map<String, Value> targets) {
        ClassValue result = new ClassValue(name, structArgs);
        for (Map.Entry<String, Value> entry : targets.entrySet()) {
            Value expr = entry.getValue();
            result.setField(entry.getKey(), expr);
        }

        Variables.set(name, result);
    }
}
