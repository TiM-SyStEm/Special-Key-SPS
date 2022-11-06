package com.timsystem.ast;

import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StdInput implements Expression, Statement {

    private static final Scanner sc = new Scanner(System.in);
    private final Expression expr;

    public StdInput(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        System.out.print(expr.eval());
        return new StringValue(sc.nextLine());
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "input : " + expr;
    }
}