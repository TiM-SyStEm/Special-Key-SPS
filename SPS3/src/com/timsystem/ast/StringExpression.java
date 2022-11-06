package com.timsystem.ast;

import com.timsystem.lib.Handler;
import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

public class StringExpression implements Expression {

    private String current;
    private StringBuilder result;
    private int line;
    private int length;
    private int pos;
    private String str;

    public StringExpression(String str, int line, String current, int pos) {
        this.str = str;
        this.line = line;
        this.current = current;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        this.pos = 0;
        this.result = new StringBuilder();
        this.length = str.length();
        try {
            lex();
        } catch (OutOfMemoryError ex) {
            this.result = new StringBuilder(str);
        }
        return new StringValue(result.toString());
    }

    private void lex() {
        while (pos < length) {
            char c = peek(0);
            if (c == '#') {
                c = next();
                interpolate();
            } else if (c == '\\') {
                c = next();
                result.append(c);
            } else {
                result.append(c);
                next();
            }
        }
    }

    private void interpolate() {
        char c = next();
        StringBuilder buffer = new StringBuilder();
        while (c != '}') {
            buffer.append(c);
            c = next();
        }
        next();
        result.append(Handler.returnHandle(buffer.toString(), "<string interpolation>"));
    }

    private char peek(int relativePos) {
        int p = relativePos + pos;
        if (p >= length) return '\0';
        return str.charAt(p);
    }

    private char next() {
        pos++;
        return peek(0);
    }

    @Override
    public String toString() {
        return str;
    }

    public String getCurrent() {
        return current;
    }

    public StringBuilder getResult() {
        return result;
    }

    public int getLine() {
        return line;
    }

    public int getLength() {
        return length;
    }

    public int getPos() {
        return pos;
    }

    public String getStr() {
        return str;
    }
}
