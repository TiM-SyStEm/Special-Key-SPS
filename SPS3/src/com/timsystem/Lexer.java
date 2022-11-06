package com.timsystem;

import com.timsystem.lib.SPKException;
import com.timsystem.lib.Token;
import com.timsystem.lib.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isDigit;
import static java.lang.Character.toLowerCase;

public final class Lexer {
    private static final String OPERATOR_CHARS = "+-*/%()[]{}=:<>,!^.;|";
    private static final Map<String, TokenType> OPERATORS;
    private static final Map<String, TokenType> KEYWORDS;

    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("%", TokenType.REMAINDER);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put(":", TokenType.COLON);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put(".", TokenType.DOT);
        OPERATORS.put("^", TokenType.POW);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("!=", TokenType.NOTEQ);
        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put(">=", TokenType.GTEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put("+=", TokenType.PLUSEQ);
        OPERATORS.put("-=", TokenType.MINUSEQ);
        OPERATORS.put("*=", TokenType.STAREQ);
        OPERATORS.put("/=", TokenType.SLASHEQ);
        OPERATORS.put("++", TokenType.INC);
        OPERATORS.put("--", TokenType.DEC);

        OPERATORS.put("=>", TokenType.MATCH);
        OPERATORS.put("::", TokenType.CC);
        OPERATORS.put(";", TokenType.SEMICOLON);
    }

    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("out", TokenType.OUT);
        KEYWORDS.put("input", TokenType.INPUT);
        KEYWORDS.put("Add", TokenType.ADD);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("not", TokenType.NOT);
        KEYWORDS.put("or", TokenType.OR);
        KEYWORDS.put("in", TokenType.IN);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("field", TokenType.FIELD);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("stop", TokenType.STOP);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("class", TokenType.STRUCT);
        KEYWORDS.put("extends", TokenType.EXTENDS);
        KEYWORDS.put("spec", TokenType.SPEC);
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("defmacro", TokenType.DEFMACRO);
        KEYWORDS.put("throw", TokenType.THROW);
        KEYWORDS.put("switch", TokenType.CASE);
        KEYWORDS.put("case", TokenType.OF);
        KEYWORDS.put("ref", TokenType.REF);
        KEYWORDS.put("try", TokenType.TRY);
        KEYWORDS.put("catch", TokenType.CATCH);
        KEYWORDS.put("private", TokenType.PRIVATE);
        KEYWORDS.put("protected", TokenType.PROTECTED);
        KEYWORDS.put("struct", TokenType.LIGHTSTRUCT);
    }


    private final String input;
    private final int length;
    private final StringBuilder buffer;
    public List<Token> tokens;
    private int pos;
    private int line = 0;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
        buffer = new StringBuilder();
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(toLowerCase(current)) != -1;
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (current == '#') comment();
            else if (isDigit(current)) tokenizeNumber();
            else if (isIdentifier(current)) tokenizeWord();
            else if (current == '"') {
                tokenizeText();
            } else if (current == '$') {
                next();
                tokenizeHexNumber();
            } else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }
        return tokens;
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.HEX_NUMBER, buffer.toString());
    }

    private void comment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeText() {
        next();// skip "
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"':
                        current = next();
                        buffer.append('"');
                        continue;
                    case '0':
                        current = next();
                        buffer.append('\0');
                        continue;
                    case 'b':
                        current = next();
                        buffer.append('\b');
                        continue;
                    case 'f':
                        current = next();
                        buffer.append('\f');
                        continue;
                    case 'n':
                        current = next();
                        buffer.append('\n');
                        continue;
                    case 'r':
                        current = next();
                        buffer.append('\r');
                        continue;
                    case 't':
                        current = next();
                        buffer.append('\t');
                        continue;
                    case 'u': // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
                        int rollbackPosition = pos;
                        while (current == 'u') current = next();
                        int escapedValue = 0;
                        for (int i = 12; i >= 0 && escapedValue != -1; i -= 4) {
                            if (isHexNumber(current)) {
                                escapedValue |= (Character.digit(current, 16) << i);
                            } else {
                                escapedValue = -1;
                            }
                            current = next();
                        }
                        if (escapedValue >= 0) {
                            buffer.append((char) escapedValue);
                        } else {
                            // rollback
                            buffer.append("\\u");
                            pos = rollbackPosition;
                        }
                        continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '"') break;
            if (current == '\0') throw new SPKException("EOFError", "Unterminated text string");
            buffer.append(current);
            current = next();
        }
        next(); // skip closing "

        addToken(TokenType.STRING, buffer.toString());
    }

    private void tokenizeWord() {
        clearBuffer();
        buffer.append(peek(0));
        char current = next();
        while (isIdentifierPart(current)) {
            buffer.append(current);
            current = next();
        }

        final String word = buffer.toString();
        if (KEYWORDS.containsKey(word))
            addToken(KEYWORDS.get(word));
        else
            addToken(TokenType.WORD, word);
    }

    private void tokenizeNumber() {
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1)
                    throw new SPKException("TypeError", String.format("invalid float number '%s'", buffer.toString() + current));
            } else if (!isDigit(current)) {
                break;
            }
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeOperator() {
        clearBuffer();
        char current = peek(0);
        while (true) {
            final String text = buffer.toString();
            if (!text.isEmpty() && !OPERATORS.containsKey(text + current)) {
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private boolean isIdentifierPart(char current) {
        return (Character.isLetterOrDigit(current) || (current == '_') || (current == '$') || (current == '?') || (current == '!'));
    }

    private boolean isIdentifier(char current) {
        return (Character.isLetter(current) || (current == '_'));
    }

    private void clearBuffer() {
        buffer.setLength(0);
    }

    private char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private char next() {
        pos++;
        char result = peek(0);
        if (result == '\n') line++;
        return result;
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text, line));
    }
}

