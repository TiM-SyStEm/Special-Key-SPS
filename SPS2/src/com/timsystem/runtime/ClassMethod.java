package com.timsystem.runtime;

import com.timsystem.ast.Statement;

import java.util.List;

public class ClassMethod extends UserDefinedFunction {

    public final ClassValue classInstance;

    public ClassMethod(String name, List<String> arguments, Statement body, ClassValue classInstance) {
        super(name, arguments, body);
        this.classInstance = classInstance;
    }

    @Override
    public Value execute(Value[] values) {
        //Variables.push();
        Variables.define("this", classInstance);

        try {
            return super.execute(values);
        } finally {
            Variables.pop();
        }
    }
}
