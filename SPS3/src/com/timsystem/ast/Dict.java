package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dict {
    public static void inject() {
        Map<String, Value> dict = new HashMap<>();
        dict.put("getValue", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            DictValue dict1 = (DictValue) args[0];
            Value val = dict1.getValue(args[1]);
            if(val instanceof NumberValue){
                return new NumberValue(val.asInt());
            }
            else if(val instanceof StringValue){
                return new StringValue(val.raw().toString());
            }
            else if(val instanceof ArrayValue){
                return new ArrayValue((ArrayValue) val);
            }
            return NumberValue.MINUS_ONE;
        })));
        dict.put("getKey", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            DictValue dict1 = (DictValue) args[0];
            Value val = dict1.getKey(args[1]);
            if(val instanceof NumberValue){
                return new NumberValue(val.asInt());
            }
            else if(val instanceof StringValue){
                return new StringValue(val.raw().toString());
            }
            else if(val instanceof ArrayValue){
                return new ArrayValue((ArrayValue) val);
            }
            return NumberValue.MINUS_ONE;
        })));
        dict.put("clear", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return ((DictValue) args[0]).clear();
        })));
        dict.put("keys", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new ArrayValue(((DictValue)args[0]).keys());
        })));
        dict.put("values", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new ArrayValue(((DictValue)args[0]).values());
        })));
        dict.put("copy", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return ((DictValue)args[0]).copy();
        })));
        dict.put("update", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            return ((DictValue)args[0]).update(args[1], args[2]);
        })));
        dict.put("remove", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            return ((DictValue)args[0]).remove(args[1]);
        })));
        dict.put("isEmpty", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return ((DictValue)args[0]).isEmpty();
        })));
        newClass("dict", new ArrayList<>(), dict);
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
