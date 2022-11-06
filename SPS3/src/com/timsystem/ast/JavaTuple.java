package com.timsystem.ast;

import javax.swing.*;

public class JavaTuple {
    private final String key;
    private final javax.swing.JComponent value;

    public JavaTuple(String key, javax.swing.JComponent value) {
        super();
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }

    public JComponent getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", key, value.toString());
    }
}
