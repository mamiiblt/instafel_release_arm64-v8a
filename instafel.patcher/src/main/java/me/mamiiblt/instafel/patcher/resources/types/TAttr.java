package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TAttr extends ResourceType {

    public TAttr(Element element) {
        super(element);
    }

    public String getName() {
        return element.getAttribute("name");
    }

    public String getFormat() {
        return element.getAttribute("format");
    }

    public void setName(String newVal) {
        element.setAttribute("name", newVal);
    }
    
    public void setFormat(String newVal) {
        element.setAttribute("format", newVal);
    }
}
