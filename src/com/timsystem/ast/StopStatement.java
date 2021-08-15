package com.timsystem.ast;

import com.timsystem.lib.SPKException;

public class StopStatement extends RuntimeException implements Statement{
    @Override
    public void execute() {
        throw this;
    }
    @Override
    public String toString() {
        return "stop";
    }
}
