package com.timsystem.runtime;

import com.timsystem.lib.MapValue;
import com.timsystem.lib.Method;
import com.timsystem.lib.SPKException;

import java.util.Objects;

public class UserDefinedClass implements Value {

    private final String className;
    private final MapValue self;
    private Method constructor;

    public UserDefinedClass(String name) {
        this.className = name;
        this.self = new MapValue(10);
    }

    public MapValue getThisMap() {
        return self;
    }

    public String getClassName() {
        return className;
    }

    public void addField(String name, Value value) {
        self.set(name, value);
    }

    public void addMethod(String name, Method method) {
        self.set(name, (Value) method);
        if (name.equals(className)) {
            constructor = method;
        }
    }

    public void callConstructor(Value[] args) {
        if (constructor != null) {
            constructor.execute(args);
        }
    }

    public Value access(Value value) {
        return self.get(value);
    }

    @Override
    public Object raw() {
        return null;
    }

    @Override
    public int asInt() {
        throw new SPKException("TypeException", "Cannot cast class to integer");
    }

    @Override
    public boolean asBool() {
        return false;
    }

    @Override
    public double asNumber() {
        throw new SPKException("TypeException", "Cannot cast class to integer");
    }

    @Override
    public String asString() {
        return "class " + className + "@" + self;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final UserDefinedClass other = (UserDefinedClass) obj;
        return Objects.equals(this.className, other.className)
                && Objects.equals(this.self, other.self);
    }


    @Override
    public String toString() {
        return asString();
    }
}
