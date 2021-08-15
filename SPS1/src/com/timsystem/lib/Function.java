package com.timsystem.lib;

import com.timsystem.runtime.Value;

import java.io.IOException;

public interface Function {
    Value execute(Value... args);
}
