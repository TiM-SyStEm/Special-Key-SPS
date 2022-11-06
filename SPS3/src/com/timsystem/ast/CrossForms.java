package com.timsystem.ast;

import com.timsystem.lib.Arguments;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ClassValue;
import com.timsystem.runtime.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.List;
import java.util.*;

public class CrossForms extends JFrame {
    private XMLValue cfmlFile;
    private int Wwidth;
    private int Wheight;
    private String cfmlVersion;
    private String cfmlMinSPK;
    private final List<JavaTuple> components = new ArrayList<JavaTuple>();
    private final List<String> actions = new ArrayList<>();

    private static void DrawLabel() {

    }

    private void DrawCombox() {
        Document document = cfmlFile.getDocument();
        Node node = document.selectSingleNode("/window/settings");
        List<Node> boxes = node.selectNodes("/window/combobox");
        for (Node box : boxes) {
            JComboBox cb = new JComboBox(box.valueOf("@list").split("\\;"));
            add(cb);
            cb.setLayout(null);
            cb.setBounds(Integer.parseInt(box.valueOf("@x")),
                    Integer.parseInt(box.valueOf("@y")),
                    Integer.parseInt(box.valueOf("@width")),
                    Integer.parseInt(box.valueOf("@height")));
            cb.setSelectedIndex(Integer.parseInt(box.valueOf("@base")));
            cb.setVisible(true);

            if (!box.valueOf("@callback").equals("")) {
                cb.addItemListener(e -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        Functions.get(box.valueOf("@callback")).execute(new StringValue((String) e.getItem()));
                    }
                });
            }
        }
    }

    private void DrawButtons(){
        Document node = cfmlFile.getDocument();
        List<Node> buttons = node.selectNodes("/window/button");
        for(Node button : buttons){
            JButton jbutton = new JButton(button.getText());
            add(jbutton);
            jbutton.setBounds(Integer.parseInt(button.valueOf("@x")),
                    Integer.parseInt(button.valueOf("@y")),
                    Integer.parseInt(button.valueOf("@width")),
                    Integer.parseInt(button.valueOf("@height")));
            jbutton.setLayout(null);
            jbutton.setVisible(true);
            String click = button.valueOf("@click");
            Function clickFunc = null;
            if (!click.equals("")) clickFunc = Functions.get(click);
            Function finalClickFunc = clickFunc;
            jbutton.addActionListener(e -> {
                if (finalClickFunc != null)  finalClickFunc.execute(new StringValue(button.valueOf("@id")));
            });
            if(!Objects.equals(button.valueOf("@id"), "") || button.valueOf("@id") != null) {
                components.add(new JavaTuple(button.valueOf("@id"), jbutton));
            }
        }
    }

    private static void newClass(String name, List<String> structArgs, Map<String, Value> targets) {
        ClassValue result = new ClassValue(name, structArgs);
        for (Map.Entry<String, Value> entry : targets.entrySet()) {
            Value expr = entry.getValue();
            result.setField(entry.getKey(), expr);
        }
        Variables.set(name, result);
    }

    public void inject() {
        Map<String, Value> cf = new HashMap<>();
        cf.put("loadCFML", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            try {
                SAXReader reader = new SAXReader();
                Document result = reader.read(new File(args[0].toString()));
                cfmlFile = new XMLValue(result);
                createForm();
                StringValue[] ret_vals = new StringValue[]{new StringValue(cfmlVersion), new StringValue(cfmlMinSPK)};
                return new ArrayValue(ret_vals);
            } catch (DocumentException e) {
                throw new SPKException("BadXML", "Path doesn't exists or file is corrupted");
            }
        })));
        cf.put("setText", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            for (JavaTuple jt : components) {
                if (Objects.equals(jt.getKey(), args[0].raw().toString())) {
                    ((JLabel) jt.getValue()).setText(args[1].raw().toString());
                    break;
                }
            }
            return NumberValue.MINUS_ONE;
        })));
        cf.put("unbindAll", new FunctionValue((args -> {
            components.clear();
            return NumberValue.MINUS_ONE;
        })));
        cf.put("setSize", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            setSize(args[0].asInt(), args[1].asInt());
            return NumberValue.MINUS_ONE;
        })));
        cf.put("getTitle", new FunctionValue((args -> new StringValue(getTitle()))));
        cf.put("getSize", new FunctionValue((args -> {
            NumberValue[] wh = new NumberValue[]{new NumberValue(getSize().getWidth()),
                    new NumberValue(getSize().getHeight())};
            return new ArrayValue(wh);
        })));
        newClass("cf", new ArrayList<>(), cf);
    }

    private void createForm() {
        // Form settings
        Document document = cfmlFile.getDocument();
        Node node = document.selectSingleNode("/window/settings");
        Container c = getContentPane(); // клиентская область окна
        c.setLayout(new BorderLayout()); // выбираем компоновщик
        Node cfmlMain = node.selectSingleNode("/window/cfml");
        Node title = node.selectSingleNode("//title");
        Node width = node.selectSingleNode("//widtht");
        Node height = node.selectSingleNode("//height");
        cfmlVersion = cfmlMain.valueOf("@version");
        cfmlMinSPK = cfmlMain.valueOf("@minspk");

        if (title != null)
            setTitle(title.getText());
        else setTitle("CrossForms SPS3");
        if (width == null)
            Wwidth = 640;
        else Wwidth = Integer.parseInt(width.getText());
        if (height == null)
            Wheight = 480;
        else Wheight = Integer.parseInt(height.getText());

        setSize(Wwidth, Wheight);
        setLayout(null);
        setVisible(true);
        // End form settings

        // Draw elements
        DrawCombox();
        DrawLabel();
        DrawButtons();
        // End draw elements
    }
}
