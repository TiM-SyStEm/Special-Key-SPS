package com.timsystem.ast;

import com.timsystem.lib.*;
import com.timsystem.runtime.*;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalExpression implements Expression, Statement {

    private static int indent;
    public final Expression functionExpr;
    public final List<Expression> arguments;

    public FunctionalExpression(Expression functionExpr) {
        this.functionExpr = functionExpr;
        arguments = new ArrayList<>();
    }

    public FunctionalExpression(Expression functionExpr, List<Expression> arguments) {
        this.functionExpr = functionExpr;
        this.arguments = arguments;
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
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }
        final Function f = consumeFunction(functionExpr);
        CallStack.enter(functionExpr.toString(), f);
        try {
            final Value result = f.execute(values);
            CallStack.exit();
            return result;
        } catch (SPKException ex) {
            if (indent == 0) System.out.println("See more information in call stack!\n");
            indent++;
            StringBuilder indentStr = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                indentStr.append("   ");
            }
            throw new SPKException("InFunctionException", ex.getType() + " -> \n" + indentStr + "" +  ex.getText());
        }
    }

    private Function consumeFunction(Expression expr) {
        try {
            final Value value = expr.eval();
            if (value instanceof Function) {
                return ((FunctionValue) value).getValue();
            }
            return getFunction(value.toString());
        } catch (UnboundVariableException ex) {
            return getFunction(ex.getVariable());
        }
    }

    private Function getFunction(String key) {
        if (Functions.isExists(key)) {
            return Functions.get(key);
        }
        if (Variables.isExists(key)) {
            final Value variable = Variables.get(key);
            if (variable instanceof Function) {
                return ((FunctionValue)variable).getValue();
            }
        }
        throw new SPKException("UnboundFunctionException", "Unbound function '" + key + "'");
    }

    @Override
    public String toString() {
        return functionExpr + "(" + arguments + ")";
    }
}