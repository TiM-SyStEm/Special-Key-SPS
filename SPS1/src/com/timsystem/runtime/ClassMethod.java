package com.timsystem.runtime;

import com.timsystem.ast.Statement;

import java.util.List;

public class ClassMethod extends UserDefinedFunction {

    public final ClassValue classInstance;

    public ClassMethod(List<String> arguments, Statement body, ClassValue classInstance) {
        super(arguments, body);
        this.classInstance = classInstance;
    }

    @Override
    public Value execute(Value[] values) {
        Variables.push();
        Variables.set("this", classInstance);

        try {
            return super.execute(values);
        } finally {
            Variables.pop();
        }
    }
}
