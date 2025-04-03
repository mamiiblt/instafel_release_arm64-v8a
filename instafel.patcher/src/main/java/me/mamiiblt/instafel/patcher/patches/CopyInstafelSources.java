package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.*;
import me.mamiiblt.instafel.patcher.resources.types.*;
import me.mamiiblt.instafel.patcher.utils.*;
import me.mamiiblt.instafel.patcher.utils.PEnvironment.Keys;
import me.mamiiblt.instafel.patcher.utils.patch.*;
import me.mamiiblt.instafel.patcher.utils.sub.PublicResHelper;
import me.mamiiblt.instafel.patcher.utils.sub.PublicResHelper.LastResourceIDs;

@PatchInfo (
    name = "Copy Instafel Sources",
    shortname = "copy_instafel_src",
    desc = "This patch needs to executed for use Instafel stuffs",
    author = "mamiiblt"
)
public class CopyInstafelSources extends InstafelPatch {

    private IFLResData.Parser resDataParser;
    private String valuesFolderPath = Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res", "values");

    @Override
    public List<InstafelTask> initializeTasks() throws ParserConfigurationException, IOException, SAXException {
        preapereResData();
        
        return List.of(
            copySmaliAndResources,
            addActivitiesAndProviderstoManifest,
            mergeStrings,
            mergeIflResources,
            createPublicIDs,
            updateRclasses
        );
    }

    InstafelTask updateRclasses = new InstafelTask("Update Instafel's R classes") {
        @Override
        public void execute() throws Exception {
            File smaliFolder = new File(Utils.mergePaths(
                SmaliUtils.getSmaliFolderByPaths("me", "mamiiblt").getAbsolutePath(), "me", "mamiiblt", "instafel"));
            IOFileFilter prefixFilter = new PrefixFileFilter("R$");
            Collection<File> files = FileUtils.listFiles(smaliFolder, prefixFilter, TrueFileFilter.INSTANCE);
            Resources<TPublic> igResources = ResourceParser.parseResPublic(getValueResourceFile("public.xml"));
            igResources.getAll().removeIf(item -> !item.getName().startsWith("ifl_"));
            for (File file : files) {
                PublicResHelper.updateRclass(igResources, file);
            }
            success("R classes updated succesfully.");
        }
    };

    InstafelTask createPublicIDs = new InstafelTask("Copy public resources with new ID's") {

        @Override
        public void execute() throws Exception {
            Resources<TPublic> igPublic = ResourceParser.parseResPublic(getValueResourceFile("public.xml"));
            Map<String, List<Integer>> categorizedIGPublics = PublicResHelper.getIDsWithCategory(igPublic);
            LastResourceIDs lastResourceIds = PublicResHelper.getBiggestResourceID(categorizedIGPublics);

            // assign new IDs to Instafel resources
            Resources<TPublic> iflPublic = resDataParser.resourcesPublic;
            for (TPublic tPublic : iflPublic) {
                int newId = lastResourceIds.get(tPublic.getType()) + 1;
                lastResourceIds.set(tPublic.getType(), newId);
                tPublic.setId(PublicResHelper.convertToHex(newId));
            }
            Log.info("Totally " + iflPublic.getSize() + " public's id updated.");

            mergeResources(igPublic, iflPublic);
            success("ID's succesfully defined.");
        }
        
    };

    // it's an only defination class
  
    InstafelTask copySmaliAndResources = new InstafelTask("Copy smali / resources") {
        @Override
        public void execute() throws Exception {
            File smallDexFolder = SmaliUtils.getSmallSizeSmaliFolder(SmaliUtils.getSmaliFolders());
            File destFolder = new File(
                Utils.mergePaths(smallDexFolder.getAbsolutePath(), "me", "mamiiblt"));

            Utils.unzipFromResources(false, "/ifl_sources/ifl_sources.zip", destFolder.getAbsolutePath());
            Log.info("Copying instafel resources");
            File igResourcesFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res"));
            Utils.unzipFromResources(false, "/ifl_sources/ifl_resources.zip", igResourcesFolder.getAbsolutePath());
            success("Instafel resources copied");
        }
    };

    InstafelTask addActivitiesAndProviderstoManifest = new InstafelTask("Add activities to manifest") {

        @Override
        public void execute() throws Exception {
            File manifestFile = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "AndroidManifest.xml"));
            Document manifestDoc = ResourceParser.parseResourceDocument(manifestFile);
            Node applicationElement = ResourceParser.getNodesFromResFile(manifestDoc, "application").item(0);
            Node manifestElement = manifestDoc.getDocumentElement();
            List<Element> iflActivities = resDataParser.activities;
            for (Element activity : iflActivities) {
                applicationElement.appendChild(manifestDoc.importNode(activity, true));
            }
            Log.info("Totally " + iflActivities.size() + " activity added");

            List<Element> iflProviders = resDataParser.providers;
            for (Element provider : iflProviders) {
                applicationElement.appendChild(manifestDoc.importNode(provider, true));
            }
            Log.info("Totally " + iflProviders.size() + " provider added");

            Element requestPermEl = manifestDoc.createElement("uses-permission");
            requestPermEl.setAttribute("android:name", "android.permission.REQUEST_INSTALL_PACKAGES");
            manifestElement.appendChild(requestPermEl);
        
            ResourceParser.buildXmlFile(manifestDoc, manifestFile); // build manifest xml file
            success("Activities & providers added succesfully from Instafel base");
        }
    };

    InstafelTask mergeIflResources = new InstafelTask("Copy IFL resources to Instagram") {
        @Override
        public void execute() throws Exception {
            mergeResources(ResourceParser.parseResColor(
                getValueResourceFile("colors.xml")
            ), resDataParser.resourcesColor);
            mergeResources(ResourceParser.parseResAttr(
                getValueResourceFile("attrs.xml")
            ), resDataParser.resourcesAttr);
            mergeResources(ResourceParser.parseResId(
                getValueResourceFile("ids.xml")
            ), resDataParser.resourcesId);
            mergeResources(ResourceParser.parseResStyle(
                getValueResourceFile("styles.xml")
            ), resDataParser.resourcesStyle);

            // merge localized strings
            Map<String, Resources<TString>> strings = resDataParser.resourcesStrings;
            for (String locale : Environment.INSTAFEL_LOCALES) {
                String param = "-" + locale;
                mergeResources(ResourceParser.parseResString(new File(
                    Utils.mergePaths(valuesFolderPath + param, "strings.xml"))
                ), strings.get("strings" + param));
            }

            success("All resources merged succesfully");
        }
    };

    InstafelTask mergeStrings = new InstafelTask("Merge strings with properties") {

        @Override
        public void execute() throws Exception {
            Resources<TString> igResources = ResourceParser.parseResString(getValueResourceFile("strings.xml"));
            mergeResources(igResources, resDataParser.resourcesStrings.get("strings"));
            if (isProdMode) {
                for (TString res : igResources) {
                    if (res.getName().startsWith("ifl_")) {
                        switch (res.getName()) {
                            case "ifl_ig_arch":
                                res.setValue("arm64-v8a");
                                break;
                            case "ifl_ig_vercode":
                                res.setValue(PEnvironment.getString(Keys.INSTAGRAM_VERSION_CODE, res.getValue()));
                                break;
                            case "ifl_ig_version":
                                res.setValue(PEnvironment.getString(Keys.INSTAGRAM_VERSION, res.getValue()));
                                break;
                            case "ifl_version":
                                res.setValue(PEnvironment.getString(Keys.IFL_VERSION, res.getValue()));
                                break;
                            case "ifl_genid":
                                res.setValue(PEnvironment.getString(Keys.GENERATION_ID, res.getValue()));
                                break;
                        }
                    }
                }
            }
            ResourceParser.buildXmlFile(igResources.getDocument(), igResources.getFile());
            Log.info("IFL String values customized for generation");
            success("App strings merged succesfully.");
        }
        
    };

    private static <T extends ResourceType> void mergeResources(Resources<T> target, Resources<T> source) throws TransformerException {
        for (T resource : source) {
            target.addExternalResource(resource);
        }
    
        ResourceParser.buildXmlFile(target.getDocument(), target.getFile());
        Log.info("Totally " + source.getSize() + " resource(s) added to " + target.getResTypeName());
    }

    private File getValueResourceFile(String fileName) {
        return new File(Utils.mergePaths(valuesFolderPath, fileName));
    }

    private void preapereResData() throws IOException, ParserConfigurationException, SAXException {
        File resDataPath = new File(Utils.mergePaths(Environment.PROJECT_DIR, "ifl_data_temp.xml"));
        Utils.copyResourceToFile("/ifl_sources/ifl_data.xml", resDataPath);
        resDataParser = new IFLResData.Parser(resDataPath);
    }
}
