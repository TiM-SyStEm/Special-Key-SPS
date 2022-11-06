package com.timsystem.lib;

import com.timsystem.log;

public class SPKException extends RuntimeException {
    private final String type;
    private final String text;

    public SPKException(String type, String text) {
        super();
        this.type = type;
        this.text = "\u001b[31m" + text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
