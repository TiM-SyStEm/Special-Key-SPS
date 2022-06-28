package com.timsystem.runtime;

import org.dom4j.Node;

public class XMLNodeValue implements Value{
    Node node;

    public XMLNodeValue(Node node) {
        this.node = node;
    }

    @Override
    public Object raw() {
        return node;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public boolean asBool() {
        return false;
    }

    @Override
    public double asNumber() {
        return 0;
    }

    @Override
    public String asString() {
        return node.toString();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
