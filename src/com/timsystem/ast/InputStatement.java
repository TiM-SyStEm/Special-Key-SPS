package com.timsystem.ast;

import com.timsystem.runtime.StringValue;
import com.timsystem.runtime.Value;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InputStatement implements Expression, Statement{
    private final Expression expr;

    public InputStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        System.out.print(expr.eval().raw());
        Scanner sc = new Scanner(System.in);
        return new StringValue(sc.nextLine().getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public String toString() {
        return "input : " + expr;
    }
}
