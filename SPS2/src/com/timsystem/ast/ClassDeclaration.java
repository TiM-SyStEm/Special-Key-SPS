package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.runtime.ClassValue;
import com.timsystem.runtime.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDeclaration implements Statement {

    private ClassValue result;
    private String name;
    private List<String> argNames;
    private Map<String, Expression> targets;

    public ClassDeclaration(ClassValue result, String name, List<String> argNames, Map<String, Expression> targets) {
        this.result = result;
        this.name = name;
        this.argNames = argNames;
        this.targets = targets;
    }

    @Override
    public void execute() {
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            Value expr = entry.getValue().eval();
            if (expr instanceof FunctionValue) {
                UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                result.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, result)));
                continue;
            }
            result.setField(entry.getKey(), expr);
        }

        Map<Value, Value> temporal = new HashMap<>();
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            temporal.put(new StringValue(entry.getKey()), entry.getValue().eval());
        }
        result.setField("__fields__", new MapValue(temporal));
        temporal = null;

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
                    res.setField(entry.getKey(), new FunctionValue(new ClassMethod(name, fun.arguments, fun.body, res)));
                    continue;
                }
                res.setField(entry.getKey(), expr);
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
