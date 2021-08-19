package com.timsystem.ast;

import com.timsystem.lib.TokenType;
import com.timsystem.runtime.Classes;
import com.timsystem.runtime.UserDefinedClass;

public class ClassStatement implements Statement{
    private final String name;
    private final Statement body;

    public ClassStatement(String name, Statement body) {
        this.name = name;
        this.body = body;
    }

    @Override
    public void execute() {
        Classes.set(name, new UserDefinedClass(body));
    }
}
