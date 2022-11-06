package com.timsystem.runtime;

import com.timsystem.ast.Statement;
import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;
import org.lwjglx.Sys;

import java.util.List;

public class ClassMethod extends UserDefinedFunction {

    public final ClassValue classInstance;
    public boolean isPrivate;
    public boolean isProtected;

    public ClassMethod(String name, List<String> arguments, Statement body, ClassValue classInstance, boolean isPrivate, boolean isProtected) {
        super(name, arguments, body);
        this.classInstance = classInstance;
        this.isPrivate = isPrivate;
        this.isProtected = isProtected;
    }

    @Override
    public Value execute(Value[] values) {
        Variables.push();
        Variables.set("this", classInstance);
        if (Handler.SCOPING == ClassScoping.GLOBAL && isPrivate) {
            throw new SPKException("AccessError", "trying to access a private method");
        }
        if (classInstance.isStatic() && Handler.SCOPING == ClassScoping.GLOBAL && isProtected){
            throw new SPKException("AccessError", "trying to access a protected method");
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