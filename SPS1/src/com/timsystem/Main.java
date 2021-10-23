package com.timsystem;

import com.timsystem.lib.Handler;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
public class Main {
    public static String getVer() {
        return "SPS1";
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            if (args[0] != "") {
                String path = args[0];
                File file = new File(path);
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                StringBuilder content = new StringBuilder();
                while (line != null) {
                    content.append(line).append("\n");
                    line = reader.readLine();
                }
                Handler.handle(content.toString(), path, false);
            }
            /*
             * Стававим консоль на паузу,
             * пока пользователь не нажмёт любую клавишу
             */
            int code;
            while (-1 == (code = System.in.read())) {
            }
            System.exit(0);
        } else {
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
                        Handler.handle(content.toString(), path, false); // Класс Handler сделан для удобного отлова ошибок
                    } catch (FileNotFoundException e) {
                        System.out.println("File is not found!");
                    } catch (IOException e) {
                        System.out.println("File read error!");
                    }
                } else if (cmd.contains("special-pm")) {
                    String[] objs = cmd.split(" ");
                    if (objs[1].equals("install")) {
                        URL website = new URL("https://raw.githubusercontent.com/TiM-SyStEm/Spk-site/main/special-pm/" + objs[2] + ".spk");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("modules\\" + objs[2] + ".spk");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                } else if (cmd.contains("ver")) {
                    System.out.println("============");
                    System.out.println(getVer());
                    System.out.println("Patch 0");
                    System.out.println("============");
                } else {
                    System.out.println("Command not found!");
                }
            }
        }
    }
}
