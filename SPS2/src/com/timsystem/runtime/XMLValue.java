package com.timsystem.runtime;

import org.dom4j.Document;

public class XMLValue implements Value{
    private Document document;

    public XMLValue(Document document) {
        this.document = document;
    }

    @Override
    public Object raw() {
        return document;
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
        return document.toString();
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
