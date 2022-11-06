package com.timsystem.lib.jrt;

import com.timsystem.ast.*;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.jaxen.expr.Expr;

import java.util.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class JRT {


    public static HashMap<String, InlineCompiler.JRTFunction> jrts = new HashMap<>();
    public static Map<String, Value> jrt = new HashMap<>();
    public static HashMap<String, ArrayValue> argsTypes = new HashMap<>();

    public static void inject() {
        jrt.put("translate", new FunctionValue((args) -> {
            StringBuilder sb = new StringBuilder();
            String fname = args[0].toString();
            sb.append("package com.timsystem.lib.jrt;\n");
            sb.append("import java.util.ArrayList;\n");
            sb.append("import java.util.List;\n");
            sb.append("public class " + fname + " implements com.timsystem.lib.jrt.InlineCompiler.JRTFunction<" + args[2].toString() + "> " + " {\n");
            sb.append("     public " + args[2].toString() + " execute(Object[] args) {\n");
            sb.append("     " + generateArgsAssignment(fname, (ArrayValue) args[1]) + "\n");
            sb.append("     " + translateAST((UserDefinedFunction) Functions.get(fname)) + "\n");
            sb.append("     }\n");
            sb.append("}");
            InlineCompiler.JRTFunction jrtFunction = InlineCompiler.translate(fname, sb.toString());
            jrts.put(fname, jrtFunction);
            argsTypes.put(fname, (ArrayValue) args[1]);
            return args[0];
        }));

        jrt.put("call", new FunctionValue((args -> {
            String fname = args[0].toString();
            if (!jrts.containsKey(fname))
                throw new SPKException("JRTError", "jrt function '" + fname + "' doesn't exists!");
            InlineCompiler.JRTFunction jrtFunction = jrts.get(fname);
            Object[] callArgs = new Object[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                callArgs[i - 1] = transormArg(args[i]);
            }
            Object returnValue = jrtFunction.execute(callArgs);
            return transformObject(returnValue);
        })));

        newClass("jrt", new ArrayList<>(), jrt);
    }

    public static String generateArgsAssignment(String fname, ArrayValue args) {
        StringBuilder sb = new StringBuilder();
        int argsCount = 0;
        UserDefinedFunction uf = (UserDefinedFunction) Functions.get(fname);
        for (Value type : args.array()) {
            String t = type.toString();
            String argName = uf.arguments.get(argsCount);
            sb.append("     ");
            if (t.equals("string")) sb.append("String " + argName + " = (String) args[" + argsCount + "];\n");
            else if (t.equals("int")) sb.append("int " + argName + " = (Integer) args[" + argsCount + "];\n");
            else if (t.equals("float")) sb.append("double " + argName + " = (Double) args[" + argsCount + "];\n");
            argsCount++;
        }
        return sb.toString();
    }

    public static String translateAST(UserDefinedFunction uf) {
        BlockStatement ub = (BlockStatement) uf.body;
        return translateAST(ub);
    }

    public static String translateAST(BlockStatement stm) {
        String acc = "";
        for (Statement st : stm.getStatements()) {
            acc += "     " + translateAST(st) + ";\n";
        }
        return acc;
    }

    public static String translateAST(Statement st) {
        if (st instanceof OutStatement) {
            return translateAST((OutStatement) st);
        } else if (st instanceof ReturnStatement) {
            return translateAST((ReturnStatement) st);
        } else if (st instanceof ExprStatement) {
            return translateAST((ExprStatement) st);
        } else if (st instanceof IfStatement) {
            return translateAST((IfStatement) st);
        } else if (st instanceof BlockStatement) {
            return translateAST((BlockStatement) st);
        } else if (st instanceof AssignmentStatement) {
            return translateAST((AssignmentStatement) st);
        } else if (st instanceof Expression) {
            return translateAST((Expression) st);
        } else if (st instanceof ForStatement) {
            return translateAST((ForStatement) st);
        }
        return "";
    }

    public static String translateAST(Expression expr) {
        if (expr instanceof StringExpression) {
            return translateAST((StringExpression) expr);
        } else if (expr instanceof BinaryExpression) {
            return translateAST((BinaryExpression) expr);
        } else if (expr instanceof ValueExpression) {
            return translateAST((ValueExpression) expr);
        } else if (expr instanceof VariableExpression) {
            return translateAST((VariableExpression) expr);
        } else if (expr instanceof FunctionalExpression) {
            return translateAST((FunctionalExpression) expr);
        } else if (expr instanceof ConditionalExpression) {
            return translateAST((ConditionalExpression) expr);
        } else if (expr instanceof AssignmentExpression) {
            return translateAST((AssignmentExpression) expr);
        } else if (expr instanceof SuffixExpression) {
            return translateAST((SuffixExpression) expr);
        } else if (expr instanceof ArrayExpression) {
            return translateAST((ArrayExpression) expr);
        }
        return "";
    }

    public static String translateAST(ArrayExpression expr) {
        String acc = "com.timsystem.lib.jrt.JRT.createArrayList(new Object[] {";
        for (int i = 0; i < expr.getElements().size(); i++) {
            acc += translateAST(expr.getElements().get(i)) + (i == expr.getElements().size() - 1 ? "" : ", ");
        }
        acc += "})";
        return acc;
    }


    public static String translateAST(ForStatement st) {
        String acc = "";
        acc += translateAST(st.getInitialization()) + ";\n";
        acc += "while (" + translateAST(st.getTermination()) + ") {\n";
        acc += translateAST(st.getStatement());
        acc += translateAST(st.getIncrement()) + ";";
        acc += "}";
        return acc;
    }

    public static String translateAST(SuffixExpression expr) {
        String var = expr.getExpr().toString();
        return var + " = (double) " + var + expr.getOperation() + " + 1";
    }

    public static String translateAST(AssignmentExpression expr) {
        return "Object " + translateAST((Expression) expr.target) + " = " + translateAST(expr.expression);
    }
    public static String translateAST(AssignmentStatement st) {
        return "Object " + st.getVariable() + " = " + translateAST(st.getExpression());
    }

    public static String translateAST(FunctionalExpression expr) {
        String acc = "com.timsystem.lib.jrt.InlineCompiler.getFunction(\"";
        acc += translateAST(expr.functionExpr) + "\").";
        acc += "execute";
        acc += "(";
        acc += "new Object[] {";
        for (int i = 0; i < expr.arguments.size(); i++) {
            acc += translateAST(expr.arguments.get(i)) + ((i == expr.arguments.size() - 1) ? "" : ", ");
        }
        acc += "}";
        acc += ")";
        return acc;
    }

    public static String translateAST(IfStatement st) {
        String acc = "";
        acc += "if (";
        acc += translateAST(st.getExpression()) + ")";
        if (st.getIfStatement() instanceof BlockStatement) {
            acc += " {\n" + translateAST(st.getIfStatement()) + "\n }";
        } else {
            acc += " {\n" + translateAST(st.getIfStatement()) + ";\n }";
        }
        if (st.getElseStatement() != null) {
            if (st.getElseStatement() instanceof BlockStatement) {
                acc += "    else { " + translateAST(st.getElseStatement()) + "} \n";
            } else {
                acc += "    else { " + translateAST(st.getElseStatement()) + ";} \n";
            }
        }
        return acc;
    }

    public static String numberify(Expression expr) {
        if (expr instanceof StringExpression) {
            return translateAST(expr);
        }
        return "(double) " + translateAST(expr);
    }


    public static String translateAST(ExprStatement st) {
        return translateAST(st.getExpr());
    }

    public static String translateAST(ReturnStatement st) {
        return "return " + translateAST(st.getExpression());
    }

    public static String translateAST(VariableExpression expr) {
        return expr.name;
    }

    public static String translateAST(ValueExpression expr) {
        return transoformValueExpr(expr.eval());
    }

    public static String translateAST(BinaryExpression expr) {
        return numberify(expr.getExpr1()) + expr.getOperation() + numberify(expr.getExpr2());
    }

    public static String translateAST(ConditionalExpression expr) {
        return translateAST(expr.getExpr1()) + conditionalOperator(expr.getOperation()) + translateAST(expr.getExpr2());
    }

    public static String translateAST(OutStatement st) {
        return "System.out.println(" + translateAST(st.getExpr()) + ")";
    }

    public static String translateAST(StringExpression expr) {
        return "\"" + expr.getStr() + "\"";
    }

    public static Object transormArg(Value value) {
        if (value instanceof NumberValue) {
            return ((NumberValue) value).asNumber();
        } else if (value instanceof StringValue) {
            return ((StringValue) value).toString();
        }
        return null;
    }

    public static String transoformValueExpr(Value expr) {
        return expr.toString();
    }

    public static Value transformObject(Object object) {
        if (object instanceof String) {
            return new StringValue(((String) object).toString());
        } else if (object instanceof Number) {
            return new NumberValue(((Number) object).doubleValue());
        }
        return new NumberValue(-1);
    }

    public static String conditionalOperator(ConditionalExpression.Operator operator) {
        switch (operator) {
            case GT -> { return ">"; }
            case LT -> { return "<"; }
            case GTEQ -> { return ">="; }
            case LTEQ -> { return "<="; }
            case EQUALS -> { return "=="; }
            case NOT_EQUALS -> { return "!="; }
            case OR -> { return "||"; }
            case AND -> { return "&&"; }
        }
        return "==";
    }

    public static ArrayList<Object> createArrayList(Object[] values) {
        return new ArrayList<Object>(Arrays.asList(values));
    }

    private static void newClass(String name, List<String> structArgs, Map<String, Value> targets) {
        ClassValue result = new ClassValue(name, structArgs);
        for (Map.Entry<String, Value> entry : targets.entrySet()) {
            Value expr = entry.getValue();
            result.setField(entry.getKey(), expr);
        }

        Variables.set(name, result);
    }

}
