package com.timsystem.sgl.rendering;

import com.timsystem.lib.SPKException;
import org.lwjgl.opengl.GL46C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shader {
    public int programId;

    public Shader(String vsSrc, String fsSrc){
        Map<Integer, String> shaderSources = new HashMap<Integer, String>(2);
        shaderSources.put(1, vsSrc);
        shaderSources.put(2, fsSrc);
        this.compile(shaderSources);
    }
    public void compile(Map<Integer, String> shaderSrc){
        int program = GL46C.glCreateProgram();

        List<Integer> shaderIds = new ArrayList<>();
        int shaderIdIndex = 1;
        for(int i = 0; i < shaderSrc.size(); i++){
            int type = i == 0 ? GL46C.GL_VERTEX_SHADER : i == 1 ? GL46C.GL_FRAGMENT_SHADER : -1;
            String source = shaderSrc.get(shaderIdIndex);
            int shader = GL46C.glCreateShader(type);
            GL46C.glShaderSource(shader, source);
            GL46C.glCompileShader(shader);

            int compres = 0;
            compres = GL46C.glGetShaderi(shader, GL46C.GL_COMPILE_STATUS);
            if (compres == GL46C.GL_FALSE){
                int maxLength = 0;
                maxLength = GL46C.glGetShaderi(shader, GL46C.GL_INFO_LOG_LENGTH);

                String infoLog = GL46C.glGetShaderInfoLog(shader, maxLength);
                GL46C.glDeleteShader(shader);
                String st = type == 0 ? "Vertex Shader" : "Fragment Shader";
                throw new SPKException("ShaderCompileError", String.format("failed to compile %s: {%s}", st, infoLog));
            }
            GL46C.glAttachShader(program, shader);
            shaderIdIndex++;
        }
        GL46C.glLinkProgram(program);
        int isLinked = 0;
        isLinked = GL46C.glGetProgrami(program, GL46C.GL_LINK_STATUS);
        if(isLinked == GL46C.GL_FALSE)
        {
            int maxLength = 0;
            maxLength = GL46C.glGetProgrami(program, GL46C.GL_INFO_LOG_LENGTH);

            String infoLog = "";
            infoLog = GL46C.glGetProgramInfoLog(program, maxLength);

            for(int shaderId : shaderIds)
            {
                GL46C.glDetachShader(program, shaderId);
            }

            for(int shaderId : shaderIds)
            {
                GL46C.glDeleteShader(shaderId);
            }
            throw new SPKException("ShaderLinkError", String.format("сannot link shader program: {%s}", infoLog));
        }
        this.programId = program;
    }

    public void bind(){
        GL46C.glUseProgram(this.programId);
    }
    public void unBind(){
        GL46C.glUseProgram(0);
    }
}
