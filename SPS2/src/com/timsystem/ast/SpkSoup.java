package com.timsystem.ast;

import com.timsystem.runtime.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SpkSoup {
    public static String docs = "dd";
    public static void inject() {
        Functions.set("pars", (Value... args) -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(args[0].toString()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            docs = doc.toString();
            return NumberValue.MINUS_ONE;
        });


//        Functions.set("variables", (Value... args) -> {
//            Variables.set("document", new StringValue(docs));
//            return NumberValue.MINUS_ONE;
//        });
        Variables.set("document", new StringValue((docs).getBytes(StandardCharsets.UTF_8)));
    }
}
