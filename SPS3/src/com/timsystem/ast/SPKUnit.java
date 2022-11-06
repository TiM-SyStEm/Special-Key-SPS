package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.log;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SPKUnit {
    private static long curTimer;
    private static int timersCounter = 0;
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
        spkunit.put("assertNotEquals", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            String res = "";
            boolean eq = args[0].equals(args[1]);
            try {
                if (!eq) res = "✔";
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
        spkunit.put("getLog", new FunctionValue((args -> {
            try (FileReader reader = new FileReader("log.txt")) {
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
        })));
        spkunit.put("clearLog", new FunctionValue((args -> {
            try (FileWriter writer = new FileWriter("log.txt", false)) {
                String text = "";
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.MINUS_ONE;
        })));
        spkunit.put("startTimer", new FunctionValue((args -> {
            curTimer = System.nanoTime();
            return NumberValue.MINUS_ONE;
        })));
        spkunit.put("stopTimer", new FunctionValue((args -> {
            try {
                int time = ((int) (System.nanoTime() - curTimer));

                log.append(String.format("UnitTest timer %d: %d nanoseconds", timersCounter, time));
            }
            catch (NullPointerException ex){
                throw new SPKException("UnboundException", "Unbound timer");
            }
            catch (Exception ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            timersCounter++;
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
