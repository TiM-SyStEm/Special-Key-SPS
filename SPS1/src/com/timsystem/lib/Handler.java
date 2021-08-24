package com.timsystem.lib;

import com.timsystem.Lexer;
import com.timsystem.Parser;
import com.timsystem.ast.BlockStatement;
import com.timsystem.runtime.Functions;
import com.timsystem.runtime.Variables;

import java.util.List;

public class Handler {
    public static void handle(String input, String pathToScript) {
        try {
            final List<Token> tokens = new Lexer(input).tokenize();
            final BlockStatement program = new Parser(tokens).parse();
            program.execute();
            Variables.clear();
            Functions.clear();
        } catch (SPKException ex) {
            System.out.println(String.format("%s: %s in file %s", ex.getType(), ex.getText(), pathToScript));
            Variables.clear();
            Functions.clear();
        }
    }
}
