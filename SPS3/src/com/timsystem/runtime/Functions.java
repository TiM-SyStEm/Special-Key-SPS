package com.timsystem.runtime;

import com.timsystem.ast.Dict;
import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.Handler;
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
        initIO();
    }
    public static void initIO(){
        Map<String, Value> io = new HashMap<>();
        io.put("clear", new FunctionValue( new FunctionValue((args -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            return NumberValue.MINUS_ONE;
        }))));
        newClass("io", new ArrayList<>(), io);
    }
    public static void initFunctions(){
        functions.put("length", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0] instanceof ArrayValue) {
                return new NumberValue(((ArrayValue) args[0]).length());
            } else if (args[0] instanceof StringValue) {
                return new NumberValue(args[0].toString().length());
            }
            else if (args[0] instanceof DictValue) {
                return new NumberValue(((DictValue) args[0]).length());
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
            return new NumberValue(args[0].asNumber());
        });
        Functions.set("toFloat", (args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(args[0].asNumber());
        });
        Functions.set("exec", (args) -> {
            Arguments.check(1, args.length);
            Handler.handle(args[0].toString(), "exec()", true);
            return NumberValue.MINUS_ONE;
        });
        Functions.set("eval", (args) -> {
            Arguments.check(1, args.length);
            return Handler.returnHandle(args[0].toString(), "eval()");
        });
        Functions.set("createVariable", (args) -> {
            Arguments.check(2, args.length);
            Variables.define(args[0].toString(), args[1]);
            return args[1];
        });
        Functions.set("getVariable", (args) -> {
            Arguments.check(1, args.length);
            return Variables.get(args[0].asString());
        });
        Functions.set("destruct", (args) -> {
            Arguments.check(1, args.length);
            Variables.del(args[0].toString());
            return NumberValue.MINUS_ONE;
        });
        Functions.set("argumentsCheck", (args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            switch (args.length){
                case 1:
                    return new NumberValue(((UserDefinedFunction) (((FunctionValue) args[0]).getValue())).getArgsCount());
                case 2:
                    NumberValue n = new NumberValue(((UserDefinedFunction) (((FunctionValue) args[0]).getValue())).getArgsCount());
                    int ni = n.asInt();
                    int expected = args[1].asInt();
                    if(ni == expected)
                        return NumberValue.ONE;
                    return NumberValue.ZERO;
            }
            return NumberValue.MINUS_ONE;
        });
        Functions.set("toByte", args -> NumberValue.of((byte) args[0].asInt()));
        Functions.set("toShort", args -> NumberValue.of((short) args[0].asInt()));
        Functions.set("toLong", args -> NumberValue.of((long) args[0].asNumber()));
        Functions.set("toDouble", args -> NumberValue.of(args[0].asNumber()));
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
