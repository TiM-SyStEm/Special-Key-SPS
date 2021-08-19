package com.timsystem.ast;

import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.FunctionValue;
import com.timsystem.runtime.Functions;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalExpression implements Expression, Statement {

    public final Expression functionExpr;
    public final List<Expression> arguments;

    public FunctionalExpression(Expression functionExpr) {
        this.functionExpr = functionExpr;
        arguments = new ArrayList<>();
    }

    public void addArgument(Expression arg) {
        arguments.add(arg);
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        System.out.println("func expr - " + functionExpr.getClass().getSimpleName());
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }
        final Function f = consumeFunction(functionExpr);
        try {
            final Value result = f.execute(values);
            return result;
        } catch (SPKException ex) {
            throw new SPKException(ex.getType(), ex.getText() + " in " + functionExpr);
        }
    }

    private Function consumeFunction(Expression expr) {
        return getFunction(expr.toString());
    }

    private Function getFunction(String key) {
        if (Functions.isExists(key)) {
            return Functions.get(key);
        }
        if (Variables.isExists(key)) {
            final Value variable = Variables.get(key);
            if (variable instanceof Function) {
                return ((FunctionValue) variable).getValue();
            }
        }
        throw new SPKException("UnboundFunctionException", "Unbound function '" + key + "'");
    }

}
