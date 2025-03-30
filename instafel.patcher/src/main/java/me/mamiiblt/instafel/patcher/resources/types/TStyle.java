package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TStyle extends ResourceType {

    public TStyle(Element element) {
        super(element);
    }

    public String getName() {
        return element.getAttribute("name");
    }
}
