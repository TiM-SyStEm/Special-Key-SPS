package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Variables;

public final class TryCatchStatement implements Statement{
    private final Statement tryStatement, catchStatement;

    public TryCatchStatement(Statement tryStatement, Statement catchStatement) {
        this.tryStatement = tryStatement;
        this.catchStatement = catchStatement;
    }

    @Override
    public void execute() {
        try{
            tryStatement.execute();
        }
        catch (SPKException ex){
            Variables.define("ex_type", new StringValue(ex.getType()));
            Variables.define("ex_text", new StringValue(ex.getText()));
            catchStatement.execute();
        }
    }
}
