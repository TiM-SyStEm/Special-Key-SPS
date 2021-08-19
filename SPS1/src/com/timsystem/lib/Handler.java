package com.timsystem.lib;

import com.timsystem.Lexer;
import com.timsystem.Parser;
import com.timsystem.ast.BlockStatement;
import com.timsystem.runtime.Variables;

import java.util.List;

public class Handler {
    public static void handle(String input, String pathToScript) {
        try {
            final List<Token> tokens = new Lexer(input).tokenize();
            for (Token token : tokens) {
                System.out.println(token.toString());
            }
            final BlockStatement program = new Parser(tokens).parse();
            program.execute();
            Variables.clear();
        } catch (SPKException ex) {
            System.out.println(String.format("%s: %s in file %s", ex.getType(), ex.getText(), pathToScript));
            Variables.clear();
        }
    }
}
