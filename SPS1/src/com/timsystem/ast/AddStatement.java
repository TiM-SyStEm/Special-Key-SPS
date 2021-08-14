package com.timsystem.ast;

import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;

import java.io.*;

public class AddStatement implements Statement {

    private String arg;

    public AddStatement(String arg) {
        this.arg = arg;
    }

    public static String readSource(String name) throws IOException {
        try{
            File file = new File(System.getProperty("user.dir") + "\\modules\\" + name + ".spk");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            String content = "";
            while (line != null) {
                content += line + "\n";
                line = reader.readLine();
            }
            return content;
        }
        catch(FileNotFoundException ex){
            throw new SPKException("ModuleError", "module '" + name + "' is not found");
        }
    }

    @Override
    public void execute() {
        if (arg.equals("stl")) {
            STL.inject();
            return;
        }
        try {
            Handler.handle(readSource(arg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Add " + arg;
    }
}
