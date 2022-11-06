package com.timsystem.lib.jrt;

import com.timsystem.lib.SPKException;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class InlineCompiler {

    public static JRTFunction translate(String fname, String code) {
        File helloWorldJava = new File("com/timsystem/lib/jrt/" + fname + ".java");
        if (helloWorldJava.getParentFile().exists() || helloWorldJava.getParentFile().mkdirs()) {

            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(helloWorldJava);
                    writer.write(code);
                    writer.flush();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }

                /** Compilation Requirements *********************************************************************************************/
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                // This sets up the class path that the compiler will use.
                // I've added the .jar file that contains the DoStuff interface within in it...
                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + File.pathSeparator + "dist/InlineCompiler.jar");

                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(helloWorldJava));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                /********************************************************************************************* Compilation Requirements **/
                if (task.call()) {
                    /** Load and execute *************************************************************************************************/
                    // Create a new custom class loader, pointing to the directory that contains the compiled
                    // classes, this should point to the top of the package structure!
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
                    // Load the class from the classloader by name....
                    Class<?> loadedClass = classLoader.loadClass("com.timsystem.lib.jrt." + fname);
                    // Create a new instance...
                    Object obj = loadedClass.newInstance();
                    return (JRTFunction) obj;
                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                        System.out.println("Error type: " + diagnostic.getMessage(Locale.ENGLISH));
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
                exp.printStackTrace();
            }
        }
        throw new SPKException("JRTError", "internal error when compiling jit function");
    }

    public static JRTFunction getFunction(String fname) {
        if (JRT.stlOverrides.containsKey(fname)) {
            return JRT.stlOverrides.get(fname);
        }
        return JRT.jrts.get(fname);
    }

    public interface JRTFunction<T> {
        T execute(Object[] args);
    }

}