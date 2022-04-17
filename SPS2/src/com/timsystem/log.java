package com.timsystem;

import java.io.FileWriter;

public class log {
    public static void append(String str) throws Exception {
        FileWriter writer = new FileWriter("log.txt", true);
        writer.write(str);
        writer.flush();
    }
    public static void clear() throws Exception {
        FileWriter writer = new FileWriter("log.txt", true);
        writer.write("");
        writer.flush();
    }
}
