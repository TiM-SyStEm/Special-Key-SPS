package com.timsystem.lib.jrt;

import com.timsystem.ast.*;
import com.timsystem.lib.CallStack;
import com.timsystem.lib.Function;
import com.timsystem.lib.SPKException;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import org.jsoup.select.Evaluator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class JRT {


    public static HashMap<String, InlineCompiler.JRTFunction> jrts = new HashMap<>();
    public static HashMap<String, InlineCompiler.JRTFunction> stlOverrides = new HashMap<>();
    public static Map<String, Value> jrt = new HashMap<>();
    public static HashMap<String, ArrayValue> argsTypes = new HashMap<>();

    public static Scanner SCANNER = new Scanner(System.in);

    public static void inject() {

        imeplementStandardLibrary();
        implementOverrides();

        Variables.define("int", new StringValue("float"));
        Variables.define("float", new StringValue("float"));
        Variables.define("array", new StringValue("array"));

        jrt.put("translate", new FunctionValue((args) -> {
            StringBuilder sb = new StringBuilder();
            String fname = args[0].toString();
            String rtype = args.length == 3 ? interpretReturnType(args[2].toString()) : "Object";
            sb.append("package com.timsystem.lib.jrt;\n");
            sb.append("import java.util.ArrayList;\n");
            sb.append("import java.util.List;\n");
            sb.append("public class " + fname + " implements com.timsystem.lib.jrt.InlineCompiler.JRTFunction<" + rtype + "> " + " {\n");
            sb.append("     public " + rtype + " execute(Object[] args) {\n");
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
            try {
                if (!jrts.containsKey(fname))
                    throw new SPKException("JRTError", "jrt function '" + fname + "' doesn't exists!");
                InlineCompiler.JRTFunction jrtFunction = jrts.get(fname);
                Object[] callArgs = new Object[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    callArgs[i - 1] = transormArg(args[i]);
                }
                Object returnValue = jrtFunction.execute(callArgs);
                return transformObject(returnValue);
            } catch (SPKException ex) {
                throw new SPKException("JrtRuntimeError", "got SPKException while executing jrt function '" + fname + "'\n     " + ex.getType() + ": " + ex.getText());
            }
        })));

        jrt.put("cached", new FunctionValue((args -> {
            String fname = args[0].toString();
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
                // Load the class from the classloader by name....
                Class<?> loadedClass = classLoader.loadClass("com.timsystem.lib.jrt." + fname);
                // Create a new instance...
                Object obj = loadedClass.newInstance();
                jrts.put(fname, (InlineCompiler.JRTFunction) obj);
                return new NumberValue(1);
            } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        })));

        newClass("jrt", new ArrayList<>(), jrt);
    }

    public static void imeplementStandardLibrary() {
        for (Map.Entry<String, Function> pair : Functions.functions.entrySet()) {
            // System.out.println(pair.getKey());
            jrts.put(pair.getKey(), new InlineCompiler.JRTFunction<Object>() {
                @Override
                public Object execute(Object[] args) {
                    Value[] vargs = new Value[args.length];
                    for (int i = 0; i < vargs.length; i++) {
                        vargs[i] = transformObject(args[i]);
                    }
                    Value result = pair.getValue().execute(vargs);
                    return transormArg(result);
                }
            });
        }

        for (Map.Entry<String, Value> pair : Variables.variables().entrySet()) {
            if (pair.getValue() instanceof ClassValue) {
                ClassValue cv = (ClassValue) pair.getValue();
                for (Map.Entry<String, Value> field : cv.fields.entrySet()) {
                    stlOverrides.put(pair.getKey() + "." + field.getKey(), new InlineCompiler.JRTFunction() {
                        @Override
                        public Object execute(Object[] args) {
                            Value f = field.getValue();
                            if (f instanceof Function) {
                                return (InlineCompiler.JRTFunction<Object>) pargs -> {
                                    try {
                                        Value[] vargs = new Value[pargs.length];
                                        for (int i = 0; i < vargs.length; i++) {
                                            vargs[i] = transformObject(pargs[i]);
                                        }
                                        CallStack.enter(pair.getKey() + "." + field.getKey() + "(jrt implementation)", (Function) f);
                                        Object c = transormArg(((FunctionValue) f).getValue().execute(vargs));
                                        CallStack.exit();
                                        return c;
                                    } catch (SPKException ex) {
                                        throw new SPKException("JrtSTLRuntimeError",
                                                "got SPKException while executing jrt port of stl function '" + pair.getKey() + "." + field.getKey() + "'\n     " +
                                                        ex.getType() + ": " + ex.getText());
                                    }
                                };
                            }
                            return transormArg(f);
                        }
                    });
                }
            }
        }
    }

    public static void implementOverrides() {
        jrts.put("arrayAppend", new InlineCompiler.JRTFunction() {
            @Override
            public Object execute(Object[] args) {
                ArrayList<Object> objs = (ArrayList<Object>) args[0];
                objs.add(args[1]);
                return args[0];
            }
        });
        stlOverrides.put("jrt.reference", args -> (InlineCompiler.JRTFunction) args1 -> {
            return jrts.get(args1[0].toString());
        });
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
            else if (t.equals("array")) sb.append("ArrayList<Object> " + argName + " = (ArrayList<Object>) args[" + argsCount + "];\n");
            argsCount++;
        }
        return sb.toString();
    }

    public static String interpretReturnType(String type) {
        if (type.equals("int") || type.equals("float")) {
            return "Double";
        } else if (type.equals("array")) {
            return "ArrayList<Object>";
        } else if (type.equals("string")) {
            return "String";
        }
        return type;
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
        } else if (st instanceof StdInput) {
            return translateAST((StdInput) st);
        } else if (st instanceof AssignmentStatement) {
            return translateAST((AssignmentStatement) st);
        } else if (st instanceof ReturnStatement) {
            return translateAST((ReturnStatement) st);
        } else if (st instanceof ExprStatement) {
            return translateAST((ExprStatement) st);
        } else if (st instanceof IfStatement) {
            return translateAST((IfStatement) st);
        } else if (st instanceof BlockStatement) {
            return translateAST((BlockStatement) st);
        } else if (st instanceof Expression) {
            return translateAST((Expression) st);
        } else if (st instanceof ForStatement) {
            return translateAST((ForStatement) st);
        } else if (st instanceof ArrayAssignmentStatement) {
            return translateAST((ArrayAssignmentStatement) st);
        } else if (st instanceof WhileStatement) {
            return translateAST((WhileStatement) st);
        } else if (st instanceof StopStatement) {
            return "break";
        } else if (st instanceof ForeachArrayStatement) {
            return translateAST((ForeachArrayStatement) st);
        } else if (st instanceof TryCatchStatement) {
            return translateAST((TryCatchStatement) st);
        } else if (st instanceof FieldStatement) {
            return translateAST((FieldStatement) st);
        } else if (st instanceof ThrowStatement) {
            return translateAST((ThrowStatement) st);
        }
        throw new SPKException("JrtTranslateError", "'" + st.getClass().getSimpleName() + "' is not supported by jrt now");
    }

    public static String translateAST(ThrowStatement st) {
        return "throw new com.timsystem.lib.SPKException(\"" + st.getType() + "\", " + translateAST(st.getExpr()) + ")";
    }

    public static String translateAST(FieldStatement st) {
        return "Object " + st.getVariable() + " = new Object()";
    }
    public static String translateAST(TryCatchStatement st) {
        String acc = "try {\n";
        acc += translateAST(st.getTryStatement());
        acc += "\n} catch (Exception ex) {\n";
        acc += "Object ex_type = ex.getClass().getSimpleName();\n";
        acc += "Object ex_text = ex.getMessage();\n";
        acc += translateAST(st.getCatchStatement());
        acc += "}\n";
        return acc;
    }

    public static String translateAST(ForeachArrayStatement st) {
        String acc = "for (Object " + st.variable + " : (ArrayList<Object>) " + translateAST(st.container) + ") {\n";
        acc += translateAST(st.body) + "}\n";
        return acc;
    }

    public static String translateAST(WhileStatement st) {
        String acc = "while (" + translateAST(st.getCondition()) + ") {\n";
        acc += translateAST(st.getStatement());
        acc += "}\n";
        return acc;
    }

    public static String translateAST(ArrayAssignmentStatement st) {
        String target = convertToInteger(translateAST(st.getArray().getIndices().get(st.getArray().getIndices().size() - 1)));
        return "com.timsystem.lib.jrt.JRT.setArrayElement(" + st.getArray().getVariable() + ", " + "(int) " + target + ", " + translateAST(st.getExpression()) + ")";
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
        } else if (expr instanceof ArrayAccessExpression) {
            return translateAST((ArrayAccessExpression) expr);
        } else if (expr instanceof ContainerAccessExpression) {
            return translateAST((ContainerAccessExpression) expr);
        } else if (expr instanceof UnaryExpression) {
            return translateAST((UnaryExpression) expr);
        } else if (expr instanceof StdInput) {
            return translateAST((StdInput) expr);
        } else if (expr instanceof FunctionReferenceExpression) {
            return translateAST((FunctionReferenceExpression) expr);
        }
        throw new SPKException("JrtTranslateError", "'" + expr.getClass().getSimpleName() + "' is not supported by jrt now");
    }

    public static String translateAST(FunctionReferenceExpression expr) {
        return call("getReference") + "(\"" + expr.name + "\")";
    }

    public static String translateAST(StdInput expr) {
        return call("readInput") + "(" + translateAST(expr.getExpr()) + ")";
    }

    public static String translateAST(UnaryExpression expr) {
        return expr.getOperation() + translateAST(expr.getExpr1());
    }

    public static String translateAST(ContainerAccessExpression expr) {
        String index = translateAST(expr.indices.get(0));
        if (stlOverrides.containsKey(expr.root + "." + index)) {
            return "com.timsystem.lib.jrt.InlineCompiler.getFunction" + "(\"" + expr.root + "." + index + "\").execute(new Object[] {})";
        }
        return "com.timsystem.lib.jrt.JRT.getArrayElement((ArrayList<Object>) " + translateAST(expr.root) + ", " + index + ")";
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
        return var + " = (double) " + var + expr.getOperation() + " 1.0";
    }

    public static String translateAST(AssignmentExpression expr) {
        return "Object " + translateAST((Expression) expr.target) + " = (Object)" + translateAST(expr.expression);
    }
    public static String translateAST(AssignmentStatement st) {
        return "" + st.getVariable() + " = " + translateAST(st.getExpression());
    }

    public static String translateAST(FunctionalExpression expr) {
        if (expr.functionExpr instanceof ContainerAccessExpression) {
            String acc = "((com.timsystem.lib.jrt.InlineCompiler.JRTFunction) ";
            acc += translateAST(expr.functionExpr);
            acc += ")";
            acc += ".execute(new Object[] {";
            for (int i = 0; i < expr.arguments.size(); i++) {
                acc += translateAST(expr.arguments.get(i)) + ((i == expr.arguments.size() - 1) ? "" : ", ");
            }
            acc += "})";
            return acc;
        }
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
        String s = translateAST(expr);
        return "((Double)" + s + ")";
    }

    public static String call(String fname) {
        return "com.timsystem.lib.jrt.JRT." + fname;
    }

    public static Double asDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        return Double.parseDouble(o.toString());
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
        return call("binaryOperation") + "(" + translateAST(expr.getExpr1()) + ", " + translateAST(expr.getExpr2()) + ", '" + expr.getOperation() + "')";
    }

    public static String translateAST(ConditionalExpression expr) {
        return numberify(expr.getExpr1()) + conditionalOperator(expr.getOperation()) + numberify(expr.getExpr2());
    }

    public static String translateAST(OutStatement st) {
        return "System.out.println(String.valueOf(" + translateAST(st.getExpr()) + "))";
    }

    public static String translateAST(StringExpression expr) {
        return "\"" + expr.getStr() + "\"";
    }

    public static Object transormArg(Value value) {
        if (value instanceof NumberValue) {
            return (Double) value.asNumber();
        } else if (value instanceof StringValue) {
            return ((StringValue) value).toString();
        } else if (value instanceof ArrayValue) {
            ArrayList<Object> objects = new ArrayList<>();
            ArrayValue arrayValue = (ArrayValue) value;
            for (Value v : arrayValue.array()) {
                objects.add(transormArg(v));
            }
            return objects;
        }
        return null;
    }

    public static String transoformValueExpr(Value expr) {
        if (expr instanceof NumberValue) {
            return "(Double) " + expr.asNumber();
        }
        return expr.toString();
    }

    public static Value transformObject(Object object) {
        if (object instanceof String) {
            return new StringValue(((String) object).toString());
        } else if (object instanceof Number) {
            return new NumberValue(((Number) object).doubleValue());
        } else if (object instanceof ArrayList) {
            ArrayList<Object> ol = (ArrayList<Object>) object;
            ArrayValue value = new ArrayValue(0);
            for (Object o : ol) {
                value.append(transformObject(o));
            }
            return value;
        } else if (object instanceof InlineCompiler.JRTFunction) {
            return new FunctionValue((pargs -> {
                Object[] vargs = new Object[pargs.length];
                for (int i = 0; i < vargs.length; i++) {
                    vargs[i] = transormArg(pargs[i]);
                }
                CallStack.enter("(jrt reference function)", args -> null);
                Value v = transformObject(((InlineCompiler.JRTFunction) object).execute(vargs));
                CallStack.exit();
                return v;
            }));
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

    public static void setArrayElement(ArrayList<Object> object, Object index, Object value) {
        object.set(((Number) index).intValue(), value);
    }

    public static Object getArrayElement(ArrayList<Object> array, Object index) {
        if (index instanceof Number) {
            return array.get(((Number) index).intValue());
        }
        return "out of bounds!";
    }

    public static Object readInput(Object prompt) {
        System.out.print(prompt.toString());
        return SCANNER.nextLine();
    }

    public static String convertToInteger(String index) {
        return "com.timsystem.lib.jrt.JRT.convertToRuntimeInt(" + index + ")";
    }

    public static Integer convertToRuntimeInt(Object o) {
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        return 0;
    }

    public static Object binaryOperation(Object o, Object o1, char op) {
        if (o instanceof String || o1 instanceof String) {
            switch (op) {
                case '+': return o.toString() + o1.toString();
                default: return "internal error";
            }
        } else {
            double a = ((Number) o).doubleValue();
            double b = ((Number) o1).doubleValue();
            switch (op) {
                case '+': return (Double) (a + b);
                case '-': return (Double) (a - b);
                case '*': return (Double) (a * b);
                case '/': return (Double) (a / b);
                default: return (Double) (-1.0);
            }
        }
    }

    public static boolean canNumberify(Object o) {
        return o instanceof Number;
    }

    public static InlineCompiler.JRTFunction getReference(String name) {
        return jrts.get(name);
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
