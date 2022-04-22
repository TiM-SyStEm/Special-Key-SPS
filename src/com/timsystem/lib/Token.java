package com.timsystem.lib;

public class Token {
    private TokenType type;
    private String text;
    private int line;

    public Token(TokenType type, String text, int line) {
        this.type = type;
        this.text = text;
        this.line = line;
    }

    public Token() {
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", type, text, line);
    }
}
