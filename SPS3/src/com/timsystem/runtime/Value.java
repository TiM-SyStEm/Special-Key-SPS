package com.timsystem.runtime;

public interface Value {
    Object raw();

    int asInt();

    boolean asBool();

    double asNumber();

    String asString();
}
