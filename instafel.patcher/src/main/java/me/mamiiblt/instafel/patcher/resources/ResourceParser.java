package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesString;

public class ResourceParser {
    public static ResourcesString parseResString(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesString resourcesString = new ResourcesString();
        Document document = parseResourceDocument(inpFile);
        document.normalize();
        List<Element> elements = getElementsFromResFile(document, "string");
        for (Element element : elements) {
            resourcesString.add(
                new TString(element.getAttribute("name"), element.getTextContent())
            );
        }
    
        return resourcesString;
    }

    public static Document parseResourceDocument(File inputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputFile);
        return document;
    }
    
    public static List<Element> getElementsFromResFile(Document document, String tagName) {
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) node);
            }
        }
        return elements;
    }
}
