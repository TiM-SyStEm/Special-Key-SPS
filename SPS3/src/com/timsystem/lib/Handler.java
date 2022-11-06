package com.timsystem.lib;

import com.timsystem.Lexer;
import com.timsystem.Parser;
import com.timsystem.ast.BlockStatement;
import com.timsystem.ast.Expression;
import com.timsystem.log;
import com.timsystem.runtime.ClassScoping;
import com.timsystem.runtime.ClassValue;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

import java.util.*;

public class Handler {

    public static ClassScoping SCOPING = ClassScoping.GLOBAL;
    public static List<String> instencesed = new ArrayList<>();

    public static void handle(String input, String pathToScript, boolean isExec) {
        try {
            try{
                if(!isExec) {
                    log.clear();
                    log.append(String.format("Start compiling (%s)\n", new Date()));
                }
            }
            catch (Exception ignored){}
            input = DeprecatedPreprocessor.preprocess(input);
            final List<Token> tokens = new Lexer(input).tokenize();

            /*for(Token t : tokens){
            *    System.out.println(t.getType() + "\t" + t.getText());
            }*/

            final BlockStatement program = new Parser(tokens).parse();
            program.execute();
            if(!isExec){
                Variables.clear();
            }
        } catch (SPKException ex) {
            try{
                log.append(String.format("%s: %s in %s (%s)\n", ex.getType(), ex.getText(), pathToScript, new Date()));
            }
            catch (Exception ex2){}
            System.out.println(String.format("%s: %s in %s", ex.getType(), ex.getText(), pathToScript));
            if(!isExec) {
                Variables.clear();
            }
            int count = CallStack.getCalls().size();
            if (count == 0) return;
            System.out.println(String.format("\nCall stack was:"));
            for (CallStack.CallInfo info : CallStack.getCalls()) {
                System.out.println("    " + count + ". " + info);
                count--;
            }
        }
    }
    public static Value setInstenced(Value res){
        try{instencesed.add(((ClassValue)res).name);}
        catch (Exception ex){}
        return res;
    }
    public static Value returnHandle(String input, String pathToScript) {
        try {
            final List<Token> tokens = new Lexer(input).tokenize();
            final Expression program = new Parser(tokens).parseExpr();
            return program.eval();
        } catch (SPKException ex) {
            System.out.println(String.format("%s: %s in %s", ex.getType(), ex.getText(), pathToScript));
            return NumberValue.ZERO;
        }
    }
}
