package com.timsystem;

import com.timsystem.lib.Handler;

import java.io.*;
import java.util.Scanner;

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
                    String content = "";
                    while (line != null) {
                        content += line + "\n";
                        line = reader.readLine();
                    }
                    Handler.pathToScript = path;
                    Handler.handle(content); // Класс Handler сделан для удобного отлова ошибок
                } catch (FileNotFoundException e) {
                    System.out.println("File is not found!");
                } catch (IOException e) {
                    System.out.println("File read error!");
                }
            } else if (cmd.equals("cls")) {
                cls();
            } else {
                System.out.println("Command not found!");
            }
        }
    }

    private static void cls() {
        for (int i = 0; i < 800; i++)
            System.out.println("");
        System.out.println("================");
        System.out.println("Special Key " + getVer());
        System.out.println("================\n");
    }
}
