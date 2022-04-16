package com.timsystem.ast;

import com.timsystem.Reflection;
import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;

import java.io.*;

public class AddStatement implements Statement {

    private String arg;

    public AddStatement(String arg) {
        this.arg = arg;
    }

    public static String readSource(String name) throws IOException {
        try {
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
        } catch (FileNotFoundException ex) {
            throw new SPKException("ModuleError", "module '" + name + "' is not found");
        }
    }

    @Override
    public void execute() {
        if (arg.equals("stl")) {
            STL.inject();
            return;
        } else if (arg.equals("CanvasAPI")) {
            CanvasAPI.inject();
            return;
        } else if (arg.equals("Keys")) {
            Keys.inject();
            return;
        } else if (arg.equals("Colors")) {
            Colors.inject();
            return;
        } else if (arg.equals("reflection")) {
            Reflection ref = new Reflection();
            ref.inject();
            return;
        } else if (arg.equals("spkunit")) {
            spkunit.inject();
            return;
        } else if(arg.equals("sgl")){
            com.timsystem.sgl.Init.inject();
            return;
        } else if(arg.equals("MouseButtons")) {
            MouseButtons.inject();
            return;
        }
        try {
            Handler.handle(readSource(arg), arg, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Add " + arg;
    }
}
