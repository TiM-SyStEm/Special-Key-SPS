package com.timsystem.ast;

import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Variables;

import java.awt.event.KeyEvent;

public class MouseButtons {
    public static void inject() {
        Variables.set("MS_RIGHT", new NumberValue(0));
        Variables.set("MS_LEFT", new NumberValue(1));
        Variables.set("MS_MIDLE", new NumberValue(2));
    }
}
