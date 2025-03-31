package me.mamiiblt.instafel.patcher.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

public class Resources<T> implements Iterable<T> {
    private final List<T> resources = new ArrayList<>();
    private Document document;
    
    public void addResource(T item) {
        resources.add(item);
    }

    public int getSize() {
        return resources.size();
    }

    public T getResource(int idx) {
        return resources.get(idx);
    }

    public List<T> getAll(){
        return resources;
    }

    @Override
    public Iterator<T> iterator() {
        return resources.iterator();
    }

    public void setDocument(Document doc) {
        this.document = doc;
    }

    public Document getDocument() {
        return document;
    }
}
