package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.android.tools.smali.smali.Smali;

import io.github.classgraph.Resource;
import me.mamiiblt.instafel.patcher.resources.IFLResData;
import me.mamiiblt.instafel.patcher.resources.ResourceParser;
import me.mamiiblt.instafel.patcher.resources.Resources;
import me.mamiiblt.instafel.patcher.resources.types.TColor;
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

    @Override
    public List<InstafelTask> initializeTasks() throws ParserConfigurationException, IOException, SAXException {
        preapereResData();
        
        return List.of(
            copySmaliAndResources,
            // addActivitiesAndProviderstoManifest,
            copyValuesResources
        );
    }

    InstafelTask copyValuesResources = new InstafelTask("Copy values resources") {

        @Override
        public void execute() throws Exception {
            File valuesFolderPath = new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res", "values"));

            Resources<TColor> igColors = ResourceParser.parseResColor(new File(Utils.mergePaths(
                valuesFolderPath.getAbsolutePath(), "colors.xml")));
            Resources<TColor> iflColors = resDataParser.resourcesColor;

            for (TColor iflColor : iflColors) {
                igColors.addResource(iflColor);
            }


            Log.info("Totally " + iflColors.getSize() + " color added.");
        }
    };

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
                applicationElement.appendChild(
                    manifestDoc.importNode(activity, true));
            }
            Log.info("Totally " + iflActivities.size() + " activity added");

            List<Element> iflProviders = resDataParser.providers;
            for (Element provider : iflProviders) {
                applicationElement.appendChild(
                    manifestDoc.importNode(provider, true));
            }
            ResourceParser.buildXmlFile(manifestDoc, manifestFile);
            Log.info("Totally " + iflProviders.size() + " provider added");
            success("Activities & providers added succesfully from Instafel base");
        }
    };

    private void preapereResData() throws IOException, ParserConfigurationException, SAXException {
        File resDataPath = new File(Utils.mergePaths(Environment.PROJECT_DIR, "ifl_data_temp.xml"));
        Utils.copyResourceToFile("/ifl_sources/ifl_data.xml", resDataPath);
        resDataParser = new IFLResData.Parser(resDataPath);
    }
}
