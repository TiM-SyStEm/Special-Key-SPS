package com.timsystem.lib;

public class SPKException extends RuntimeException {
    private final String type;
    private final String text;

    public SPKException(String type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
