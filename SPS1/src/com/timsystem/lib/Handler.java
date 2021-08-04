package com.timsystem.lib;

import com.timsystem.Lexer;
import com.timsystem.Parser;
import com.timsystem.ast.Statement;

import java.util.List;

public class Handler {
    public static void handle(String input) {
        try {
            final List<Token> tokens = new Lexer(input).tokenize();
            for (Token token : tokens) {
                System.out.println(token);
            }
            final List<Statement> statements = new Parser(tokens).parse();
            for (Statement statement : statements) {
                System.out.println(statement);
            }
            for (Statement statement : statements) {
                statement.execute();
            }
        } catch (SPKException ex) {
            System.out.println(String.format("%s: %s", ex.getType(), ex.getText()));
        }
    }
}
