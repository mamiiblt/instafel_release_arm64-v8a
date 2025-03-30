package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.resources.IFLResDataBuilder;
import me.mamiiblt.instafel.patcher.resources.ResElementBuilder;
import me.mamiiblt.instafel.patcher.resources.ResourceParser;
import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.resources.vt.ResourcesString;
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
        File sourceFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "smali", "me")); // select me.mamiiblt.instafel package
        File destFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "smali_sources"));
        FileUtils.copyDirectoryToDirectory(sourceFolder, destFolder);
        Log.info("Smali sources succesfully copied");
    }

    private void copyRawSources() throws IOException {
        Log.info("Copying Instafel resources into /res folder");
        File resFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "res"));
        if (!resFolder.exists()) {
            FileUtils.forceMkdir(resFolder);
        }

        copyRawResource("drawable", resFolder);
        copyRawResource("layout", resFolder);
        copyRawResource("xml", resFolder);
        parseResources();
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
        
        Document doc = resDataBuilder.getDocument();
        for (TString iflStr : iflStrings) {
            resDataBuilder.addElToCategory("strings" + langCode, ResElementBuilder.buildString(
                doc, iflStr));
        }
        Log.info("Totally " + iflStrings.size() + " strings added from " + langCode + " managed.");
    }

    private void parseResources() {
        try {
            resDataBuilder = new IFLResDataBuilder(new File(Utils.mergePaths(
                Environment.PROJECT_DIR, "res", "ifl_res.xml")));

            getAndAddInstafelString("");
            for (String locale : Environment.INSTAFEL_LOCALES) {
                getAndAddInstafelString(locale);
            }
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
}
