package com.timsystem.ast;

import com.timsystem.lib.SPKException;
import com.timsystem.runtime.ArrayValue;
import com.timsystem.runtime.NumberValue;
import com.timsystem.runtime.Value;
import com.timsystem.runtime.Variables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class MatchExpression implements Expression, Statement {

    public final Expression expression;
    public final List<Pattern> patterns;

    public MatchExpression(Expression expression, List<Pattern> patterns) {
        this.expression = expression;
        this.patterns = patterns;
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        final Value value = expression.eval();
        for (Pattern p : patterns) {
            if (p instanceof ConstantPattern) {
                final ConstantPattern pattern = (ConstantPattern) p;
                if (match(value, pattern.constant) && optMatches(p)) {
                    return evalResult(p.result);
                }
            }
            if (p instanceof VariablePattern) {
                final VariablePattern pattern = (VariablePattern) p;
                if (pattern.variable.equals("_")) return evalResult(p.result);

                if (Variables.isExists(pattern.variable)) {
                    if (match(value, Variables.get(pattern.variable)) && optMatches(p)) {
                        return evalResult(p.result);
                    }
                } else {
                    Variables.define(pattern.variable, value);
                    if (optMatches(p)) {
                        final Value result = evalResult(p.result);
                        Variables.del(pattern.variable);
                        return result;
                    }
                    Variables.del(pattern.variable);
                }
            }
            if ((value instanceof ArrayValue) && (p instanceof ListPattern)) {
                final ListPattern pattern = (ListPattern) p;
                if (matchListPattern((ArrayValue) value, pattern)) {
                    // Clean up variables if matched
                    final Value result = evalResult(p.result);
                    for (String var : pattern.parts) {
                        Variables.del(var);
                    }
                    return result;
                }
            }
            if ((value instanceof ArrayValue) && (p instanceof TuplePattern)) {
                final TuplePattern pattern = (TuplePattern) p;
                if (matchTuplePattern((ArrayValue) value, pattern) && optMatches(p)) {
                    return evalResult(p.result);
                }
            }
        }
        throw new SPKException("BadMatchExpression", "Bad pattern or bad cases");
    }

    private boolean matchTuplePattern(ArrayValue array, TuplePattern p) {
        if (p.values.size() != array.length()) return false;

        final int size = array.length();
        for (int i = 0; i < size; i++) {
            final Expression expr = p.values.get(i);
            if ( (expr != TuplePattern.ANY) && (expr.eval().equals(array.get(i))) ) {
                return false;
            }
        }
        return true;
    }

    private boolean matchListPattern(ArrayValue array, ListPattern p) {
        final List<String> parts = p.parts;
        final int partsSize = parts.size();
        final int arraySize = array.length();
        switch (partsSize) {
            case 0: // match [] { case []: ... }
                if ((arraySize == 0) && optMatches(p)) {
                    return true;
                }
                return false;

            case 1: // match arr { case [x]: x = arr ... }
                final String variable = parts.get(0);
                Variables.define(variable, array);
                if (optMatches(p)) {
                    return true;
                }
                Variables.del(variable);
                return false;

            default: { // match arr { case [...]: .. }
                if (partsSize == arraySize) {
                    // match [0, 1, 2] { case [a::b::c]: a=0, b=1, c=2 ... }
                    return matchListPatternEqualsSize(p, parts, partsSize, array);
                } else if (partsSize < arraySize) {
                    // match [1, 2, 3] { case [head :: tail]: ... }
                    return matchListPatternWithTail(p, parts, partsSize, array, arraySize);
                }
                return false;
            }
        }
    }

    private boolean matchListPatternEqualsSize(ListPattern p, List<String> parts, int partsSize, ArrayValue array) {
        // Set variables
        for (int i = 0; i < partsSize; i++) {
            Variables.define(parts.get(i), array.get(i));
        }
        if (optMatches(p)) {
            // Clean up will be provided after evaluate result
            return true;
        }
        // Clean up variables if no match
        for (String var : parts) {
            Variables.del(var);
        }
        return false;
    }

    private boolean matchListPatternWithTail(ListPattern p, List<String> parts, int partsSize, ArrayValue array, int arraySize) {
        // Set element variables
        final int lastPart = partsSize - 1;
        for (int i = 0; i < lastPart; i++) {
            Variables.define(parts.get(i), array.get(i));
        }
        // Set tail variable
        final ArrayValue tail = new ArrayValue(arraySize - partsSize + 1);
        for (int i = lastPart; i < arraySize; i++) {
            tail.set(i - lastPart, array.get(i));
        }
        Variables.define(parts.get(lastPart), tail);
        // Check optional condition
        if (optMatches(p)) {
            // Clean up will be provided after evaluate result
            return true;
        }
        // Clean up variables
        for (String var : parts) {
            Variables.del(var);
        }
        return false;
    }

    private boolean match(Value value, Value constant) {
        return value.equals(constant);
    }

    private boolean optMatches(Pattern pattern) {
        if (pattern.optCondition == null) return true;
        return pattern.optCondition.eval() != NumberValue.ZERO;
    }

    private Value evalResult(Statement s) {
        try {
            s.execute();
        } catch (ReturnStatement ret) {
            return ret.getReturnValue();
        }
        return NumberValue.ZERO;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("match ").append(expression).append(" {");
        for (Pattern p : patterns) {
            sb.append("\n  case ").append(p);
        }
        sb.append("\n}");
        return sb.toString();
    }

    public abstract static class Pattern {
        public Statement result;
        public Expression optCondition;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (optCondition != null) {
                sb.append(" if ").append(optCondition);
            }
            sb.append(": ").append(result);
            return sb.toString();
        }
    }

    public static class ConstantPattern extends Pattern {
        Value constant;

        public ConstantPattern(Value pattern) {
            this.constant = pattern;
        }

        @Override
        public String toString() {
            return constant.toString().concat(super.toString());
        }
    }

    public static class VariablePattern extends Pattern {
        public String variable;

        public VariablePattern(String pattern) {
            this.variable = pattern;
        }

        @Override
        public String toString() {
            return variable.concat(super.toString());
        }
    }

    public static class ListPattern extends Pattern {
        List<String> parts;

        public ListPattern() {
            this(new ArrayList<>());
        }

        ListPattern(List<String> parts) {
            this.parts = parts;
        }

        public void add(String part) {
            parts.add(part);
        }

        @Override
        public String toString() {
            final Iterator<String> it = parts.iterator();
            if (it.hasNext()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("[").append(it.next());
                while (it.hasNext()) {
                    sb.append(" :: ").append(it.next());
                }
                sb.append("]").append(super.toString());
                return sb.toString();
            }
            return "[]".concat(super.toString());
        }
    }

    public static class TuplePattern extends Pattern {
        public List<Expression> values;

        public TuplePattern() {
            this(new ArrayList<Expression>());
        }

        public TuplePattern(List<Expression> parts) {
            this.values = parts;
        }

        public void addAny() {
            values.add(ANY);
        }

        public void add(Expression value) {
            values.add(value);
        }

        @Override
        public String toString() {
            final Iterator<Expression> it = values.iterator();
            if (it.hasNext()) {
                final StringBuilder sb = new StringBuilder();
                sb.append('(').append(it.next());
                while (it.hasNext()) {
                    sb.append(", ").append(it.next());
                }
                sb.append(')').append(super.toString());
                return sb.toString();
            }
            return "()".concat(super.toString());
        }

        private static final Expression ANY = new Expression() {
            @Override
            public Value eval() {
                return NumberValue.ONE;
            }


            @Override
            public String toString() {
                return "_".concat(super.toString());
            }
        };
    }
}