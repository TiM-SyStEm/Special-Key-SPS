package com.timsystem.ast;

import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpkSoup {

    public static String docs = "";
    public static Document docum = null;
    public static Elements element = null;

    public static void inject() {

        HashMap<String, Value> spksoup = new HashMap<>();

        spksoup.put("pars",  new FunctionValue(args  -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(args[0].toString()).get();
            } catch (IOException e) {
                e.printStackTrace();
            } catch {
                return;
            }
            Variables.set("document", new StringValue((doc.toString()).getBytes(StandardCharsets.UTF_8)));
            docum = doc;
            return NumberValue.MINUS_ONE;
        }));

        spksoup.put("select", new FunctionValue(args -> {
            Elements divs = docum.select(args[0].toString());
            Variables.set("elements", new StringValue((divs.toString()).getBytes(StandardCharsets.UTF_8)));
            element = divs;
            return NumberValue.MINUS_ONE;
        }));

        spksoup.put("onlyText", new FunctionValue(args -> {
            ArrayList<String> elementi = new ArrayList<String>();
            for (Element elem : element.select(args[0].toString())){
                elementi.add(elem.text());
            }
            for (int i = 1; i < elementi.toArray().length + 1; i++) {
                String name = "elementText" + i;
                Variables.set(name, new StringValue((elementi.get(i - 1)).getBytes(StandardCharsets.UTF_8)));
            }
            Variables.set("sizeElement", new NumberValue((elementi.size())));
            int od = Integer.parseInt(args[1].toString());
            if (od == 1){
                for (Element elem : element.select(args[0].toString())){
                    System.out.println(elem.text());
                }
                return NumberValue.MINUS_ONE;
            }else{
                return NumberValue.MINUS_ONE;
            }
        }));

        spksoup.put("attr", new FunctionValue(args -> {
            for (Element elem : element){
                System.out.println(elem.attr(args[0].toString()));
            }
            return NumberValue.MINUS_ONE;
        }));
        newClass("spksoup", new ArrayList<String>(), spksoup);
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
