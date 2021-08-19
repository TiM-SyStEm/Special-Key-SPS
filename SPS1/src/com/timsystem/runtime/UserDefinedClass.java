package com.timsystem.runtime;

import com.timsystem.ast.ReturnStatement;
import com.timsystem.ast.Statement;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKClass;

import java.util.List;

public class UserDefinedClass implements SPKClass {
    private final Statement body;

    public UserDefinedClass(Statement body) {
        this.body = body;
    }
    @Override
    public Value execute(Value... args) {
        body.execute();
        return NumberValue.ZERO;
    }
}
