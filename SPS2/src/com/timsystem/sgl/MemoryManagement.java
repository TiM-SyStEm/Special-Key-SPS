package com.timsystem.sgl;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.*;
import java.nio.channels.FileChannel;

public class MemoryManagement {
    public static ByteBuffer resourceToByteBuffer(final String resource) throws IOException, IOException {
        File file = new File(resource);

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer buffer = BufferUtils.createByteBuffer((int) fileChannel.size() + 1);

        while (fileChannel.read(buffer) != -1){
            ;
        }

        fileInputStream.close();
        fileChannel.close();
        buffer.flip();

        return buffer;
    }
    public static FloatBuffer floatBufStoreData(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public static IntBuffer intBufStoreData(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public static DoubleBuffer floatBufStoreData(double[] data){
        DoubleBuffer buffer = MemoryUtil.memAllocDouble(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public static LongBuffer intBufStoreData(long[] data){
        LongBuffer buffer = MemoryUtil.memAllocLong(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
