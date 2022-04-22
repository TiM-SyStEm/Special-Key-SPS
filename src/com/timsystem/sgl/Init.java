package com.timsystem.sgl;

import com.timsystem.lib.Arguments;
import com.timsystem.runtime.*;
import com.timsystem.runtime.ClassValue;
import com.timsystem.sgl.rendering.Shader;
import com.timsystem.sgl.rendering.Texture;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46C;
import java.nio.FloatBuffer;
import java.util.*;

public class Init {
    // Special Graphics Library
    // use accelerate 3d with OpenGL 4.6
    public static void inject() {
        Map<String, Value> sgl = new HashMap<>();
        Variables.set("SGL_NEARST", new NumberValue(GL46C.GL_NEAREST));
        Variables.set("SGL_LINEAR", new NumberValue(GL46C.GL_LINEAR));
        Variables.set("SGL_VER", new StringValue("1.0-beta"));
        sgl.put("createWindow", new FunctionValue((args -> {
            String title;
            int width = 640;
            int height = 480;
            switch (args.length) {
                case 2:
                    title = args[0].raw().toString();
                    new SGL().run(width, height, title, args[1]);
                    break;
                case 4:
                    width = (int) args[0].asNumber();
                    height = (int) args[1].asNumber();
                    title = args[2].raw().toString();
                    new SGL().run(width, height, title, args[3]);
                    break;
            }
            return new NumberValue(Window.id);
        })));
        sgl.put("windowPos", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            Window.xpos = args[0].asInt(); Window.ypos = args[1].asInt();
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("windowCenterPos", new FunctionValue((args -> {
            Window.isCenterPos = true;
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("windowSizeLimits", new FunctionValue((args -> {
            Arguments.check(4, args.length);
            GLFW.glfwSetWindowSizeLimits(Window.id, args[0].asInt(), args[1].asInt(), args[2].asInt(), args[3].asInt());
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("viewEvents", new FunctionValue((args -> {
            GLFW.glfwPollEvents();
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("swapBuffers", new FunctionValue((args -> {
            GLFW.glfwSwapBuffers(Window.id);
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("windowDestroy", new FunctionValue((args -> {
            GLFW.glfwDestroyWindow(Window.id);
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("clearColor", new FunctionValue((args -> {
            Arguments.check(4, args.length);
            int red = args[0].asInt();
            int green = args[1].asInt();
            int blue = args[2].asInt();
            int alpha = args[3].asInt();
            GL46C.glClearColor(red, green, blue, alpha);
            GL46C.glClear(GL46C.GL_COLOR_BUFFER_BIT | GL46C.GL_DEPTH_BUFFER_BIT);
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("drawQuads", new FunctionValue((args -> {
            Value[] raw_v_pos;
            float[] v_pos;
            int vaoId;
            int vboId;
            FloatBuffer fBuffer;

            switch (args.length){
                case 1:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    //создаём атрибут для вершины, указывая id, тип данных.
                    //включаем созданный выше 0 атрибут вершины
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_QUADS, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glBindVertexArray(vaoId);
                    break;
                case 2:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }
                    Value[] raw_v_colors = ((ArrayValue)args[1]).array();
                    float[] v_colors = new float[raw_v_colors.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_colors.length; i++){
                        v_colors[i] = (float)raw_v_colors[i].asNumber();
                    }

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    // буффер цветов
                    int vertexCBuffer = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vertexCBuffer);
                    FloatBuffer fBuffer2 = MemoryManagement.floatBufStoreData(v_colors);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer2, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glVertexAttribPointer(1, 4, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_QUADS, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glBindVertexArray(vaoId);
                    break;
                case 3:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }
                    raw_v_colors = ((ArrayValue)args[1]).array();
                    v_colors = new float[raw_v_colors.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_colors.length; i++){
                        v_colors[i] = (float)raw_v_colors[i].asNumber();
                    }
                    Value[] raw_tex = ((ArrayValue)args[2]).array();
                    SGL.tex = new Texture(raw_tex[0].raw().toString(), raw_tex[1].asInt());
                    SGL.tex.bind();

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glActiveTexture(GL46C.GL_TEXTURE0);
                    GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, SGL.tex.getId());

                    // буффер цветов
                    vertexCBuffer = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vertexCBuffer);
                    fBuffer2 = MemoryManagement.floatBufStoreData(v_colors);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer2, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glVertexAttribPointer(1, 4, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_QUADS, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glDisableVertexAttribArray(1);
                    GL46C.glBindVertexArray(vaoId);
                    break;
            }
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("drawTriangles", new FunctionValue((args -> {
            Value[] raw_v_pos;
            float[] v_pos;
            int vaoId;
            int vboId;
            FloatBuffer fBuffer;

            switch (args.length){
                case 1:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    //создаём атрибут для вершины, указывая id, тип данных.
                    //включаем созданный выше 0 атрибут вершины
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_TRIANGLES, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glBindVertexArray(vaoId);
                    break;
                case 2:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }
                    Value[] raw_v_colors = ((ArrayValue)args[1]).array();
                    float[] v_colors = new float[raw_v_colors.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_colors.length; i++){
                        v_colors[i] = (float)raw_v_colors[i].asNumber();
                    }

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    // буффер цветов
                    int vertexCBuffer = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vertexCBuffer);
                    FloatBuffer fBuffer2 = MemoryManagement.floatBufStoreData(v_colors);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer2, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glVertexAttribPointer(1, 4, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_TRIANGLES, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glBindVertexArray(vaoId);
                    break;
                case 3:
                    raw_v_pos = ((ArrayValue)args[0]).array();
                    v_pos = new float[raw_v_pos.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_pos.length; i++){
                        v_pos[i] = (float)raw_v_pos[i].asNumber();
                    }
                    raw_v_colors = ((ArrayValue)args[1]).array();
                    v_colors = new float[raw_v_colors.length];
                    // convert to float[]
                    for(int i = 0; i < raw_v_colors.length; i++){
                        v_colors[i] = (float)raw_v_colors[i].asNumber();
                    }
                    Value[] raw_tex = ((ArrayValue)args[2]).array();
                    SGL.tex = new Texture(raw_tex[0].raw().toString(), raw_tex[1].asInt());
                    SGL.tex.bind();

                    vaoId = GL46C.glCreateVertexArrays();
                    GL46C.glBindVertexArray(vaoId);
                    //генерируем Vertex Buffer и связываем его
                    vboId = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    fBuffer = MemoryManagement.floatBufStoreData(v_pos);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(0);
                    GL46C.glVertexAttribPointer(0, 3, GL46C.GL_FLOAT, false, 0, 0);

                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glActiveTexture(GL46C.GL_TEXTURE0);
                    GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, SGL.tex.getId());

                    // буффер цветов
                    vertexCBuffer = GL46C.glCreateBuffers();
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vertexCBuffer);
                    fBuffer2 = MemoryManagement.floatBufStoreData(v_colors);
                    GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER, fBuffer2, GL46C.GL_STATIC_DRAW);
                    GL46C.glEnableVertexAttribArray(1);
                    GL46C.glVertexAttribPointer(1, 4, GL46C.GL_FLOAT, false, 0, 0);

                    //развязываем Vbo's и сам лист Vao
                    GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, vboId);
                    GL46C.glBindVertexArray(vaoId);

                    GL46C.glBindVertexArray(vaoId);

                    //рисуем модель используя quad-ы
                    if (SGL.shader != null) SGL.shader.bind();
                    GL46C.glDrawArrays(GL46C.GL_TRIANGLES, 0, v_pos.length / 3);
                    if (SGL.shader != null) SGL.shader.unBind();
                    //выключаем созданный выше 0 атрибут вершины
                    GL46C.glDisableVertexAttribArray(0);
                    GL46C.glDisableVertexAttribArray(1);
                    GL46C.glBindVertexArray(vaoId);
                    break;
            }
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("useShader", new FunctionValue((args -> {
            Arguments.check(2, args.length);
            SGL.shader = new Shader(args[0].raw().toString(), args[1].raw().toString());
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("blendAlphaMode", new FunctionValue((args -> {
            GL46C.glEnable(GL46C.GL_BLEND);
            GL46C.glBlendFunc(GL46C.GL_SRC_ALPHA, GL46C.GL_ONE_MINUS_SRC_ALPHA);
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("shaderUniforms", new FunctionValue((args -> {
            String name = args[args.length-1].raw().toString();
            if (SGL.tex != null) SGL.tex.bind();
            if (SGL.shader != null) SGL.shader.bind();
            if(args.length == 2)
                GL46C.glUniform1f(GL46C.glGetUniformLocation(SGL.shader.programId, name), (float)args[0].asNumber());
            else if(args.length == 3)
                GL46C.glUniform2f(GL46C.glGetUniformLocation(SGL.shader.programId, name), (float)args[0].asNumber(), (float)args[1].asNumber());
            else if(args.length == 4)
                GL46C.glUniform3f(GL46C.glGetUniformLocation(SGL.shader.programId, name), (float)args[0].asNumber(), (float)args[1].asNumber(), (float)args[2].asNumber());
            else if(args.length == 5)
                GL46C.glUniform4f(GL46C.glGetUniformLocation(SGL.shader.programId, name), (float)args[0].asNumber(), (float)args[1].asNumber(), (float)args[2].asNumber(), (float)args[3].asNumber());
            if (SGL.shader != null) SGL.shader.unBind();
            if (SGL.tex != null) SGL.tex.unBind();
            return NumberValue.MINUS_ONE;
        })));
        sgl.put("isCloseRequest", new FunctionValue((args -> new NumberValue(GLFW.glfwWindowShouldClose(Window.id)))));
        newClass("sgl", new ArrayList<>(), sgl);

        sglinput();
    }
    private static void sglinput(){
        Map<String, Value> sglin = new HashMap<>();
        sglin.put("keyDown", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Window.keyDown(args[0].asInt()));
        })));
        sglin.put("keyPressed", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            int key = args[0].asInt();
            return new NumberValue(Window.keyDown(key) && !Window.keys[key]);
        })));
        sglin.put("keyReleased", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            int key = args[0].asInt();
            return new NumberValue(!Window.keyDown(key) && Window.keys[key]);
        })));
        sglin.put("buttonDown", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            return new NumberValue(Window.buttonDown(args[0].asInt()));
        })));
        sglin.put("buttonPressed", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            int key = args[0].asInt();
            return new NumberValue(Window.buttonDown(key) && !Window.buttons[key]);
        })));
        sglin.put("buttonReleased", new FunctionValue((args -> {
            Arguments.check(1, args.length);
            int key = args[0].asInt();
            return new NumberValue(!Window.buttonDown(key) && Window.buttons[key]);
        })));
        sglin.put("mouseX", new FunctionValue((args -> {
            return new NumberValue(Window.mouseX);
        })));
        sglin.put("mouseY", new FunctionValue((args -> {
            return new NumberValue(Window.mouseY);
        })));
        newClass("sglin", new ArrayList<>(), sglin);
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
