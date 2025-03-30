package me.mamiiblt.instafel.patcher.resources;

import org.w3c.dom.*;

import me.mamiiblt.instafel.patcher.resources.types.TString;

public class ResElementBuilder {
    public static Element buildString(Document document, TString tString) {
        Element element = document.createElement("string");
        element.setAttribute("name", tString.getName());
        element.setTextContent(tString.getValue());
        return element;
    }
}
