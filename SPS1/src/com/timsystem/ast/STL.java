package com.timsystem.ast;

import com.sun.jdi.connect.Connector;
import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.timsystem.Main.getVer;

public class STL {
    public static void inject() {
        Variables.set("PI", NumberValue.of(Math.PI));
        Variables.set("E", NumberValue.of(Math.E));
        Variables.set("__ver__", new StringValue(("Special Key " + getVer()).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__about__", new StringValue(("SPK is dynamic a interpreted programming language built on Java Virtual Machine\nCreated by Timofey Gorlov in Russia with his team").getBytes(StandardCharsets.UTF_8)));
        Functions.functions.put("sin", args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.sin(args[0].asNumber()));
        });
        Functions.functions.put("cos", (Value... args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.cos(args[0].asNumber()));
        });
        Functions.functions.put("tan", (Value... args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.tan(args[0].asNumber()));
        });
        Functions.functions.put("sqrt", (Value... args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        });
        Functions.functions.put("cbrt", (Value... args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        });
        Functions.functions.put("round", (Value... args) -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.round(args[0].asNumber()));
        });
        Functions.functions.put("random", (Value... args) -> {
            Arguments.check(2, args.length);
            return new NumberValue(args[0].asNumber() + (int) (Math.random() * args[1].asNumber()));
        });
        Functions.functions.put("sleep", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                Thread.sleep((long) args[0].asNumber());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return NumberValue.ZERO;
        });

        Functions.functions.put("asString", (args) -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().getBytes(StandardCharsets.UTF_8));
        });
    
