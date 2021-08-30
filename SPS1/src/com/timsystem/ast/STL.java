package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.timsystem.Main.getVer;

public class STL {
    public static void inject() {
        Variables.set("PI", NumberValue.of(Math.PI));
        Variables.set("E", NumberValue.of(Math.E));
        Variables.set("__ver__", new StringValue(("Special Key " + getVer()).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__about__", new StringValue(("SPK is dynamic a interpreted programming language built on Java Virtual Machine\nCreated by Timofey Gorlov in Russia with his team").getBytes(StandardCharsets.UTF_8)));
        Functions.set("typeof", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0] instanceof NumberValue) {
                try {
                    args[0].asInt();
                    return new StringValue("Int");
                } catch (Exception exception2) {
                    return new StringValue("Float");
                }
            } else if (args[0] instanceof StringValue) {
                return new StringValue("String");
            } else return new StringValue("UnknownType");
        });
        Functions.set("sleep", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                Thread.sleep((long) args[0].asNumber());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return NumberValue.ZERO;
        });
        Functions.set("downloadWithURL", (Value... args) -> {
            Arguments.check(2, args.length);
            try {
                URL website = new URL(args[0].raw().toString());
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(args[1].raw().toString());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.ZERO;
        });
        initFileClass();
        initMathClass();
    }

    private static void initFileClass() {
        Map<String, Value> file = new HashMap<>();
        file.put("read", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            try (FileReader reader = new FileReader(args[0].raw().toString())) {
                // читаем посимвольно
                int c;
                StringBuilder all = new StringBuilder();
                while ((c = reader.read()) != -1) {
                    all.append((char) c);
                }
                return new StringValue(all.toString());
            } catch (IOException ex) {
                throw new SPKException("FileReadError", "the file cannot be read");
            }
        }));
        file.put("write", new FunctionValue(args ->{
            Arguments.check(2, args.length);
            try (FileWriter writer = new FileWriter(args[0].raw().toString(), false)) {
                String text = args[1].raw().toString();
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.ZERO;
        }));
        file.put("append", new FunctionValue(args ->{
            Arguments.check(2, args.length);
            try (FileWriter writer = new FileWriter(args[0].raw().toString(), true)) {
                String text = args[1].raw().toString();
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.ZERO;
        }));

        newClass("File", new ArrayList<>(), file);
    }

    private static void initMathClass() {
        Map<String, Value> math = new HashMap<>();

        math.put("sin", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.sin(args[0].asNumber()));
        }));
        math.put("cos", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.cos(args[0].asNumber()));
        }));
        math.put("tan",  new FunctionValue(args ->{
            Arguments.check(1, args.length);
            return new NumberValue(Math.tan(args[0].asNumber()));
        }));
        math.put("sqrt", new FunctionValue(args ->{
            Arguments.check(1, args.length);
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        }));
        math.put("cbrt", new FunctionValue(args ->{
            Arguments.check(1, args.length);
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        }));
        math.put("round", new FunctionValue(args ->{
            Arguments.check(1, args.length);
            return new NumberValue(Math.round(args[0].asNumber()));
        }));
        math.put("random", new FunctionValue(args ->{
            Arguments.check(2, args.length);
            return new NumberValue(args[0].asNumber() + (int) (Math.random() * args[1].asNumber()));
        }));

        newClass("Math", new ArrayList<>(), math);
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