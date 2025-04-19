package me.mamiiblt.instafel.patcher.patches.clone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import me.mamiiblt.instafel.patcher.resources.ResourceParser;
import me.mamiiblt.instafel.patcher.resources.Resources;
import me.mamiiblt.instafel.patcher.resources.types.TString;
import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Clone General",
    shortname = "clone_general",
    desc = "It makes app compatible for clone generation.",
    author = "mamiiblt",
    listable = false,
    runnable = false
)
public class CloneGeneral extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
    private File cloneRefFolder = null;
    private Document manifest = null;
    private Element manifestTag = null;
    private List<ProviderData> providerDatas = new ArrayList<>();
    private JSONArray blacklistedPermissions = null;

    private String NEW_PACKAGE_NAME = "com.instafel.android";
    private String NEW_APP_NAME = "Instafel";
    private String ANDROID_AUTHORITIES = "android:authorities";
    private String ANDROID_NAME = "android:name";

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        this.cloneRefFolder = new File(Utils.mergePaths(Env.PROJECT_DIR, "clone_ref"));
        this.blacklistedPermissions = new JSONArray(slurp(CloneGeneral.class.getResourceAsStream("/blacklisted_perms.json")));
        
        return List.of(
            copyIconAssets,
            changePackageInManifest,
            updateAppName,
            changeProviders,
            updatePermissions
        );
    }

    InstafelTask copyIconAssets = new InstafelTask("Copy Icon images") {

        @Override
        public void execute() throws Exception {
            File valuesFolder = new File(Utils.mergePaths(Env.PROJECT_DIR, "sources", "res"));

            Collection<File> allDirs = FileUtils.listFilesAndDirs(
                valuesFolder,
                FalseFileFilter.FALSE,
                TrueFileFilter.INSTANCE 
            );

            for (File dir : allDirs) {
                if (dir.isDirectory() && dir.getName().startsWith("mipmap")) {
                    File fBackground = new File(Utils.mergePaths(dir.getAbsolutePath(), "ig_launcher_background.png"));
                    if (fBackground.exists()) {
                        copyAssetFromResources("/clone_assets/ifl_clone_background.png", new File(
                            fBackground.getAbsolutePath().replace("sources", "clone_ref")
                        ));
                        Log.info("Background copied to clone_ref in " + dir.getName());
                    }

                    File fForeground = new File(Utils.mergePaths(dir.getAbsolutePath(), "ig_launcher_foreground.png"));
                    if (fForeground.exists()) {
                        copyAssetFromResources("/clone_assets/ifl_clone_foreground.png", new File(
                            fForeground.getAbsolutePath().replace("sources", "clone_ref")
                        ));
                        Log.info("Foreground copied to clone_ref in " + dir.getName());
                    }
                }
            }
            success("Icon images succesfully copied.");
        }
        
    };

    private void copyAssetFromResources(String resDir, File distPath) throws IOException {
        InputStream in = CloneGeneral.class.getResourceAsStream(resDir);
        FileUtils.copyToFile(in, distPath);
    }

    InstafelTask changePackageInManifest = new InstafelTask("Change package in manifest") {
        @Override
        public void execute() throws Exception {

            FileUtils.forceMkdir(cloneRefFolder);

            manifest = (Document) ResourceParser.parseResourceDocument(new File(Utils.mergePaths(
                Env.PROJECT_DIR, "sources", "AndroidManifest.xml")));
            manifestTag = manifest.getDocumentElement();
            manifestTag.setAttribute("package", "com.instafel.android");
            updateRefManifest(manifest);
            success("Package attribute changed to com.instafel.android");
        }        
    };

    InstafelTask updateAppName = new InstafelTask("Update app name") {

        @Override
        public void execute() throws Exception {
            Element appTag = ResourceParser.getElementsFromResFile(manifest, "application").get(0);
            String appLabelResource = appTag.getAttribute("android:label").replace("@string/", "");
        
            File stringsFile = new File(Utils.mergePaths(
                Env.PROJECT_DIR, "sources", "res", "values", "strings.xml"));
            Resources<TString> appStrings = ResourceParser.parseResString(stringsFile);
            
            boolean success = false;
            for (TString tString : appStrings) {
                if (tString.getName().equals(appLabelResource)) {
                    success = true;
                    tString.getElement().setTextContent(NEW_APP_NAME);;
                    Log.info("Name changed in resource " + tString.getName());
                }
            }
            
            if (success) {
                File cStringFile = new File(appStrings.getFile().getAbsolutePath().replace("sources", "clone_ref"));
                FileUtils.copyFile(appStrings.getFile(), cStringFile);
                ResourceParser.buildXmlFile(appStrings.getDocument(), cStringFile);
                success("App name succesfully changed within clone ref.");
            } else {
                failure("String resource cannot be found.");
            }
        }
        
    };

    InstafelTask updatePermissions = new InstafelTask("Update IG permissions on manifest") {

        @Override
        public void execute() throws Exception {
            List<Element> permissions = new ArrayList<>();
            permissions.addAll(ResourceParser.getElementsFromResFile(manifest, "permission"));
            permissions.addAll(ResourceParser.getElementsFromResFile(manifest, "uses-permission"));

            Iterator<Element> iterator = permissions.iterator();
            while (iterator.hasNext()) {
                Element perm = iterator.next();
                for (int a = 0; a < blacklistedPermissions.length(); a++) {
                    if (perm.getAttribute(ANDROID_NAME).startsWith(blacklistedPermissions.getString(a))) {
                        iterator.remove();
                        break;
                    }
                }
            }

            for (Element perm : permissions) {
                String name = perm.getAttribute(ANDROID_NAME);

                if (name.contains("com.instagram.android")) {
                    name = name.replace("com.instagram.android", NEW_PACKAGE_NAME);
                    perm.setAttribute(ANDROID_NAME, name);
                    Log.info("Updated " + perm.getTagName() + " " + name);
                }
            }

            updateRefManifest(manifest);
            success("Permissions succesfully updated");
        }
        
    };

    InstafelTask changeProviders = new InstafelTask("Change providers in manifest") {

        @Override
        public void execute() throws Exception {
            List<Element> providers = ResourceParser.getElementsFromResFile(manifest, "provider");
            for (Element provider : providers) {
                String old_authority = provider.getAttribute(ANDROID_AUTHORITIES); 
                String new_authority = old_authority.contains("com.instagram.android") ? 
                    old_authority.replace("com.instagram.android", NEW_PACKAGE_NAME) :
                    "patcher_renamed_" + old_authority;

                provider.setAttribute(ANDROID_AUTHORITIES, new_authority);
                ProviderData providerData = new ProviderData(old_authority, new_authority);
                providerDatas.add(providerData);
                Log.info("Authority changed, " + providerData.new_authority);
            }
            updateRefManifest(manifest);
            Log.info("All authorities (" + providerDatas.size() + ") of providers are changed in manifest");
            Log.info("Changing providers in smali files...");
            File[] smaliFolders = smaliUtils.getSmaliFolders();            
            for (File folder : smaliFolders) {
                Iterator<File> fileIterator = FileUtils.iterateFiles(new File(Utils.mergePaths(folder.getAbsolutePath())), null, true);
                while (fileIterator.hasNext()) {
                    File file = fileIterator.next();
                    List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath()); 

                    for (int i = 0; i < fContent.size(); i++) {
                        String line = fContent.get(i);
                        for (ProviderData pData : providerDatas) {
                            if (line.contains(pData.old_authority)) {
                                fContent.set(i, line.replace(pData.old_authority, pData.new_authority));
                                Log.info("Provider fixed in " + folder.getName() + "/"+ file.getName());
                                FileUtils.writeLines(new File(file.getAbsolutePath().replace("sources", "clone_ref")), fContent);
                            }
                        }
                    }
                }
            }

            success("All providers updated.");
        }
    };

    public class ProviderData {
        public String old_authority, new_authority;

        public ProviderData(String old_authority, String new_authority) {
            this.old_authority = old_authority;
            this.new_authority = new_authority;
        }
    }

    private void updateRefManifest(Document manifest) throws TransformerException {
        ResourceParser.buildXmlFile(manifest, new File(Utils.mergePaths(cloneRefFolder.getAbsolutePath(), "AndroidManifest.xml")));
        Log.info("Referance manifest file updated.");
    }

    public static String slurp(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }
}
