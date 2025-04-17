package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.types.*;
import me.mamiiblt.instafel.patcher.utils.Env;

public class IFLResData {

    public static class Parser {
        private File inputFile;
        public Resources<TAttr> resourcesAttr;
        public Resources<TId> resourcesId;
        public Resources<TColor> resourcesColor;
        public Resources<TStyle> resourcesStyle;
        public Resources<TPublic> resourcesPublic;
        public List<Element> activities, providers;
        public Map<String, Resources<TString>> resourcesStrings = new HashMap<>();

        public Parser(File inputFile) throws ParserConfigurationException, IOException, SAXException {
            this.inputFile = inputFile;
            parseResData();
        }

        private void parseResData() throws ParserConfigurationException, IOException, SAXException {
            resourcesColor = ResourceParser.parseResColor(inputFile);
            resourcesId = ResourceParser.parseResId(inputFile);
            resourcesStyle = ResourceParser.parseResStyle(inputFile);
            resourcesPublic = ResourceParser.parseResPublic(inputFile);
            resourcesAttr = ResourceParser.parseResAttr(inputFile);
            Document document = ResourceParser.parseResourceDocument(inputFile);
            activities = ResourceParser.getElementsFromResFile(document, "activity");
            providers = ResourceParser.getElementsFromResFile(document, "provider");
            parseStringRes();
        }

        private void parseStringRes() throws ParserConfigurationException, IOException, SAXException {
            Document document = ResourceParser.parseResourceDocument(inputFile);
            List<Element> categories = ResourceParser.getElementsFromResFile(document, "vcategory");
            categories.removeIf(item -> !item.getAttribute("name").contains("strings"));

            for (Element category : categories) {
                Resources<TString> resourcesString = new Resources<TString>();
                resourcesString.setFile(inputFile);
                resourcesString.setDocument(document);
                NodeList categoryElements = category.getChildNodes();
                for (int i = 0; i < categoryElements.getLength(); i++) {
                    Node node = categoryElements.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        resourcesString.getAll().add(new TString((Element) node));
                    }
                }
                resourcesStrings.put(category.getAttribute("name"), resourcesString);
            }
        }
    }
    
    public static class Builder {
        private File distFile;
        private Document doc;
        private Element elValues;
        private Map<String, Element> categories = new HashMap<>();

        public Builder(File distFile) throws Exception {
            this.distFile = distFile;
            if (!distFile.exists()) {
                distFile.createNewFile();
            }
            createDataDocument();
        }

        private void createDataDocument() throws ParserConfigurationException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Element elRoot = doc.createElement("instafel");
            elRoot.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android");
            doc.appendChild(elRoot);
            elValues = doc.createElement("values");
            elRoot.appendChild(elValues);

            createCategory("providers");
            createCategory("activities");
            createCategory("styles");
            createCategory("public");
            createCategory("ids");
            createCategory("attrs");
            createCategory("colors");
            createCategory("strings");
            for (String langCode : Env.INSTAFEL_LOCALES) {
                createCategory("strings-" + langCode);
            }
        }

        public void addElToCategory(String categoryName, Element element) {
            Element category = categories.get(categoryName);
            Node importedNode = doc.importNode(element, true);
            category.appendChild(importedNode);
        }

        public void buildXml() throws TransformerException {
    
            for (Map.Entry<String, Element> entry : categories.entrySet()) {
                elValues.appendChild(entry.getValue());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(distFile);
            transformer.transform(source, result);
        }

        private void createCategory(String categortName) {
            Element vcategoryEl = doc.createElement("vcategory");
            vcategoryEl.setAttribute("name", categortName);
            categories.put(categortName, vcategoryEl);
        }

        public Document getDocument() {
            return doc;
        }
    }
}
