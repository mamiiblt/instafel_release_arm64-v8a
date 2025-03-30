package me.mamiiblt.instafel.patcher.resources;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class IFLResDataBuilder {
    
    private File distFile;
    private Document doc;
    private Element valuesEl;
    private Map<String, Element> categories = new HashMap<>();

    public IFLResDataBuilder(File distFile) throws Exception {
        this.distFile = distFile;
        if (!distFile.exists()) {
            distFile.createNewFile();
        }
        createDataDocument();
    }

    private void createDataDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();

        createCategory("strings");
        createCategory("attrs");
        createCategory("public");
    }

    public void addElToCategory(String categoryName, Element element) {
        Element category = categories.get(categoryName);
        category.appendChild(element);
    }

    public void buildXml() throws TransformerException {
        Element elRoot = doc.createElement("instafel");
        doc.appendChild(elRoot);
        Element elValues = doc.createElement("values");
        elRoot.appendChild(elValues);

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
