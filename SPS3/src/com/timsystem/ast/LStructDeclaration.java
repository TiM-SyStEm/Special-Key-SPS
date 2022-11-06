package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Handler;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LStructDeclaration  implements Statement {

    private LStructValue result;
    private String name;
    private Map<String, Expression> targets;


    public LStructDeclaration(LStructValue result, String name, Map<String, Expression> targets) {
        this.result = result;
        this.name = name;
        this.targets = targets;
    }
    @Override
    public void execute() {
        for (Map.Entry<String, Expression> entry : targets.entrySet()) {
            Value expr = entry.getValue().eval();
            if (expr instanceof FunctionValue) {
                UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                FunctionValue fv = (FunctionValue) expr;
                result.setField(entry.getKey(), new FunctionValue(new LStructMethod(name, fun.arguments, fun.body, result, fv.isPrivate())));
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
            LStructValue res = new LStructValue(name);
            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                res.setField(entry.getKey(), entry.getValue().eval());
            }

            for (Map.Entry<String, Expression> entry : targets.entrySet()) {
                Value expr = entry.getValue().eval();
                if (expr instanceof FunctionValue) {
                    UserDefinedFunction fun = (UserDefinedFunction) ((FunctionValue) expr).getValue();
                    FunctionValue fv = (FunctionValue) expr;
                    res.setField(entry.getKey(), new FunctionValue(new LStructMethod(name, fun.arguments, fun.body, result, fv.isPrivate())));
                    continue;
                }
                res.setField(entry.getKey(), expr);
            }
            return res;
        });
        Variables.set(name, result);
    }

    public LStructValue getResult() {
        return result;
    }

    public void setResult(LStructValue result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Map<String, Expression> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, Expression> targets) {
        this.targets = targets;
    }
}
