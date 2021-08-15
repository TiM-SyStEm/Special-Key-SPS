package com.timsystem;

import com.timsystem.lib.Handler;
import com.timsystem.lib.SPKException;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main {
    public static String getVer() {
        return "SPS1";
    }

    public static void main(String[] args) throws IOException {
        System.out.println("================");
        System.out.println("Special Key " + getVer());
        System.out.println("================\n");
        while (true) {
            System.out.print("$>");
            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
            String cmd = sc.readLine();

            if (cmd.contains("comp ")) {
                String path = cmd.contains(".spk") ?
                        cmd.split(" ")[1] :
                        cmd.split(" ")[1] + ".spk";
                try {
                    File file = new File(path);
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
                    String line = reader.readLine();
                    StringBuilder content = new StringBuilder();
                    while (line != null) {
                        content.append(line).append("\n");
                        line = reader.readLine();
                    }
                    Handler.pathToScript = path;
                    Handler.handle(content.toString()); // Класс Handler сделан для удобного отлова ошибок
                } catch (FileNotFoundException e) {
                    System.out.println("File is not found!");
                } catch (IOException e) {
                    System.out.println("File read error!");
                }
            } else if (cmd.contains("special-pm")) {
                String[] objs = cmd.split(" ");
                if(objs[1].equals("install")){
                    URL website = new URL("https://raw.githubusercontent.com/TiM-SyStEm/Spk-site/main/special-pm/" + objs[2] + ".spk");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream("modules\\" + objs[2] + ".spk");
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
                cls();
            } else if (cmd.equals("cls")) {
                cls();
            } else {
                System.out.println("Command not found!");
            }
        }
    }

    private static void cls() {
        for (int i = 0; i < 800; i++)
            System.out.println();
        System.out.println("================");
        System.out.println("Special Key " + getVer());
        System.out.println("================\n");
    }
}
