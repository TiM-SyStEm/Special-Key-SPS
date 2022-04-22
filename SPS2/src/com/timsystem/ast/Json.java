package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {
    public static void inject() {
        Map<String, Value> json = new HashMap<>();
        json.put("get", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            String retstr;
            try{
                Object obj = new JSONParser().parse(args[0].raw().toString());
                JSONObject jo = (JSONObject) obj;
                retstr = jo.get(args[1].raw().toString()).toString();
            }
            catch (org.json.simple.parser.ParseException ex) {throw new SPKException("JsonParseError", "cannot parse json file");}
            return new StringValue(retstr);
        })));
        json.put("write", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            JSONObject obj = new JSONObject();
            Value[] v = ((ArrayValue) args[0]).array();

            for(Value i : v){
                String[] str = i.raw().toString().split(":");
                obj.put(str[0], str[1]);
            }
            try (FileWriter file = new FileWriter(args[1].raw().toString())) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                throw new SPKException("FileWriteError", "can't write json object to file");
            }
            return NumberValue.MINUS_ONE;
        })));
        newClass("json", new ArrayList<>(), json);
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
