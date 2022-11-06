package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadSPK {
    private static List<Thread> threads = new ArrayList<>();
    static class SomeThread implements Runnable
    {
        private Value value;
        public SomeThread(Value value){
            this.value = value;
        }
        public void run()
        {
            (((FunctionValue) value).getValue()).execute();
        }
    }
    public static void inject() {
        Map<String, Value> thread = new HashMap<>();
        thread.put("start", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            Thread new_thread = new Thread(new SomeThread(args[0]));
            new_thread.start();
            new_thread.setName(((UserDefinedFunction) (((FunctionValue) args[0]).getValue())).getNameOfFunction());
            threads.add(new_thread);
            return new NumberValue(threads.size()-1);
        })));
        thread.put("getName", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            Thread cur_thread = threads.get(args[0].asInt());
            return new StringValue(cur_thread.getName());
        })));
        thread.put("setName", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            Thread cur_thread = threads.get(args[0].asInt());
            cur_thread.setName(args[1].raw().toString());
            return NumberValue.MINUS_ONE;
        })));
        thread.put("getPriority", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            Thread cur_thread = threads.get(args[0].asInt());
            return new NumberValue(cur_thread.getPriority());
        })));
        thread.put("setPriority", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            Thread cur_thread = threads.get(args[0].asInt());
            cur_thread.setPriority(args[1].asInt());
            return NumberValue.MINUS_ONE;
        })));
        thread.put("threadsUnbind", new FunctionValue((args -> {
            threads.clear();
            return NumberValue.MINUS_ONE;
        })));
        newClass("thread", new ArrayList<>(), thread);
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
