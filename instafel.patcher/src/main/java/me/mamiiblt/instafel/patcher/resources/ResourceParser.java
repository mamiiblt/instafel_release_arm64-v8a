package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.types.ResourceType;
import me.mamiiblt.instafel.patcher.resources.types.TAttr;
import me.mamiiblt.instafel.patcher.resources.types.TColor;
import me.mamiiblt.instafel.patcher.resources.types.TId;
import me.mamiiblt.instafel.patcher.resources.types.TPublic;
import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.resources.types.TStyle;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;

public class ResourceParser {

    private static <T extends ResourceType> Resources<T> parseResource(File file, String tag, Function<Element, T> constructor, String typeName) 
        throws ParserConfigurationException, IOException, SAXException {

        Document doc = parseResourceDocument(file);
        Resources<T> resources = new Resources<>();
        resources.setFile(file);
        resources.setDocument(doc);
        resources.setTypeName(typeName);
        resources.parse(tag, constructor);

        return resources;
    }

    public static Resources<TString> parseResString(File file) throws ParserConfigurationException, IOException, SAXException {
        for (String locale : Environment.INSTAFEL_LOCALES) {
            if (file.getAbsolutePath().contains("values-" + locale)) {
                return parseResource(file, "string", TString::new, "strings-" + locale);
            }
        }
        return parseResource(file, "string", TString::new, "strings");
    }

    public static Resources<TColor> parseResColor(File file) throws ParserConfigurationException, IOException, SAXException {
        return parseResource(file, "color", TColor::new, "colors");
    }

    public static Resources<TAttr> parseResAttr(File file) throws ParserConfigurationException, IOException, SAXException {
        return parseResource(file, "attr", TAttr::new, "attrs");
    }

    public static Resources<TId> parseResId(File file) throws ParserConfigurationException, IOException, SAXException {
        Resources<TId> res = parseResource(file, "item", TId::new, "ids");
        res.getAll().removeIf(item -> !item.getElement().hasAttribute("type"));
        return res;
    }

    public static Resources<TPublic> parseResPublic(File file) throws ParserConfigurationException, IOException, SAXException {
        return parseResource(file, "public", TPublic::new, "publics");
    }

    public static Resources<TStyle> parseResStyle(File file) throws ParserConfigurationException, IOException, SAXException {
        return parseResource(file, "style", TStyle::new, "styles");
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

    public static Document parseResourceDocument(File inputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputFile);
        document.normalize();
        return document;
    }

    public static void buildXmlFile(Document doc, File distFile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(
            new StreamSource(ResourceParser.class.getResourceAsStream("/styling.xslt")));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(distFile);
        transformer.transform(source, result);
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

    public static NodeList getNodesFromResFile(Document document, String tagName) {
        document.normalize();
        return document.getElementsByTagName(tagName);
    }
}
