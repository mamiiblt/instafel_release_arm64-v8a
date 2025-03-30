package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TId extends ResourceType {

    public TId(Element element) {
        super(element);
    }

    public String getType() {
        return element.getAttribute("type");
    }

    public String getName() {
        return element.getAttribute("name");
    }

    public void setType(String newVal) {
        element.setAttribute("type", newVal);
    }
    
    public void setName(String newVal) {
        element.setAttribute("name", newVal);
    }
}
