package me.mamiiblt.instafel.patcher.resources.types;

import org.w3c.dom.Element;

public class TPublic extends ResourceType {

    private long convertedID = 0;

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
    
    public void setId(String val) {
        element.setAttribute("id", val);
    }

    public void setConvertedId(long id) {
        convertedID = id;
    }

    public long getConvertedId() {
        return convertedID;
    }

}
