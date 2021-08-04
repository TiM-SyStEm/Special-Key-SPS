package com.timsystem.lib;

public enum TokenType {
    STAR,
    SLASH,
    PLUS,
    MINUS,
    NUMBER,
    STRING,
    EOF,
    HEX_NUMBER,
    LPAREN,
    RPAREN,
    EQ,
    COLON,

    // Keywords
    OUT,
    ADD,
    VAR,

    WORD
}
