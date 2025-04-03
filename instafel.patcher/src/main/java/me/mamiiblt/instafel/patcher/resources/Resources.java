package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import me.mamiiblt.instafel.patcher.resources.types.ResourceType;

public class Resources<T extends ResourceType> implements Iterable<T> {
    private final List<T> resources = new ArrayList<>();
    private File sourceFile;
    private Document document;
    private Element resourcesTag;
    private String resTypeName;

    public void setDocument(Document doc) {
        this.document = doc;
        this.resourcesTag = (Element) document.getElementsByTagName("resources").item(0);
    }

    public void parse(String tag, Function<Element, T> constructor) {
        NodeList nodeList = document.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);
            if (element.getNodeType() == Node.ELEMENT_NODE) {
                resources.add(constructor.apply((Element) element));
            }
        }
    }

    public void addExternalResource(T item) {
        Element el = (Element) document.importNode(item.getElement(), true);
        item.setElement(el);
        resourcesTag.appendChild(el);
        resources.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return resources.iterator();
    }

    public List<T> getAll() {
        return resources;
    }

    public int getSize() {
        return resources.size();
    }

    public void setFile(File file) {
        this.sourceFile = file;
    }

    public File getFile() {
        return sourceFile;
    }

    public Document getDocument() {
        return document;
    }

    public void setTypeName(String val) {
        this.resTypeName = val;
    }

    public String getResTypeName() {
        return this.resTypeName;
    }
}
