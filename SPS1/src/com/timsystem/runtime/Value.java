package com.timsystem.runtime;

public interface Value {
    Object raw();

    double asNumber();

    int asInt();

    boolean asBool();
}
