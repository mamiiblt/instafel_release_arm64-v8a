package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo(
    name = "Unlock Developer Options",
    shortname = "unlock_developer_options",
    desc = "You can unlock developer options with applying this patch!",
    author = "mamiiblt"
)
public class UnlockDeveloperOptions extends InstafelPatch {

    private String className = null;

    @Override
    public List<InstafelTask> initializeTasks() {
        return List.of(
            getDevOptionsTask,
            addConstraintLineTask
        );
    }

    InstafelTask getDevOptionsTask = new InstafelTask("Get dev options class from whoptions") {
        @Override
        public void execute() throws FileNotFoundException {
            String imageDebugSessionHelperPath = "com/instagram/debug/image/sessionhelper/ImageDebugSessionHelper.smali";
            List<File> whOptionsFileQuery = SmaliUtils.getSmaliFilesByName(imageDebugSessionHelperPath);
            File imageDebugSessionSmali = null;
            if (whOptionsFileQuery.size() == 0 || whOptionsFileQuery.size() > 1) {
                failure("ImageDebugSessionHelper file can't be found / selected.");
            } else {
                imageDebugSessionSmali = whOptionsFileQuery.get(0);
                Log.info("ImageDebugSessionHelper file is " + imageDebugSessionSmali.getPath());
            }

            List<String> imageDebugContent = SmaliUtils.getSmaliFileContent(
                imageDebugSessionSmali.getAbsolutePath());       
            List<LineData> methodHeaderLines =  SmaliUtils.getContainLines(
                imageDebugContent, "method", "shouldEnableImageDebug");

            if (methodHeaderLines.size() != 1) {
                failure("shouldEnableImageDebug method can't found!");
            }

            LineData methodHeader = methodHeaderLines.get(0);
            List<String> methodContent = new ArrayList<>();
            for (int i = methodHeader.getNum(); i < imageDebugContent.size(); i++) {
            String line = imageDebugContent.get(i);
            methodContent.add(line);
                if (line.contains(".end method")) {
                    break;
                }
            }

            List<LineData> callLines = SmaliUtils.getContainLines(methodContent, 
            "invoke-static", "LX/", "A00");
            if (callLines.size() == 0) {
                failure("callLies is more than 1");
            }
            LineData callLine = callLines.get(0);
            className = callLine.getContent().split("/")[1].split(";")[0];
            success("DevOptions class is " + className);
        }
    };

    InstafelTask addConstraintLineTask = new InstafelTask("Add constraint line to LX/? class") {
        @Override
        public void execute() throws IOException {
            File devOptionsFile = SmaliUtils.getSmaliFilesByName(
                "X/" + className + ".smali"
            ).get(0);
            List<String> devOptionsContent = SmaliUtils.getSmaliFileContent(devOptionsFile.getAbsolutePath());
            List<LineData> moveResultLines = SmaliUtils.getContainLines(
                devOptionsContent, "move-result", "v0");
            if (moveResultLines.size() != 1) {
                failure("Move result line size is 0 or bigger than 1");
            } 
            LineData moveResultLine = moveResultLines.get(0);
            if (devOptionsContent.get(moveResultLine.getNum() + 2).contains("const v0, 0x1")) {
                failure("Developer options already unlocked.");
            } 

            devOptionsContent.add(moveResultLine.getNum() + 1, "    ");
            devOptionsContent.add(moveResultLine.getNum() + 2, "    const v0, 0x1");
            SmaliUtils.writeContentIntoFile(devOptionsFile.getAbsolutePath(), devOptionsContent);
            Log.info("Contraint added succesfully.");
            success("Developer options unlocked succesfully.");
        }
    };
}
