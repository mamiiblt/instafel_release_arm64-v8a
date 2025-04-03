package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class ResourceType {
    public Element element;

    public ResourceType(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return this.element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
