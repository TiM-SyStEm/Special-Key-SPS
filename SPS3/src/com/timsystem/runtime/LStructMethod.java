package com.timsystem.runtime;

import com.timsystem.ast.Statement;
import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;

import java.util.List;

public class LStructMethod extends UserDefinedFunction {

    public final LStructValue structInstance;
    public boolean isPrivate;

    public LStructMethod(String name, List<String> arguments, Statement body, LStructValue structInstance, boolean isPrivate) {
        super(name, arguments, body);
        this.structInstance = structInstance;
        this.isPrivate = isPrivate;
    }

    @Override
    public Value execute(Value[] values) {
        Variables.push();
        Variables.set("this", structInstance);
        if (Handler.SCOPING == ClassScoping.GLOBAL && isPrivate) {
            throw new SPKException("AccessError", "trying to access a private method");
        }
        Handler.SCOPING = ClassScoping.INTERNAL;
        try {
            Value rv = super.execute(values);
            if (!isPrivate) Handler.SCOPING = ClassScoping.GLOBAL;
            return rv;
        } finally {
            Variables.pop();
        }
    }
}
