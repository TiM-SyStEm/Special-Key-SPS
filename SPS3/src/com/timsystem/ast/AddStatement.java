package com.timsystem.ast;
import com.timsystem.Main;
import com.timsystem.Reflection;
import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;
import com.timsystem.lib.jrt.JRT;
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
            StringBuilder content = new StringBuilder();
            while (line != null) {
                content.append(line).append("\n");
                line = reader.readLine();
            }
            return content.toString();
        } catch (FileNotFoundException ex1) {
            try{
                String dirPath;
                if(Main.path1 != null){
                    dirPath = Main.path1.toString();
                }
                else{
                    dirPath = "";
                }
                File file = new File(dirPath + "\\" + name + ".spk");
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                StringBuilder content = new StringBuilder();
                while (line != null) {
                    content.append(line).append("\n");
                    line = reader.readLine();
                }
                return content.toString();
            }
            catch (FileNotFoundException ex2){
                throw new SPKException("ModuleError", "module '" + name + "' is not found");
            }
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
                //com.timsystem.sgl.Init.inject();
                System.out.println("SGL has been turned off for a while!!!!");
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
            case "spksoup" -> {
                SpkSoup.inject();
                return;
            }
            case "thread" -> {
                ThreadSPK.inject();
                return;
            }
            case "async" -> {
                Async.inject();
                return;
            }
            case "CrossForms" -> {
                new CrossForms().inject();
                return;
            }
            case "dict" -> {
                new Dict().inject();
                return;
            }
            case "jrt" -> {
                new JRT().inject();
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