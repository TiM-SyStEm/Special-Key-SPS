package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

public final class AssignmentStatement implements Statement {

    private final String variable;
    private final Expression expression;
    private final int mode;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
        this.mode = 0;
    }
    public AssignmentStatement(String variable, Expression expression, int mode) {
        this.variable = variable;
        this.expression = expression;
        this.mode = mode;
    }

    @Override
    public void execute() {
        if (mode == 0){
            final Value result = expression.eval();
            Variables.set(variable, result);
        }
        else {
            try{
                // Is number
                Integer.parseInt(Variables.get(variable).asString());
                // =========
                if (mode == 1){
                    final Value result = expression.eval();
                    Variables.set(variable, NumberValue.of(Variables.get(variable).asInt() + result.asInt()));
                }
                else if(mode == 2){
                    final Value result = expression.eval();
                    Variables.set(variable, NumberValue.of(Variables.get(variable).asInt() - result.asInt()));
                }
                else if(mode == 3){
                    final Value result = expression.eval();
                    Variables.set(variable, NumberValue.of(Variables.get(variable).asInt() * result.asInt()));
                }
                else if(mode == 4){
                    final Value result = expression.eval();
                    Variables.set(variable, NumberValue.of(Variables.get(variable).asInt() / result.asInt()));
                }
            }
            catch (Exception ex){
                if (mode == 1){
                    final Value result = expression.eval();
                    Variables.set(variable, new StringValue(Variables.get(variable).toString() + result.toString()));
                }
                else{
                    throw new SPKException("TypeError", "non-applicable operation to string");
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("var %s = %s", variable, expression);
    }

    public String getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getMode() {
        return mode;
    }
}
