package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.resources.IFLResDataBuilder;
import me.mamiiblt.instafel.patcher.resources.ResourceParser;
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
import me.mamiiblt.instafel.patcher.source.SourceManager;
import me.mamiiblt.instafel.patcher.source.SourceUtils;
import me.mamiiblt.instafel.patcher.source.isource.IflSourceCreator;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class CreateIflSourceZip implements Command {

    private File baseValuesDir;
    private IFLResDataBuilder resDataBuilder;

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
                        sourceManager.setConfig(SourceUtils.getDefaultIflConfig(sourceManager.getConfig()));
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
            resDataBuilder = new IFLResDataBuilder(new File(Utils.mergePaths(
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

    private void copyResourceColor() throws ParserConfigurationException, IOException, SAXException {
        ResourcesColor resColors = ResourceParser.parseResColor(new File(
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
        ResourcesAttr resAttrs = ResourceParser.parseResAttr(new File(
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
        ResourcesId resIds = ResourceParser.parseResId(new File(
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
        ResourcesPublic resPublics = ResourceParser.parseResPublic(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "public.xml")
        ));
        List<TPublic> iflPublics = resPublics.getAll();
        iflPublics.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TPublic iflPublic : iflPublics) {
            iflPublic.getElement().removeAttribute("id");
            resDataBuilder.addElToCategory("public", iflPublic.getElement());
        }
        Log.info("Totally " + iflPublics.size() + " public added to resource data.");
    }

    private void copyResourceStyle() throws ParserConfigurationException, IOException, SAXException {
        ResourcesStyle resStyles = ResourceParser.parseResStyle(new File(
            Utils.mergePaths(baseValuesDir.getAbsolutePath(), "styles.xml")
        ));
        List<TStyle> iflStyles = resStyles.getAll();
        iflStyles.removeIf(item -> !item.getName().startsWith("ifl_"));

        for (TStyle iflAttr : iflStyles) {
            resDataBuilder.addElToCategory("styles", iflAttr.getElement());
        }
        Log.info("Totally " + iflStyles.size() + " style added to resource data.");
    }

    private void getAndAddInstafelString(String langCode) throws ParserConfigurationException, IOException, SAXException {
        if (!langCode.isEmpty()) {
            langCode = "-" + langCode;
        }
        ResourcesString resStrings = ResourceParser.parseResString(new File(
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
