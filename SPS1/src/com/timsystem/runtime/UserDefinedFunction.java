package com.timsystem.runtime;

import com.timsystem.ast.ReturnStatement;
import com.timsystem.ast.Statement;
import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;

import java.util.ArrayList;
import java.util.List;

public class UserDefinedFunction implements Function {

    public final List<String> arguments;
    public final Statement body;

    public UserDefinedFunction(List<String> arguments, Statement body) {
        this.arguments = arguments;
        this.body = body;
    }

    public int getArgsCount() {
        return arguments.size();
    }

    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) return "";
        return arguments.get(index);
    }

    @Override
    public Value execute(Value... values) {
        final int size = values.length;
        Arguments.check(arguments.size(), values.length);

        try {
            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.define(getArgsName(i), values[i]);
            }
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            return rt.getReturnValue();
        } finally {
            Variables.pop();
        }
    }

    @Override
    public String toString() {
        if (body instanceof ReturnStatement) {
            return String.format("def%s = %s", arguments, ((ReturnStatement)body).getReturnValue());
        }
        return String.format("def%s %s", arguments, body);
    }
}