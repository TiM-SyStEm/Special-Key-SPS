package com.timsystem.runtime;

import com.timsystem.ast.ReturnStatement;
import com.timsystem.ast.Statement;
import com.timsystem.lib.Function;

import java.util.List;

public class UserDefinedFunction implements Function {
    private final List<String> argNames;
    private final Statement body;

    public UserDefinedFunction(List<String> argNames, Statement body) {
        this.argNames = argNames;
        this.body = body;
    }
    public int getArgsCount(){
        return argNames.size();
    }
    public String getArgName(int index){
        if(index < 0 || index >= getArgsCount()) return "";
        return argNames.get(index);
    }
    @Override
    public Value execute(Value... args) {
        try{
            body.execute();
            return NumberValue.ZERO;
        }
        catch (ReturnStatement rs){
            return rs.getReturnValue();
        }
    }
}