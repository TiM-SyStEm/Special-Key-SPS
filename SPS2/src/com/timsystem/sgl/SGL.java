package com.timsystem.sgl;


import com.timsystem.runtime.FunctionValue;
import com.timsystem.runtime.Value;
import com.timsystem.sgl.rendering.Shader;
import com.timsystem.sgl.rendering.Texture;

public class SGL {
    public Window window;
    private int width;
    private int height;
    private String title;
    private Value update_func;
    public static Shader shader;
    public static Texture tex;

    public void run(int width, int height, String title, Value update_func)
    {
        this.width = width;
        this.height = height;
        this.title = title;
        this.update_func = update_func;
        this.init();
    }

    public void init()
    {
        this.window = new Window(width, height, title);
        this.window.create();
        this.update();
    }

    public void update()
    {
        while(!this.window.isClosedRequest())
        {
            Window.handleKeyboardInput();
            Window.handleMouseInput();
            ((FunctionValue) update_func).execute();
        }

        this.window.destroy();
    }
}
