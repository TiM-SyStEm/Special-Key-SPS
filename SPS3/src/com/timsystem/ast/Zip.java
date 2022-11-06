package com.timsystem.ast;
import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.*;

public class Zip {
    public static void inject() {
        Map<String, Value> zip = new HashMap<>();
        zip.put("create", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            if(args[0] instanceof StringValue){
                zip(args[0].raw().toString(), args[1].raw().toString());
            }
            else if(args[0] instanceof ArrayValue){
                Value[] v = ((ArrayValue) args[0]).array();
                List<File> files = new ArrayList<>();
                for(Value i : v){
                    files.add(new File(i.raw().toString()));
                }
                mzip(files, args[1].raw().toString());
            }
            return NumberValue.MINUS_ONE;
        })));
        zip.put("unpack", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            unzip(args[0].raw().toString(), args[1].raw().toString());
            return NumberValue.MINUS_ONE;
        })));
        newClass("zip", new ArrayList<>(), zip);
    }
    private static void newClass(String name, List<String> structArgs, Map<String, Value> targets) {
        ClassValue result = new ClassValue(name, structArgs);
        for (Map.Entry<String, Value> entry : targets.entrySet()) {
            Value expr = entry.getValue();
            result.setField(entry.getKey(), expr);
        }

        Variables.set(name, result);
    }
    private static void zip(String filename, String outname){
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outname));
            FileInputStream fis= new FileInputStream(filename);)
        {
            ZipEntry entry1=new ZipEntry(filename);
            zout.putNextEntry(entry1);
            // считываем содержимое файла в массив byte
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // добавляем содержимое к архиву
            zout.write(buffer);
            // закрываем текущую запись для новой записи
            zout.closeEntry();
        }
        catch(Exception ex){
            throw new SPKException("ZipError", "Cannot create zip");
        }
    }
    public static File mzip(List<File> files, String filename) {
        File zipfile = new File(filename);
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try {
            // create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            // compress the files
            for(int i=0; i<files.size(); i++) {
                FileInputStream in = new FileInputStream(files.get(i).getCanonicalFile());
                // add ZIP entry to output stream
                out.putNextEntry(new ZipEntry(files.get(i).getName()));
                // transfer bytes from the file to the ZIP file
                int len;
                while((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                // complete the entry
                out.closeEntry();
                in.close();
            }
            // complete the ZIP file
            out.close();
            return zipfile;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    private static void unzip(String filname, String dir){
        try(ZipInputStream zin = new ZipInputStream(new FileInputStream(filname)))
        {
            ZipEntry entry;
            String name;
            long size;
            while((entry=zin.getNextEntry())!=null){

                name = entry.getName(); // получим название файла
                size=entry.getSize();  // получим его размер в байтах
                // распаковка
                FileOutputStream fout = new FileOutputStream(dir + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }
}
