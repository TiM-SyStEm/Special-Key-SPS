package com.timsystem.ast;
import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.timsystem.Main.getVer;

public class STL {
    public static void inject() {
        Variables.set("__ver__", new StringValue(("Special Key " + getVer()).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__about__", new StringValue(("SPK is dynamic a interpreted programming language built on Java Virtual Machine\nCreated by Timofey Gorlov in Russia with his team").getBytes(StandardCharsets.UTF_8)));
        Variables.set("__cwd__", new StringValue((System.getProperty("user.dir")).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__os__", new StringValue((System.getProperty("os.name")).getBytes(StandardCharsets.UTF_8)));
        Variables.set("__user__", new StringValue((System.getProperty("user.name")).getBytes(StandardCharsets.UTF_8)));
        Functions.set("typeof", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0] instanceof NumberValue) {
                try {
                    args[0].asInt();
                    return new StringValue("Int");
                } catch (Exception exception2) {
                    return new StringValue("Float");
                }
            } else if (args[0] instanceof StringValue) {
                return new StringValue("String");
            }else if (args[0] instanceof ArrayValue){
                return new StringValue("Array");
            } else return new StringValue("UnknownType");
        });
        Functions.set("sleep", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                Thread.sleep((long) args[0].asNumber());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return NumberValue.MINUS_ONE;
        });
        Functions.set("downloadWithURL", (Value... args) -> {
            Arguments.check(2, args.length);
            try {
                URL website = new URL(args[0].raw().toString());
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(args[1].raw().toString());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.MINUS_ONE;
        });
        Functions.set("arrayAppend", (args -> {
            Arguments.check(2, args.length);
            ((ArrayValue) args[0]).append(args[1]);
            return args[0];
        }));
        initFileClass();
        initDirClass();
        initMathClass();
        initStrClass();
        initProcess();
        initChar();
        initColor();
        initEffects();
        initDatetime();
    }
    private static void initDatetime(){
        Map<String, Value> dt = new HashMap<>();
        dt.put("getSeconds", new FunctionValue((args -> new NumberValue(new Date().getSeconds()))));
        dt.put("getMinutes", new FunctionValue((args -> new NumberValue(new Date().getMinutes()))));
        dt.put("getHours", new FunctionValue((args -> new NumberValue(new Date().getHours()))));
        dt.put("getDayWeek", new FunctionValue((args -> new NumberValue(new Date().getDay()))));
        dt.put("getDate", new FunctionValue((args -> new NumberValue(new Date().getDate()))));
        dt.put("getMonth", new FunctionValue((args -> new NumberValue(new Date().getMonth()))));
        dt.put("getYear", new FunctionValue((args -> new NumberValue(new Date().getYear()))));
        newClass("dt", new ArrayList<>(), dt);
    }

    private static void initEffects() {
        Map<String, Value> effects = new HashMap<>();
        effects.put("normal", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[0m" + args[0]);
        })));
        effects.put("bold", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[1m" + args[0]);
        })));
        effects.put("light", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[2m" + args[0]);
        })));
        effects.put("italic", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[3m" + args[0]);
        })));
        effects.put("underline", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[4m" + args[0]);
        })));
        effects.put("blink", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[5m" + args[0]);
        })));
        newClass("effects", new ArrayList<>(), effects);
    }

    private static void initColor() {
        Map<String, Value> color = new HashMap<>();
        Map<String, Value> backcolor = new HashMap<>();
        color.put("reset", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[0m" + args[0]);
        })));
        color.put("black", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[30m" + args[0]);
        })));
        color.put("red", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[31m" + args[0]);
        })));
        color.put("green", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[32m" + args[0]);
        })));
        color.put("yellow", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[33m" + args[0]);
        })));
        color.put("blue", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[34m" + args[0]);
        })));
        color.put("purple", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[35m" + args[0]);
        })));
        color.put("cyan", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[36m" + args[0]);
        })));
        color.put("white", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[37m" + args[0]);
        })));
        backcolor.put("reset", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[10m" + args[0]);
        })));
        backcolor.put("black", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[40m" + args[0]);
        })));
        backcolor.put("red", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[41m" + args[0]);
        })));
        backcolor.put("green", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[42m" + args[0]);
        })));
        backcolor.put("yellow", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[43m" + args[0]);
        })));
        backcolor.put("blue", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[44m" + args[0]);
        })));
        backcolor.put("purple", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[45m" + args[0]);
        })));
        backcolor.put("cyan", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[46m" + args[0]);
        })));
        backcolor.put("white", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new StringValue("\u001b[47m" + args[0]);
        })));
        newClass("color", new ArrayList<>(), color);
        newClass("backcolor", new ArrayList<>(), backcolor);
    }
    private static void initDirClass() {
        Map<String, Value> dir = new HashMap<>();
        dir.put("create", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            boolean isCreated = new File(args[0].raw().toString()).mkdir();
            if (!isCreated) {
                return NumberValue.ZERO;
            } else {
                return NumberValue.ONE;
            }
        })));
        dir.put("delete", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            File file = new File(args[0].raw().toString());
            deleteDir(file);
            return NumberValue.MINUS_ONE;
        })));
        dir.put("isempty", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            File[] contents = new File(args[0].raw().toString()).listFiles();
            if (contents == null) {
                return NumberValue.ONE;
            } else {
                return NumberValue.ZERO;
            }
        })));
        dir.put("list", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            File[] contents = new File(args[0].raw().toString()).listFiles();
            List<String> files = new ArrayList<>();
            if (contents != null) {
                for (File f : contents) {
                    files.add(f.toString());
                }
            }
            return ArrayValue.of(files.toArray(new String[files.size()]));
        })));
        newClass("dir", new ArrayList<>(), dir);
    }

    private static void initStrClass() {
        Map<String, Value> str = new HashMap<>();
        str.put("format", new FunctionValue((args -> {
            String dnstr = args[0].toString();
            for(int i = 1; i < args.length; i++){
                final String regex = "{" + (i-1) + "}";
                dnstr = dnstr.replace(regex, args[i].toString());
            }
            return new StringValue(dnstr);
        })));
        str.put("replace", new FunctionValue((args -> {
            Arguments.check(3, args.length);
            final String input = args[0].toString();
            final String regex = args[1].toString();
            final String replacement = args[2].toString();

            return new StringValue(input.replaceAll(regex, replacement));
        })));
        str.put("split", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            final String input = args[0].toString();
            final String spl = args[1].toString();

            return ArrayValue.of(input.split(spl));
        })));
        str.put("chars", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            char[] chars = args[0].toString().toCharArray();
            return ArrayValue.of(chars);
        })));
        str.put("bychars", new FunctionValue((args -> {
            ArrayValue chars;
            String string = "";
            switch (args.length) {
                case 1:
                    chars = (ArrayValue) args[0];
                    for (int x = 0; x < chars.length(); x++) {
                        if (chars.get(x).asString().length() == 1) {
                            string += chars.get(x);
                        } else {
                            throw new SPKException("NotCharacter", "element '" + chars.get(x) + "' is mot a char");
                        }
                    }
                    break;
                case 2:
                    chars = (ArrayValue) args[0];
                    for (int x = 0; x < chars.length(); x++) {
                        if (chars.get(x).asString().length() == 1) {
                            if (string.length() == 0) string += chars.get(x);
                            else string += args[1].toString() + chars.get(x);
                        } else {
                            throw new SPKException("NotCharacter", "element '" + chars.get(x) + "' is mot a char");
                        }
                    }
                    break;
            }
            return new StringValue(string);
        })));
        str.put("empty", new StringValue(""));
        newClass("str", new ArrayList<>(), str);
    }

    private static void initProcess() {
        Map<String, Value> process = new HashMap<>();
        process.put("start", new FunctionValue((args -> {
            switch (args.length) {
                case 1:
                    try {
                        ProcessBuilder builder = new ProcessBuilder(args[0].toString());
                        // указываем перенаправление stderr в stdout, чтобы проще было отлаживать
                        builder.redirectErrorStream(true);
                        Process pb = builder.start();

                        try (BufferedReader br = new BufferedReader(new InputStreamReader(pb.getInputStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        }

                        // ждем завершения процесса
                        pb.waitFor();
                    } catch (IOException | InterruptedException ex) {
                        throw new SPKException("StartProcessError", "start process return error");
                    }
                    break;
                case 2:
                    try {
                        ProcessBuilder builder = new ProcessBuilder(args[0].toString(), args[1].toString());
                        // указываем перенаправление stderr в stdout, чтобы проще было отлаживать
                        builder.redirectErrorStream(true);
                        Process pb = builder.start();

                        try (BufferedReader br = new BufferedReader(new InputStreamReader(pb.getInputStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        }

                        // ждем завершения процесса
                        pb.waitFor();
                    } catch (IOException | InterruptedException ex) {
                        throw new SPKException("StartProcessError", "start process return error");
                    }
                    break;
            }
            return NumberValue.MINUS_ONE;
        })));
        process.put("exit", new FunctionValue((args -> {
            System.exit(0);
            return NumberValue.MINUS_ONE;
        })));
        newClass("process", new ArrayList<>(), process);
    }

    private static void initChar() {
        Map<String, Value> Char = new HashMap<>();
        Char.put("isvalid", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            try {
                //Test
                args[0].asInt();
                return NumberValue.ZERO;
            } catch (Exception ex) {
                if (args[0].toString().length() == 1) {
                    return NumberValue.ONE;
                } else return NumberValue.ZERO;
            }
        })));
        newClass("char", new ArrayList<>(), Char);
    }

    private static void initFileClass() {
        Map<String, Value> file = new HashMap<>();
        file.put("create", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            try (FileWriter writer = new FileWriter(args[0].raw().toString(), false)) {
                writer.write("");
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileCreateError", "can't create file");
            }
            return NumberValue.MINUS_ONE;
        }));
        file.put("read", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            try (FileReader reader = new FileReader(args[0].raw().toString())) {
                // читаем посимвольно
                int c;
                StringBuilder all = new StringBuilder();
                while ((c = reader.read()) != -1) {
                    all.append((char) c);
                }
                return new StringValue(all.toString());
            } catch (IOException ex) {
                throw new SPKException("FileReadError", "the file cannot be read");
            }
        }));
        file.put("write", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            try (FileWriter writer = new FileWriter(args[0].raw().toString(), false)) {
                String text = args[1].raw().toString();
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.MINUS_ONE;
        }));
        file.put("append", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            try (FileWriter writer = new FileWriter(args[0].raw().toString(), true)) {
                String text = args[1].raw().toString();
                writer.write(text);
                writer.flush();
            } catch (IOException ex) {
                throw new SPKException("FileWriteError", "can't write a str to a file");
            }
            return NumberValue.MINUS_ONE;
        }));
        newClass("File", new ArrayList<>(), file);
    }

    private static void initMathClass() {
        Map<String, Value> math = new HashMap<>();
        math.put("PI", NumberValue.of(Math.PI));
        math.put("E", NumberValue.of(Math.E));
        math.put("sin", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.sin(args[0].asNumber()));
        }));
        math.put("cos", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.cos(args[0].asNumber()));
        }));
        math.put("tan", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.tan(args[0].asNumber()));
        }));
        math.put("sqrt", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        }));
        math.put("cbrt", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        }));
        math.put("round", new FunctionValue(args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Math.round(args[0].asNumber()));
        }));
        math.put("random", new FunctionValue(args -> {
            Arguments.check(2, args.length);
            return new NumberValue(args[0].asNumber() + (int) (Math.random() * args[1].asNumber()));
        }));
        newClass("Math", new ArrayList<>(), math);
    }
    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private static void newClass(String name, List<String> structArgs, Map<String, Value> targets) {
        ClassValue result = new ClassValue(name, structArgs);
        for (Map.Entry<String, Value> entry : targets.entrySet()) {
            Value expr = entry.getValue();
            result.setField(entry.getKey(), expr);
        }

        Variables.set(name, result);
    }
}