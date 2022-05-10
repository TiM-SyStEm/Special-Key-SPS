package com.timsystem.sgl.object;

import com.timsystem.sgl.rendering.Texture;
import org.lwjglx.util.vector.Vector3f;

public class GameObject {
    protected Vector3f position;
    protected Vector3f rotation;
    protected Vector3f scale;
    private Texture texture;

    public GameObject(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = new Vector3f(position);
        this.rotation = new Vector3f(rotation);
        this.scale = new Vector3f(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
