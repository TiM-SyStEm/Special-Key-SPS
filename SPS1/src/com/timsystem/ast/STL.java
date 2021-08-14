package com.timsystem.ast;

import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;

import java.nio.charset.StandardCharsets;

import static com.timsystem.Main.getVer;

public class STL {
    public static void inject() {
        Variables.set("PI", NumberValue.of(Math.PI));
        Variables.set("E", NumberValue.of(Math.E));
        Variables.set("__ver__", new StringValue(("Special Key " + getVer()).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__about__", new StringValue(("SPK is dynamic a interpreted programming language built on Java Virtual Machine\nCreated by Timofey Gorlov in Russia with his team").getBytes(StandardCharsets.UTF_8)));
        Functions.functions.put("sin", (Function) args -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.sin(args[0].asNumber()));
        });
        Functions.functions.put("cos", (Function) (Value... args) -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.cos(args[0].asNumber()));
        });
        Functions.functions.put("tan", (Function) (Value... args) -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.tan(args[0].asNumber()));
        });
        Functions.functions.put("sqrt", (Function) (Value... args) -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        });
        Functions.functions.put("cbrt", (Function) (Value... args) -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        });
        Functions.functions.put("round", (Function) (Value... args) -> {
            if (args.length != 1) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(Math.round(args[0].asNumber()));
        });
        Functions.functions.put("random", (Function) (Value... args) -> {
            if (args.length != 2) throw new SPKException("ArgumentExpected","One arg expected");
            return new NumberValue(args[0].asNumber() + (int) (Math.random() * args[1].asNumber()));
        });
        Functions.functions.put("Array", (Function) ArrayValue::new);
    }
}
