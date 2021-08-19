package com.timsystem.ast;

import com.timsystem.lib.ClassDeclarations;

import java.util.ArrayList;
import java.util.List;

public final class ClassStatement implements Statement {

    public final String name;
    public final List<FunctionalDefineStatement> methods;
    public final List<AssignmentExpression> fields;

    public ClassStatement(String name) {
        this.name = name;
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public void addField(AssignmentExpression expr) {
        fields.add(expr);
    }

    public void addMethod(FunctionalDefineStatement statement) {
        methods.add(statement);
    }

    @Override
    public void execute() {
        ClassDeclarations.set(name, this);
    }

    @Override
    public String toString() {
        return String.format("class %s {\n  %s  %s}", name, fields, methods);
    }
}
