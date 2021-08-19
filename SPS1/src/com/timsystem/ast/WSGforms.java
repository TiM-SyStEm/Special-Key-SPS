package com.timsystem.ast;

import com.timsystem.lib.Function;
import com.timsystem.runtime.*;

import javax.swing.*;

public class WSGforms {
    private static final NumberValue MINUS_ONE = new NumberValue(-1);
    private static NumberValue lastKey;
    private static ArrayValue mouseHover;
    private static JFrame frame;

    public static void inject() {
        Functions.set("Form", new WSGforms.DrawForm());
        Functions.set("Prompt", new WSGforms.DrawPrompt());
        Functions.set("Button", new WSGforms.DrawButton());
        Functions.set("TextBox", new WSGforms.DrawTextBox());
        Functions.set("Label", new WSGforms.DrawLabel());
        lastKey = MINUS_ONE;
        mouseHover = new ArrayValue(new Value[]{NumberValue.ZERO, NumberValue.ZERO});
    }

    private static class DrawForm implements Function {

        @Override
        public Value execute(Value... args) {
            String title = "";
            int width = 640;
            int height = 480;
            switch (args.length) {
                case 1:
                    title = args[0].raw().toString();
                    break;
                case 2:
                    title = "WSGforms";
                    width = (int) args[0].asNumber();
                    height = (int) args[1].asNumber();
                    break;
                case 3:
                    title = args[0].raw().toString();
                    width = (int) args[1].asNumber();
                    height = (int) args[2].asNumber();
                    break;
            }

            frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);
            frame.setVisible(true);
            return NumberValue.ZERO;
        }
    }

    private static class DrawButton implements Function {
        @Override
        public Value execute(Value... args) {
            String title;
            int width = 100;
            int height = 30;
            int x = 0;
            int y = 0;
            JButton button;
            switch (args.length) {
                case 1:
                    title = args[0].raw().toString();
                    button = new JButton(title);
                    button.setBounds(x, y, width, height);
                    frame.add(button);
                    break;
                case 3:
                    title = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    button = new JButton(title);
                    button.setBounds(x, y, width, height);
                    frame.add(button);
                    break;
                case 5:
                    title = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    width = (int) args[3].raw();
                    height = (int) args[4].raw();
                    button = new JButton(title);
                    button.setBounds(x, y, width, height);
                    frame.add(button);
                    break;
            }
            return NumberValue.ZERO;
        }
    }

    private static class DrawTextBox implements Function {
        @Override
        public Value execute(Value... args) {
            String text;
            int width = 150;
            int height = 30;
            int x = 0;
            int y = 0;
            JTextField txtbox;
            switch (args.length) {
                case 1:
                    text = args[0].raw().toString();
                    txtbox = new JTextField(text);
                    txtbox.setBounds(x, y, width, height);
                    frame.add(txtbox);
                    break;
                case 3:
                    text = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    txtbox = new JTextField(text);
                    txtbox.setBounds(x, y, width, height);
                    frame.add(txtbox);
                    break;
                case 4:
                    text = "";
                    x = (int) args[0].raw();
                    y = (int) args[1].raw();
                    width = (int) args[2].raw();
                    height = (int) args[3].raw();
                    txtbox = new JTextField(text);
                    txtbox.setBounds(x, y, width, height);
                    frame.add(txtbox);
                    break;
                case 5:
                    text = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    width = (int) args[3].raw();
                    height = (int) args[4].raw();
                    txtbox = new JTextField(text);
                    txtbox.setBounds(x, y, width, height);
                    frame.add(txtbox);
                    break;
            }
            return NumberValue.ZERO;
        }
    }

    private static class DrawLabel implements Function {
        @Override
        public Value execute(Value... args) {
            String text;
            int width = 150;
            int height = 30;
            int x = 0;
            int y = 0;
            JLabel label;
            switch (args.length) {
                case 1:
                    text = args[0].raw().toString();
                    label = new JLabel(text);
                    label.setBounds(x, y, width, height);
                    frame.add(label);
                    break;
                case 3:
                    text = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    label = new JLabel(text);
                    label.setBounds(x, y, width, height);
                    frame.add(label);
                    break;
                case 5:
                    text = args[0].raw().toString();
                    x = (int) args[1].raw();
                    y = (int) args[2].raw();
                    width = (int) args[3].raw();
                    height = (int) args[4].raw();
                    label = new JLabel(text);
                    label.setBounds(x, y, width, height);
                    frame.add(label);
                    break;
            }
            return NumberValue.ZERO;
        }
    }

    private static class DrawPrompt implements Function {
        @Override
        public Value execute(Value... args) {
            final String v = JOptionPane.showInputDialog(args[0].raw().toString());
            return new StringValue(v == null ? "0" : v);
        }
    }
}
