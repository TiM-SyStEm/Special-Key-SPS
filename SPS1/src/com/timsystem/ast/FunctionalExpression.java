package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionalExpression implements Expression{
    private final String name;
    private final List<Expression> arguments;

    public FunctionalExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
    public FunctionalExpression(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }
    public void addArgument(Expression arg){
        arguments.add(arg);
    }
    @Override
    public Value eval() {
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }

        final Function function = getFunction(name);
        if (function instanceof UserDefinedFunction) {
            final UserDefinedFunction userFunction = (UserDefinedFunction) function;
            Arguments.check(userFunction.getArgsCount(), size);

            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArgName(i), values[i]);
            }
            final Value result = userFunction.execute(values);
            Variables.pop();
            return result;
        }
        return function.execute(values);
    }

    private Function getFunction(String key) {
        if (Functions.isExists(key)) return Functions.get(key);
        if (Variables.isExists(key)) {
            final Value value = Variables.get(key);
            if (value instanceof FunctionValue) return ((FunctionValue)value).getValue();
        }
        throw new RuntimeException("Unknown function " + key);
    }

    @Override
    public String toString() {
        return name + "(" + arguments.toString() + ")";
    }
}
