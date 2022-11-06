package com.timsystem.ast;

import com.timsystem.runtime.Functions;
import com.timsystem.runtime.UserDefinedFunction;

import java.util.List;

public class FunctionalDefineStatement implements Statement {
    private final String name;
    private final List<String> argNames;
    private final Statement body;

    public FunctionalDefineStatement(String name, List<String> argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new UserDefinedFunction(name, argNames, body));
    }

    @Override
    public String toString() {
        return "fun (" + argNames.toString() + ") " + body.toString();
    }
}
