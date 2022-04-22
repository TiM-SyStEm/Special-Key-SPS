package com.timsystem;

import com.timsystem.lib.Handler;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Variables;
import org.fusesource.jansi.AnsiConsole;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main {
    public static String getVer() {
        return "SPS2";
    }
    public static final String RESET = "\u001b[0;0;10m";
    public static void main(String[] args) throws IOException {
        AnsiConsole.systemInstall();
        if (args.length == 1) {
            if (!Objects.equals(args[0], "")) {
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
                System.out.print(RESET + "$>");
                BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
                String cmd = sc.readLine();

                if (cmd.contains("comp ")) {
                    String path = cmd.contains(".spk") ?
                            cmd.split(" ")[1] :
                            cmd.split(" ")[1] + ".spk";
                    try {
                        if(!path.contains("\\")){
                            Variables.set("__cwd_run__", new StringValue((System.getProperty("user.dir")).getBytes(StandardCharsets.UTF_8)));
                        }
                        else{
                            Variables.set("__cwd_run__", new StringValue(new File(path).getParent()));
                        }
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
                        URL website = new URL("https://raw.githubusercontent.com/TiM-SyStEm/Special-Key-SPS/main/special-pm" + objs[2] + ".spk");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("modules\\" + objs[2] + ".spk");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                } else if (cmd.contains("ver")) {
                    System.out.println("============");
                    System.out.println(getVer());
                    System.out.println("Patch 0");
                    System.out.println("============");
                } else if (cmd.contains("cls")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("================");
                    System.out.println("Special Key " + getVer());
                    System.out.println("================\n");
                } else {
                    System.out.println("Command not found!");
                }
            }
        }
    }
}
