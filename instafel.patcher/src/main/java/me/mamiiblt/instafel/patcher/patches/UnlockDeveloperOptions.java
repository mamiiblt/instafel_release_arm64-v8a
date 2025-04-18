package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.source.SmaliParser;
import me.mamiiblt.instafel.patcher.source.SmaliParser.SmaliInstruction;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo(
    name = "Unlock Developer Options",
    shortname = "unlock_developer_options",
    desc = "You can unlock developer options with applying this patch!",
    author = "mamiiblt",
    listable = true,
    runnable = true
)
public class UnlockDeveloperOptions extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
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
        public void execute() throws IOException {
            String imageDebugSessionHelperPath = "com/instagram/debug/image/sessionhelper/ImageDebugSessionHelper.smali";
            List<File> whOptionsFileQuery = smaliUtils.getSmaliFilesByName(imageDebugSessionHelperPath);
            File imageDebugSessionSmali = null;
            if (whOptionsFileQuery.size() == 0 || whOptionsFileQuery.size() > 1) {
                failure("ImageDebugSessionHelper file can't be found / selected.");
            } else {
                imageDebugSessionSmali = whOptionsFileQuery.get(0);
                Log.info("ImageDebugSessionHelper file is " + imageDebugSessionSmali.getPath());
            }

            List<String> imageDebugContent = smaliUtils.getSmaliFileContent(
                imageDebugSessionSmali.getAbsolutePath());       
            List<LineData> methodHeaderLines =  smaliUtils.getContainLines(
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

            List<LineData> callLines = smaliUtils.getContainLines(methodContent, 
            "invoke-static", "LX/", "A00");
            if (callLines.size() == 0) {
                failure("callLies is more than 1");
            }
            LineData callLine = callLines.get(0);
            SmaliInstruction callLineInstruction = SmaliParser.parseInstruction(callLine.getContent(), callLine.getNum());
            success("DevOptions class is " + className);
            className = callLineInstruction.getClassName().replace("LX/", "").replace(";", "");
        }
    };

    InstafelTask addConstraintLineTask = new InstafelTask("Add constraint line to LX/? class") {
        @Override
        public void execute() throws IOException {
            File devOptionsFile = smaliUtils.getSmaliFilesByName(
                "X/" + className + ".smali"
            ).get(0);
            List<String> devOptionsContent = smaliUtils.getSmaliFileContent(devOptionsFile.getAbsolutePath());
            List<LineData> moveResultLines = smaliUtils.getContainLines(
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
            smaliUtils.writeContentIntoFile(devOptionsFile.getAbsolutePath(), devOptionsContent);
            Log.info("Contraint added succesfully.");
            success("Developer options unlocked succesfully.");
        }
    };
}
