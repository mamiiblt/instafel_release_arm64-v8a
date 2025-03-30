package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TColor extends ResourceType {

    public TColor(Element element) {
        super(element);
    }

    public String getName() {
        return element.getAttribute("name");
    }

    public String getValue() {
        return element.getTextContent();
    }

    public void setName(String newVal) {
        element.setAttribute("name", newVal);
    }

    public void setValue(String newVal) {
        element.setTextContent(newVal);
    }
}
