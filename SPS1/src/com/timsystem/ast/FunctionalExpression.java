package com.timsystem.ast;

import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.Functions;
import com.timsystem.runtime.UserDefinedFunction;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

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

        final Function function = Functions.get(name);
        if (function instanceof UserDefinedFunction) {
            final UserDefinedFunction userFunction = (UserDefinedFunction) function;
            if (size != userFunction.getArgsCount()) throw new SPKException("SyntaxError", "Args count mismatch");

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
    @Override
    public String toString() {
        return name + "(" + arguments.toString() + ")";
    }
}
