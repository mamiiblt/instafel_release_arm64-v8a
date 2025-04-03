package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import me.mamiiblt.instafel.patcher.resources.IFLResData;
import me.mamiiblt.instafel.patcher.resources.ResourceParser;
import me.mamiiblt.instafel.patcher.resources.Resources;
import me.mamiiblt.instafel.patcher.resources.types.ResourceType;
import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

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
            // copySmaliAndResources,
            // addActivitiesAndProviderstoManifest,
            // mergeStrings,
            // mergeIflResources
        );
    }

    InstafelTask mergeStrings = new InstafelTask("Merge strings with properties") {

        @Override
        public void execute() throws Exception {
            Resources<TString> igResources = ResourceParser.parseResString(getValueResourceFile("strings.xml"));
            mergeResources(igResources, resDataParser.resourcesStrings.get("strings"));
            for (TString tString : igResources) {
                // don't forget to configure props when project environment read basics finished
                /*switch (tString.getName()) {
                    case "ifl_ig_arch":
                        break;
                
                    default:
                        break;
                }*/
            }
            ResourceParser.buildXmlFile(igResources.getDocument(), igResources.getFile());
            Log.info("IFL String values customized for generation");
            success("App strings merged succesfully.");
        }
        
    };

    InstafelTask mergeIflResources = new InstafelTask("Copy IFL resources to Instagram") {
        @Override
        public void execute() throws Exception {
            // merge colors, attrs, ids, styles
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

    private File getValueResourceFile(String fileName) {
        return new File(Utils.mergePaths(valuesFolderPath, fileName));
    }

    private static <T extends ResourceType> void mergeResources(Resources<T> target, Resources<T> source) throws TransformerException {
        for (T resource : source) {
            target.addExternalResource(resource);
        }
    
        ResourceParser.buildXmlFile(target.getDocument(), target.getFile());
        Log.info("Totally " + source.getSize() + " resource(s) added to " + target.getResTypeName());
    }

    InstafelTask copySmaliAndResources = new InstafelTask("Copy smali / resources") {
        @Override
        public void execute() throws Exception {
            File smallDexFolder = SmaliUtils.getSmallSizeSmaliFolder(SmaliUtils.getSmaliFolders());
            File destFolder = new File(
                Utils.mergePaths(smallDexFolder.getAbsolutePath(), "me", "mamiiblt", "instafel"));

            Utils.unzipFromResources(false, "/ifl_sources/ifl_sources.zip", destFolder.getAbsolutePath());
            success("Smali files succesfully extracted.");
            Log.info("Copying instafel resources");
            File igResourcesFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res"));
            Utils.unzipFromResources(false, "/ifl_sources/ifl_resources.zip", igResourcesFolder.getAbsolutePath());
            Log.info("Instafel resources copied");
        }
    };

    InstafelTask addActivitiesAndProviderstoManifest = new InstafelTask("Add activities to manifest") {

        @Override
        public void execute() throws Exception {
            File manifestFile = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "AndroidManifest.xml"));
            Document manifestDoc = ResourceParser.parseResourceDocument(manifestFile);
            Node applicationElement = ResourceParser.getNodesFromResFile(manifestDoc, "application").item(0);
            
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
            manifestDoc.appendChild(requestPermEl);
        
            ResourceParser.buildXmlFile(manifestDoc, manifestFile); // build manifest xml file
            success("Activities & providers added succesfully from Instafel base");
        }
    };

    private void preapereResData() throws IOException, ParserConfigurationException, SAXException {
        File resDataPath = new File(Utils.mergePaths(Environment.PROJECT_DIR, "ifl_data_temp.xml"));
        Utils.copyResourceToFile("/ifl_sources/ifl_data.xml", resDataPath);
        resDataParser = new IFLResData.Parser(resDataPath);
    }
}
