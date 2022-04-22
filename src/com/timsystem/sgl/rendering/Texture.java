package com.timsystem.sgl.rendering;

import com.timsystem.lib.SPKException;
import com.timsystem.sgl.MemoryManagement;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46C;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int id;
    private int width, height;
    private int channels;
    private ByteBuffer data;
    private String res;
    private int format, internalFormat;
    private int filter;

    public Texture(String res, int filter){
        this.res = res;
        this.filter = filter;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer channel = BufferUtils.createIntBuffer(1);
        try{
            data = stbi_load_from_memory(MemoryManagement.resourceToByteBuffer(res), w, h, channel, 0);

            this.width = w.get(0);
            this.height = h.get(0);
            this.channels = channel.get(0);
        }
        catch (Exception ex){
            throw new SPKException("stb_imageError", "image can`t load ");
        }
        if(this.channels == 4){
            this.internalFormat = GL46C.GL_RGBA8;
            this.format = GL46C.GL_RGBA;
        }
        else if(this.channels == 3)
        {
            this.internalFormat = GL46C.GL_RGB;
            this.format = GL46C.GL_RGB;
        }
        this.id = GL46C.glCreateTextures(GL46C.GL_TEXTURE_2D);
        GL46C.glTextureStorage2D(this.id, 1, this.internalFormat, this.width, this.height);

        GL46C.glTextureParameteri(this.id, GL46C.GL_TEXTURE_MIN_FILTER, filter);
        GL46C.glTextureParameteri(this.id, GL46C.GL_TEXTURE_MAG_FILTER, GL46C.GL_NEAREST);
        GL46C.glTextureParameteri(this.id, GL46C.GL_TEXTURE_WRAP_S, GL46C.GL_CLAMP_TO_BORDER);
        GL46C.glTextureParameteri(this.id, GL46C.GL_TEXTURE_WRAP_T, GL46C.GL_CLAMP_TO_BORDER);

        GL46C.glTextureSubImage2D(this.id, 0, 0, 0, this.width, this.height, this.format, GL46C.GL_UNSIGNED_BYTE, data);
        if(data != null)
            stbi_image_free(data);
    }
    public void bind()
    {
        //если вы используете стандарт фунцкии то не забудте активировать ее иначе вы не увидите ее на экране.
        //glActiveTexture(GL_TEXTURE0);

        GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, this.id);
    }

    public void unBind()
    {
        GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getChannels() {
        return channels;
    }

    public ByteBuffer getData() {
        return data;
    }

    public String getRes() {
        return res;
    }

    public int getFormat() {
        return format;
    }

    public int getInternalFormat() {
        return internalFormat;
    }
}
