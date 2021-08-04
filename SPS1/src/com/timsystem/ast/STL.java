package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Variables;

import java.nio.charset.StandardCharsets;

import static com.timsystem.Main.getVer;

public class STL {
    public static void inject() {
        Variables.set("PI", NumberValue.of(Math.PI));
        Variables.set("E", NumberValue.of(Math.E));
        Variables.set("__ver__", new StringValue(("Special Key " + getVer()).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__about__", new StringValue(("SPK is dynamic a interpreted programming language built on Java Virtual Machine\nCreated by Timofey Gorlov in Russia with his team.").getBytes(StandardCharsets.UTF_8)));
    }
}
