package com.timsystem.lib;

import com.timsystem.ast.Statement;
import com.timsystem.runtime.UserDefinedClass;
import com.timsystem.runtime.UserDefinedFunction;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

import java.util.List;

public class Method extends UserDefinedFunction {

    public final UserDefinedClass classInstance;

    public Method(String name, List<String> arguments, Statement body, UserDefinedClass classInstance) {
        super(name, arguments, body);
        this.classInstance = classInstance;
    }

    @Override
    public Value execute(Value[] values) {
        Variables.push();
        Variables.define("self", classInstance.getThisMap());

        try {
            return super.execute(values);
        } finally {
            Variables.pop();
        }
    }
}
