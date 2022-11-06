package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ClassValue;
import com.timsystem.runtime.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedClassDeclaration implements Statement {

    private ClassValue result;
    private String name;
    private List<String> argNames;
    private Map<String, Expression> targets;
    private List<String> extension;
    private ArrayList<Statement> childClasses;

    public ExtendedClassDeclaration(ClassValue result, String name, List<String> argNames, Map<String, Expression> targets, List<String> extension, ArrayList<Statement> childClasses) {
        this.result = result;
        this.name = name;
        this.argNames = argNames;
        this.targets = targets;
        this.extension = extension;
        this.childClasses = childClasses;
    }

    @Override
    public void execute() {
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            Value expr = entry.getValue().eval();
            if (expr instanceof FunctionValue) {
                UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                FunctionValue fv = (FunctionValue) expr;
                result.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, result, fv.isPrivate())));
                continue;
            }
            result.setField(entry.getKey(), expr);
        }

        for (Statement ct : childClasses) {
            ClassDeclaration cd = (ClassDeclaration) ct;
            cd.setName(cd.getName() + "_subclass");
            ct.execute();
        }

        Map<Value, Value> temporal = new HashMap<>();
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            temporal.put(new StringValue(entry.getKey()), entry.getValue().eval());
        }
        result.setField("__fields__", new MapValue(temporal));
        for (Statement ct : childClasses) {
            ClassDeclaration cd = (ClassDeclaration) ct;
            result.setField(cd.getName().replace("_subclass", ""), new FunctionValue((crgs) -> {
                return Functions.get(cd.getName()).execute(crgs);
            }));
        }
        temporal = null;
        try {
            for (String ex : extension){
                result.fields.putAll(((ClassValue) Variables.get(ex)).fields);
            }
        } catch (ClassCastException ex) {
            throw new SPKException("StructExtensionException", ex.getMessage());
        }

        Functions.set(name, (args) -> {
            Arguments.check(result.getArgsCount(), args.length);
            ClassValue res = new ClassValue(name, argNames);
            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                res.setField(entry.getKey(), entry.getValue().eval());
            }

            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                Value expr = entry.getValue().eval();
                if (expr instanceof FunctionValue) {
                    UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                    FunctionValue fv = (FunctionValue) expr;
                    res.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, res, fv.isPrivate())));
                    continue;
                }
                res.setField(entry.getKey(), expr);
            }
            for (Statement ct : childClasses) {
                ClassDeclaration cd = (ClassDeclaration) ct;
                res.setField(cd.getName().replace("_subclass", ""), new FunctionValue((crgs) -> {
                    return Functions.get(cd.getName()).execute(crgs);
                }));
            }
            for (int index = 0; index < res.getArgsCount(); index++) {
                String arg = res.getArg(index);
                Value expr = args[index];
                res.setField(arg, expr);
            }

            return res;
        });
        Variables.set(name, result);
    }
}
