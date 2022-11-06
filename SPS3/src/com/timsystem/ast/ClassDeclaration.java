package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.Handler;
import com.timsystem.runtime.ClassValue;
import com.timsystem.runtime.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDeclaration implements Statement {

    private ClassValue result;
    private String name;
    private List<String> argNames;
    private Map<String, Expression> targets;

    private ArrayList<Statement> childClasses;

    public ClassDeclaration(ClassValue result, String name, List<String> argNames, Map<String, Expression> targets, ArrayList<Statement> childClasses) {
        this.result = result;
        this.name = name;
        this.argNames = argNames;
        this.targets = targets;
        this.childClasses = childClasses;
    }
    @Override
    public void execute() {
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            Value expr = entry.getValue().eval();
            if (expr instanceof FunctionValue) {
                UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                FunctionValue fv = (FunctionValue) expr;
                result.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, result, fv.isPrivate(), fv.isProtected())));
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
        temporal = null;
        for (Statement ct : childClasses) {
            ClassDeclaration cd = (ClassDeclaration) ct;
            result.setField(cd.getName().replace("_subclass", ""), new FunctionValue((crgs) -> {
                return Functions.get(cd.getName()).execute(crgs);
            }));
        }
        Functions.set(name, (args) -> {
            Arguments.check(result.getArgsCount(), args.length);
            ClassValue res;
            if(!Handler.instencesed.contains(name)) {
                res = new ClassValue(name, argNames, true);
            }
            else res = new ClassValue(name, argNames);
            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                res.setField(entry.getKey(), entry.getValue().eval());
            }

            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                Value expr = entry.getValue().eval();
                if (expr instanceof FunctionValue) {
                    UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                    FunctionValue fv = (FunctionValue) expr;
                    res.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, res, fv.isPrivate(), fv.isProtected())));
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

    public ClassValue getResult() {
        return result;
    }

    public void setResult(ClassValue result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArgNames() {
        return argNames;
    }

    public void setArgNames(List<String> argNames) {
        this.argNames = argNames;
    }

    public Map<String, Expression> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, Expression> targets) {
        this.targets = targets;
    }

    public ArrayList<Statement> getChildClasses() {
        return childClasses;
    }

    public void setChildClasses(ArrayList<Statement> childClasses) {
        this.childClasses = childClasses;
    }
}
