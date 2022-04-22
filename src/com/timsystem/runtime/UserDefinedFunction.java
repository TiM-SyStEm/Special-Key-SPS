package com.timsystem.runtime;

import com.timsystem.ast.ReturnStatement;
import com.timsystem.ast.Statement;
import com.timsystem.lib.*;

import java.util.ArrayList;
import java.util.List;

public class UserDefinedFunction implements Function {

    public final List<String> arguments;
    public final Statement body;
    private String name;

    public UserDefinedFunction(String name, List<String> arguments, Statement body) {
        this.name = name;
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
        Specification spec = Specifications.get(name);
        Arguments.check(arguments.size(), values.length);
        if (spec != null) Arguments.check(arguments.size(), spec.getTypes().size());

        try {
            Variables.push();
            for (int i = 0; i < size; i++) {
                Value v = values[i];
                if (spec != null) {
                    if (!specEquals(v, spec.getTypes().get(i)))
                        throw new SPKException("Specification Error", "Expected " + spec + ", got " + specify(values));
                }
                Variables.define(getArgsName(i), v);
            }
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            return rt.getReturnValue();
        } finally {
            Variables.pop();
        }
    }

    private boolean specEquals(Value value, SpecificationType type) {
        SpecificationType newType = typify(value);
        return newType == type;
    }

    private SpecificationType typify(Value value) {
        SpecificationType newType = null;
        if (value instanceof NumberValue) newType = SpecificationType.NUMBER;
        if (value instanceof StringValue) newType = SpecificationType.STRING;
        if (value instanceof ArrayValue) newType = SpecificationType.ARRAY;
        if (value instanceof FunctionValue) newType = SpecificationType.FUNCTION;
        if (value instanceof ClassValue) newType = SpecificationType.CLASS;

        return newType;
    }

    private ArrayList<SpecificationType> specify(Value[] values) {
        ArrayList<SpecificationType> specs = new ArrayList<>();
        for (Value value : values) {
            specs.add(typify(value));
        }
        return specs;
    }

    @Override
    public String toString() {
        if (body instanceof ReturnStatement) {
            return String.format("def%s = %s", arguments, ((ReturnStatement)body).getReturnValue());
        }
        return String.format("def%s %s", arguments, body);
    }
}