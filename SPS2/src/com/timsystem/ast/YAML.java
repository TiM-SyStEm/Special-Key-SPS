package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YAML {

    private static Yaml yaml = new Yaml();

    public void inject() {
        HashMap<String, Value> yamlClass = new HashMap<>();

        yamlClass.put("load", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            HashMap<String, Object> yamlResult = yaml.load(readFile(args[0].toString()));
            HashMap<String, String> temp = new HashMap<>();
            for (Map.Entry<String, Object> entry : yamlResult.entrySet()) {
                temp.put(entry.getKey(), entry.getValue().toString());
            }

            return new YAMLValue(yamlResult, temp);
        }));

        yamlClass.put("get", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            YAMLValue value = (YAMLValue) args[0];
            return new StringValue(value.get(args[1].toString()));
        }));

        yamlClass.put("set", new FunctionValue(args -> {
            Arguments.check(3, args.length);
            YAMLValue value = (YAMLValue) args[0];
            value.put(args[1].toString(), args[2].toString());
            value.complexPut(args[1].toString(), args[2].raw());
            return value;
        }));

        yamlClass.put("getNested", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            YAMLValue value = (YAMLValue) args[0];
            ArrayList<HashMap<String, Object>> nested = (ArrayList<HashMap<String, Object>>) value.complexGet(args[1].toString());
            HashMap<String, Object> nestedComplex = new HashMap<>();
            HashMap<String, String> temp = new HashMap<>();
            for (HashMap<String, Object> map : nested) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    nestedComplex.put(entry.getKey(), entry.getValue());
                    temp.put(entry.getKey(), entry.getValue().toString());
                }
            }
            return new YAMLValue(nestedComplex, temp);
        }));

        yamlClass.put("write", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            YAMLValue value = (YAMLValue) args[0];
            Map<String, Object> file = value.getComplex();
            StringWriter writer = new StringWriter();
            yaml.dump(file, writer);
            try (FileWriter f = new FileWriter(args[1].toString(), false)) {
                f.write(writer.getBuffer().toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return new StringValue(writer.toString());
        }));

        yamlClass.put("bake", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            YAMLValue value = (YAMLValue) args[0];
            Map<String, Object> file = value.getComplex();
            StringWriter writer = new StringWriter();
            yaml.dump(file, writer);

            return new StringValue(writer.toString());
        }));

        newClass("yaml", new ArrayList<>(), yamlClass);
    }

    public String readFile(String file) {

        Path path = null;
        path = Paths.get(file);

        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
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
