package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Async {
    public static void inject() {
        Map<String, Value> async = new HashMap<>();
        async.put("supply", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            CompletableFuture<NumberValue> new_async;
            new_async = CompletableFuture.supplyAsync(() -> {
                (((FunctionValue) args[0]).getValue()).execute();
                return NumberValue.ONE;
            });
            return NumberValue.MINUS_ONE;
        })));
        newClass("async", new ArrayList<>(), async);
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
