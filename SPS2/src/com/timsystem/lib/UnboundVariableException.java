package com.timsystem.lib;

public class UnboundVariableException extends SPKException {
    String variable;
    public UnboundVariableException(String type, String text, String variable) {
        super(type, text);
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
