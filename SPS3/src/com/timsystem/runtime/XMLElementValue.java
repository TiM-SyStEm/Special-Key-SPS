package com.timsystem.runtime;

import org.dom4j.Document;
import org.dom4j.Element;

public class XMLElementValue implements Value {

    private Element element;
    private Document doc;

    public XMLElementValue(Element element, Document doc) {
        this.element = element;
        this.doc = doc;
    }

    public XMLElementValue(Element element) {
        this.element = element;
    }

    @Override
    public Object raw() {
        return element;
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
        return element.getName();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
