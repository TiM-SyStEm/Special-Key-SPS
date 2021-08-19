package com.timsystem.lib;

import com.timsystem.runtime.Value;

public interface Function {
    Value execute(Value... args);
}
