package com.timsystem.sgl;

import com.timsystem.lib.SPKException;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Window {
    private final int width;
    private final int height;
    public static IntBuffer bufferedWidth;
    public static IntBuffer bufferedHeight;
    public static String title;
    public static long id;
    public static GLFWVidMode videoMode;
    public static int xpos;
    public static int ypos;
    public static boolean isCenterPos = false;
    public static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    public static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    public static float mouseX;
    public static float mouseY;

    public Window(int width, int height, String title)
    {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create()
    {
        if(!GLFW.glfwInit()){
            throw new SPKException("CreateWindowError", "sgl3d can't create a window");
        }
        id = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if(id == 0){
            throw new SPKException("CreateWindowError", "the window id is zero");
        }
        try(MemoryStack memoryStack = MemoryStack.stackPush()){
            bufferedWidth = BufferUtils.createIntBuffer(1);
            bufferedHeight = BufferUtils.createIntBuffer(1);
            GLFW.glfwGetWindowSize(id, bufferedWidth, bufferedHeight);
            videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowTitle(id, title);
            GLFW.glfwSetWindowSize(id, width, height);
            GLFW.glfwSetWindowAspectRatio(id, width, height);
        }
        catch (Exception e){
            throw new SPKException("MemoryStackError", "sgl3d can't create a window");
        }

        if(isCenterPos){
            GLFW.glfwSetWindowPos(Window.id,
                    ((Window.videoMode.width() - Window.bufferedWidth.get(0)) / 2),
                    ((Window.videoMode.height() - Window.bufferedHeight.get(0)) / 2));
        }
        else GLFW.glfwSetWindowPos(Window.id, xpos, ypos);

        GLFW.glfwMakeContextCurrent(id);
        GL.createCapabilities();
        GL46C.glViewport(0, 0, bufferedWidth.get(0), bufferedHeight.get(0));

        setCursorPosCallbacks(id);
    }

    public void update()
    {
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(id);
    }

    public void destroy()
    {
        GLFW.glfwDestroyWindow(id);
    }

    public boolean isClosedRequest()
    {
        return GLFW.glfwWindowShouldClose(id);
    }

    public static void handleKeyboardInput(){
        for(int i = 0; i < GLFW.GLFW_KEY_LAST; i++){
            keys[i] = keyDown(i);
        }
    }
    public static boolean keyDown(int key){
        return GLFW.glfwGetKey(Window.id, key) == GLFW.GLFW_TRUE;
    }
    public static boolean buttonDown(int key){
        return GLFW.glfwGetMouseButton(Window.id, key) == GLFW.GLFW_TRUE;
    }
    public static void handleMouseInput(){
        for(int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++){
            buttons[i] = buttonDown(i);
        }
    }
    public static void setCursorPosCallbacks(long id){
        GLFW.glfwSetCursorPosCallback(id, (window, xpos, ypos) -> {
            mouseX = (float)xpos;
            mouseY = (float)ypos;
        });
    }
}
