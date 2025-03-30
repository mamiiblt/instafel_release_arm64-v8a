package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TPublic extends ResourceType {

    public TPublic(Element element) {
        super(element);
    }

    public String getType() {
        return element.getAttribute("type");
    }

    public String getName() {
        return element.getAttribute("name");
    }

    public String getId() {
        return element.getAttribute("id");
    }

}
