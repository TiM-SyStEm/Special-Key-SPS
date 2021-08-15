package com.timsystem.lib;

import com.timsystem.runtime.Value;

public interface GettableSettable {
    Value get();

    void set(Value expr);
}
