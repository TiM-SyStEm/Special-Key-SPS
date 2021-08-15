package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Variables;

public class WSGcolors {
    public static void inject(){
        Variables.set("RED", new NumberValue(16711688));
        Variables.set("GREEN", new NumberValue(65309));
        Variables.set("BLUE", new NumberValue(5887));
        Variables.set("WHITE", new NumberValue(16777215));
        Variables.set("BLACK", new NumberValue(0));
        Variables.set("PURPLE", new NumberValue(9109759));
        Variables.set("PINK", new NumberValue(16761037));
        Variables.set("YELLOW", new NumberValue(16776960));
    }
}
