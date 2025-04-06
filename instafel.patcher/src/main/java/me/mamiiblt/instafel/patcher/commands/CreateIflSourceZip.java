package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.resources.*;
import me.mamiiblt.instafel.patcher.resources.types.*;
import me.mamiiblt.instafel.patcher.source.*;
import me.mamiiblt.instafel.patcher.source.isource.*;
import me.mamiiblt.instafel.patcher.utils.*;

public class CreateIflSourceZip implements Command {

    private File baseValuesDir;
    private IFLResData.Builder resDataBuilder;

    @Override
    public void execute(String[] args) {
        try {
            if (args.length != 0) {
                String fileArgument = args[0];
                if (fileArgument.contains(".apk")) {
                    File apkPath = new File(Paths.get(Environment.USER_DIR, fileArgument).toString());
                    File outputFolder = new File(Utils.mergePaths(
                        Environment.USER_DIR, apkPath.getName().replace(".apk", "") + "_temp"));

                    if (outputFolder.exists()) {
                        Environment.PROJECT_DIR = outputFolder.getAbsolutePath();
                        Log.info("Output folder is exist");
                    } else {
                        Environment.PROJECT_DIR = IflSourceCreator.createTempSourceDir(apkPath.getName());
                        SourceManager sourceManager = new SourceManager();
                        sourceManager.setConfig(SourceUtils.getDefaultIflConfigDecoder(sourceManager.getConfig()));
                        sourceManager.getConfig().setFrameworkDirectory(SourceUtils.getDefaultFrameworkDirectory());
                        sourceManager.decompile(new ExtFile(
                            Utils.mergePaths(apkPath.getAbsolutePath())
                        ));
                        Log.info("Base APK succesfully decompiled.");
                    }

                    baseValuesDir = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res", "values"));
                    
                    copyInstafelSmaliSources();
                    copyRawSources();
                } else {
                    Log.warning("Please select an .apk file");
                }
            } else {
                Log.severe("Please pass a source APK to CLI");
            }
        } catch(Exception e) {
            Log.severe("Error while running command: " + e.getMessage());
        }
    }

    private void copyInstafelSmaliSources() throws IOException {
        Log.info("Copying Instafel smali sources");
        File sourceFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "smali", "me", "mamiiblt", "instafel")); // select me.mamiiblt.instafel package
        File destFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "smali_sources"));
        FileUtils.copyDirectoryToDirectory(sourceFolder, destFolder);
        Log.info("Smali sources succesfully copied");
    }

    private void copyRawSources() throws IOException, ParserConfigurationException, SAXException {
        Log.info("Copying Instafel resources into /res folder");
        File resFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res"));
        if (!resFolder.exists()) {
            FileUtils.forceMkdir(resFolder);
        }

        copyRawResource("drawable", resFolder);
        copyRawResource("layout", resFolder);
        copyRawResource("xml", resFolder);
        parseResources();
        Utils.zipDirectory(
            Paths.get(Utils.mergePaths(Environment.PROJECT_DIR, "smali_sources")),
            Paths.get(Utils.mergePaths(Environment.PROJECT_DIR, "ifl_sources.zip"))
        );
        Utils.zipDirectory(
            Paths.get(Utils.mergePaths(Environment.PROJECT_DIR, "res")),
            Paths.get(Utils.mergePaths(Environment.PROJECT_DIR, "ifl_resources.zip"))
        );
        Utils.deleteDirectory(Utils.mergePaths(Environment.PROJECT_DIR, "sources"));
        Utils.deleteDirectory(Utils.mergePaths(Environment.PROJECT_DIR, "smali_sources"));
        Utils.deleteDirectory(Utils.mergePaths(Environment.PROJECT_DIR, "res"));
        Log.info("Assets are ready!");
    }

    private void parseResources() {
        try {
            resDataBuilder = new IFLResData.Builder(new File(Utils.mergePaths(
                Environment.PROJECT_DIR, "ifl_data.xml")));

            getAndAddInstafelString("");
            for (String locale : Environment.INSTAFEL_LOCALES) {
                getAndAddInstafelString(locale);
            }
            copyResourceAttr();
            copyResourceColor();
            copyResourceId();
            copyResourceStyle();
            copyResourcePublic();
            exportManifestThingsToResData();
            resDataBuilder.buildXml();
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while parsing / extracting resources");
        }
    }

    private void copyRawResource(String folderName, File resFolder) throws IOException {
        Log.info("Copying " + folderName + " files...");
        File source = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res", folderName));
        File dest = new File(Utils.mergePaths(Environment.PROJECT_DIR, "res", folderName));
        Collection<File> files = FileUtils.listFiles(source, 
            new PrefixFileFilter("ifl_"), null);
        
        for (File file : files) {
            FileUtils.copyFileToDirectory(file, dest);
            Log.info(file.getName() + " copied.");
        }
        Log.info("Totally " + files.size() + " resource copied from " + folderName);
    }

    private void exportManifestThingsToResData() throws ParserConfigurationException, IOException, SAXException {
        Log.info("Exporting activities from Instafel base");
        File fPath = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "AndroidManifest.xml"));
        List<Element> activities = ResourceParser.getActivitiesFromManifest(fPath);

        for (Element element : activities) {
            if (element.getAttribute("android:name").contains("ifl_a_menu")) {
                element.setAttribute("android:exported", "false");
                while (element.hasChildNodes()) {
                    element.removeChild(element.getFirstChild());
                }
            }
            resDataBuilder.addElToCategory("activities", element);
        }

        Log.info("Exporting providers from Instafel base");
        List<Element> providers = ResourceParser.getProvidersFromManifest(fPath);
        providers.removeIf(item -> item.getAttribute("android:authorities").contains("androidx-startup"));
        for (Element provider : providers) {
            resDataBuilder.addElToCategory("providers", provider);
        }
    }

    private void copyResourceColor() throws ParserConfigurationException, IOException, SAXException {
        Resources<TColor> resColors = ResourceParser.parseResColor(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "colors.xml")
        ));
        List<TColor> iflColors = resColors.getAll();
        iflColors.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TColor iflColor : iflColors) {
            resDataBuilder.addElToCategory("colors", iflColor.getElement());
        }
        Log.info("Totally " + iflColors.size() + " color added to resource data.");
    }

    private void copyResourceAttr() throws ParserConfigurationException, IOException, SAXException {
        Resources<TAttr> resAttrs = ResourceParser.parseResAttr(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "attrs.xml")
        ));
        List<TAttr> iflAttrs = resAttrs.getAll();
        iflAttrs.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TAttr iflAttr : iflAttrs) {
            resDataBuilder.addElToCategory("attrs", iflAttr.getElement());
        }
        Log.info("Totally " + iflAttrs.size() + " attr added to resource data.");
    }

    private void copyResourceId() throws ParserConfigurationException, IOException, SAXException {
        Resources<TId> resIds = ResourceParser.parseResId(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "ids.xml")
        ));
        List<TId> iflIds = resIds.getAll();
        iflIds.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TId iflId : iflIds) {
            resDataBuilder.addElToCategory("ids", iflId.getElement());
        }
        Log.info("Totally " + iflIds.size() + " id added to resource data.");
    }

    private void copyResourcePublic() throws ParserConfigurationException, IOException, SAXException {
        Resources<TPublic> resPublics = ResourceParser.parseResPublic(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "public.xml")
        ));
        List<TPublic> iflPublics = resPublics.getAll();
        iflPublics.removeIf(item -> !item.getName().startsWith("ifl_"));
        iflPublics.removeIf(item -> item.getName().equals("ifl_ic_launcher") || item.getName().equals("ifl_ic_launcher_round"));
        for (TPublic iflPublic : iflPublics) {
            iflPublic.getElement().removeAttribute("id");
            resDataBuilder.addElToCategory("public", iflPublic.getElement());
        }
        Log.info("Totally " + iflPublics.size() + " public added to resource data.");
    }

    private void copyResourceStyle() throws ParserConfigurationException, IOException, SAXException {
        Resources<TStyle> resStyles = ResourceParser.parseResStyle(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "styles.xml")
        ));
        List<TStyle> iflStyles = resStyles.getAll();
        iflStyles.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TStyle iflStyle : iflStyles) {
            if (iflStyle.getName().equals("ifl_theme_light")) {
                iflStyle.getElement().removeAttribute("parent");
            }
            // If this property is not exist Instagram will be crash.
            Element igdsColorLink = resStyles.getDocument().createElement("item");
            igdsColorLink.setAttribute("name", "igds_color_link");
            igdsColorLink.setTextContent("@color/ifl_white");
            iflStyle.getElement().appendChild(igdsColorLink);

            resDataBuilder.addElToCategory("styles", iflStyle.getElement());
        }
        Log.info("Totally " + iflStyles.size() + " style added to resource data.");
    }

    private void getAndAddInstafelString(String langCode) throws ParserConfigurationException, IOException, SAXException {
        if (!langCode.isEmpty()) {
            langCode = "-" + langCode;
        }
        Resources<TString> resStrings = ResourceParser.parseResString(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath() + langCode, "strings.xml")
        ));
        List<TString> iflStrings = resStrings.getAll();
        iflStrings.removeIf(item -> !item.getName().startsWith("ifl_"));
        
        for (TString iflStr : iflStrings) {
            resDataBuilder.addElToCategory("strings" + langCode, iflStr.getElement());
        }
        if (langCode.contains("-")) {
            langCode = langCode.replace("-", "");
            Log.info("Totally " + iflStrings.size() + " strings added from " + langCode + " to resource data.");
        } else {
            Log.info("Totally " + iflStrings.size() + " strings added to resource data.");
        }
    }
}
