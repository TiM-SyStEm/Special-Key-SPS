package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XML {
    public static void inject() {
        HashMap<String, Value> xml = new HashMap<>();

        xml.put("read", new FunctionValue((Value... args) -> {
            Arguments.check(1, args.length);
            try {
                SAXReader reader = new SAXReader();
                Document result = reader.read(new File(args[0].toString()));
                return new XMLValue(result);
            } catch (DocumentException e) {
                throw new SPKException("BadXML", "Path doesn't exists or file is corrupted");
            }
        }));

        xml.put("selectNode", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Document document = ((XMLValue) args[0]).getDocument();
            Node node = document.selectSingleNode(args[1].toString());
            return new XMLNodeValue(node);
        }));

        xml.put("getAttribute", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Node node = ((XMLNodeValue) args[0]).getNode();
            return new StringValue(node.valueOf(args[1].toString()));
        }));

        xml.put("selectNodes", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            try {
                Node node = ((XMLNodeValue) args[0]).getNode();
                List<Node> temp = node.selectNodes(args[1].toString());
                XMLNodeValue[] result = new XMLNodeValue[temp.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = new XMLNodeValue(temp.get(i));
                }
                return new ArrayValue(result);
            } catch (ClassCastException ex) {
                Document node = ((XMLValue) args[0]).getDocument();
                List<Node> temp = node.selectNodes(args[1].toString());
                XMLNodeValue[] result = new XMLNodeValue[temp.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = new XMLNodeValue(temp.get(i));
                }
                return new ArrayValue(result);
            }

        }));

        xml.put("createEmptyDocument", new FunctionValue((Value... args) -> {
           Arguments.check(0, args.length);
           return new XMLValue(DocumentHelper.createDocument());
        }));

        xml.put("getNodeName", new FunctionValue((Value... args) -> {
            Arguments.check(1, args.length);
            Node node = ((XMLNodeValue) args[0]).getNode();
            return new StringValue(node.getName());
        }));

        xml.put("spawnRootElement", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Document doc = ((XMLValue) args[0]).getDocument();
            Element root = doc.addElement(args[1].toString());
            return new XMLElementValue(root, doc);
        }));

        xml.put("createElement", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Element root = ((XMLElementValue) args[0]).getElement();
            Element result = root.addElement(args[1].toString());
            return new XMLElementValue(result);
        }));

        xml.put("addElementAttribute", new FunctionValue((Value... args) -> {
            Arguments.check(3, args.length);
            Element element = ((XMLElementValue) args[0]).getElement();
            element.addAttribute(args[1].toString(), args[2].toString());
            return args[0];
        }));

        xml.put("addElementText", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Element element = ((XMLElementValue) args[0]).getElement();
            element.addText(args[1].toString());
            return args[0];
        }));

        xml.put("setElementText", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            Element element = ((XMLElementValue) args[0]).getElement();
            element.setText(args[1].toString());
            return args[0];
        }));

        xml.put("dumpXML", new FunctionValue((Value... args) -> {
            Arguments.check(2, args.length);
            try {
                FileWriter writer = new FileWriter(args[0].toString());
                Document doc = ((XMLElementValue) args[1]).getDoc();
                doc.write(writer);
                writer.close();
            } catch (IOException e) {
                throw new SPKException("NoSuchFile", "File doesn't exists");
            }
            return NumberValue.ONE;
        }));


        newClass("xml", new ArrayList<String>(), xml);
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
