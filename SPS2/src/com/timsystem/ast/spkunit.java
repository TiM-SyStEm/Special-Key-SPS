package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.log;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import java.util.*;

public class spkunit {
    public static void inject() {
        Map<String, Value> spkunit = new HashMap<>();
        spkunit.put("assertEquals", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            String res = "";
            boolean eq = args[0].equals(args[1]);
            try {
                if (eq) res = "✔";
                else res = "✖";
                log.append(String.format("UnitTest Result: %s [%s]", res, args[2].toString()));
            }
            catch (Exception ex2){}
            return new NumberValue(eq);
        })));
        spkunit.put("assertNotNull", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            String res = "";
            boolean eq = args[0] != NumberValue.MINUS_ONE;
            try {
                if (eq) res = "✔";
                else res = "✖";
                log.append(String.format("UnitTest Result: %s [%s]", res, args[1].toString()));
            }
            catch (Exception ignored){}
            return new NumberValue(eq);
        })));
        spkunit.put("assertNull", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            String res = "";
            boolean eq = args[0] == NumberValue.MINUS_ONE;
            try {
                if (eq) res = "✔";
                else res = "✖";
                log.append(String.format("UnitTest Result: %s [%s]", res, args[1].toString()));
            }
            catch (Exception ignored){}
            return new NumberValue(eq);
        })));
        spkunit.put("assertTrue", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            String res = "";
            boolean eq = args[0] == NumberValue.ONE;
            try {
                if (eq) res = "✔";
                else res = "✖";
                log.append(String.format("UnitTest Result: %s [%s]", res, args[1].toString()));
            }
            catch (Exception ignored){}
            return new NumberValue(eq);
        })));
        spkunit.put("point", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            try {
                log.append(String.format("UnitTest point: ✔ [%s]", args[0].toString()));
            }
            catch (Exception ignored){}
            return NumberValue.MINUS_ONE;
        })));
        newClass("spkunit", new ArrayList<>(), spkunit);
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
