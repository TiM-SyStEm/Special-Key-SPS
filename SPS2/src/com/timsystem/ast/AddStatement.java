package com.timsystem.ast;
import com.timsystem.Reflection;
import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.YAML;

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
        switch (arg) {
            case "stl" -> {
                STL.inject();
                return;
            }
            case "CanvasAPI" -> {
                CanvasAPI.inject();
                return;
            }
            case "Keys" -> {
                Keys.inject();
                return;
            }
            case "Colors" -> {
                Colors.inject();
                return;
            }
            case "reflection" -> {
                Reflection ref = new Reflection();
                ref.inject();
                return;
            }
            case "spkunit" -> {
                SPKUnit.inject();
                return;
            }
            case "sgl" -> {
                com.timsystem.sgl.Init.inject();
                return;
            }
            case "MouseButtons" -> {
                MouseButtons.inject();
                return;
            }
            case "zip" -> {
                Zip.inject();
                return;
            }
            case "json" -> {
                Json.inject();
                return;
            }
            case "yaml" -> {
                new YAML().inject();
                return;
            }
            case "xml" -> {
                XML.inject();
                return;
            }
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