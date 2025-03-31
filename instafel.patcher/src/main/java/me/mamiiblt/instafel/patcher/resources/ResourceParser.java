package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.types.TAttr;
import me.mamiiblt.instafel.patcher.resources.types.TColor;
import me.mamiiblt.instafel.patcher.resources.types.TId;
import me.mamiiblt.instafel.patcher.resources.types.TPublic;
import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.resources.types.TStyle;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesAttr;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesColor;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesId;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesPublic;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesString;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesStyle;
import me.mamiiblt.instafel.patcher.utils.Log;

public class ResourceParser {
    public static ResourcesString parseResString(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesString resourcesString = new ResourcesString();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "string");
        for (Element element : elements) {
            resourcesString.add(new TString(element));
        }
    
        return resourcesString;
    }

    public static ResourcesColor parseResColor(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesColor resourcesColor = new ResourcesColor();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "color");
        for (Element element : elements) {
            resourcesColor.add(new TColor(element));
        }
    
        return resourcesColor;
    }

    public static ResourcesAttr parseResAttr(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesAttr resourcesAttr = new ResourcesAttr();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "attr");
        for (Element element : elements) {
            resourcesAttr.add(new TAttr(element));
        }
    
        return resourcesAttr;
    }

    public static ResourcesId parseResId(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesId resourcesId = new ResourcesId();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "item");
        for (Element element : elements) {
            resourcesId.add(new TId(element));
        }
    
        return resourcesId;
    }

    public static List<Element> getActivitiesFromManifest(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        if (!inpFile.getName().equals("AndroidManifest.xml")) {
            Log.severe("Input file is not an AndroidManifest file");
        }

        Document document = parseResourceDocument(inpFile);
        return getElementsFromResFile(document, "activity");
    }

    public static List<Element> getProvidersFromManifest(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        if (!inpFile.getName().equals("AndroidManifest.xml")) {
            Log.severe("Input file is not an AndroidManifest file");
        }

        Document document = parseResourceDocument(inpFile);
        return getElementsFromResFile(document, "provider");
    }

    public static ResourcesPublic parseResPublic(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesPublic resourcesPublic = new ResourcesPublic();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "public");
        for (Element element : elements) {
            resourcesPublic.add(new TPublic(element));
        }
    
        return resourcesPublic;
    }

    public static ResourcesStyle parseResStyle(File inpFile) throws ParserConfigurationException, IOException, SAXException {
        ResourcesStyle resourcesStyle = new ResourcesStyle();
        Document document = parseResourceDocument(inpFile);
        List<Element> elements = getElementsFromResFile(document, "style");
        for (Element element : elements) {
            resourcesStyle.add(new TStyle(element));
        }
    
        return resourcesStyle;
    }

    public static Document parseResourceDocument(File inputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputFile);
        document.normalize();
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
