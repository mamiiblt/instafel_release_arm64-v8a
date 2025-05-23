package me.mamiiblt.instafel.patcher.patches.fix;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.source.SmaliParser;
import me.mamiiblt.instafel.patcher.source.SmaliParser.SmaliInstruction;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils.MethodContent;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Fix Secure Content Crash",
    shortname = "fix_secure_ctx_crash",
    desc = "Fixes Link & MainActivity crash issues with when using custom signature",
    author = "mamiiblt",
    listable = true,
    runnable = true
)
public class FixSecureCtxCrash extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
    private File secureCtxFile = null;
    private List<String> sCtxFContent = null;
    private int invokeA00Line = 0, invokeA01Line = 0;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findSecureContext,
            getInvokeLinesFromCtx,
            replaceValuesBetweenInvoke
        );
    }

    InstafelTask findSecureContext = new InstafelTask("Find IgSecureContext part in X") {

        @Override
        public void execute() throws Exception {
            File[] smaliFolders = smaliUtils.getSmaliFolders();
            int scannedFileSize = 0;
            boolean fileFoundLock = false;
            
            for (File folder : smaliFolders) {
                if (fileFoundLock) {
                    break;
                } else {
                    File xFolder = new File(Utils.mergePaths(folder.getAbsolutePath(), "X"));
                    Log.info("Searching in X folder of " + folder.getName());
    
                    Iterator<File> fileIterator = FileUtils.iterateFiles(xFolder, null, true);
                    while (fileIterator.hasNext()) {
                        scannedFileSize++;
                        File file = fileIterator.next();
                        List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath()); 
                        List<LineData> matchLines = smaliUtils.getContainLines(fContent,
                        "const-string", "IgSecureContext");
                        
                        if (matchLines.size() == 1) {
                            secureCtxFile = file;
                            Log.info("File found in " + secureCtxFile.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (secureCtxFile != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                Log.info("File name is " + secureCtxFile.getName());
                success("IgSecureContext X file is find succesfully.");
            } else {
                failure("IgSecureContext file cannot be found.");
            }
        }
    };

    InstafelTask getInvokeLinesFromCtx = new InstafelTask("Get invoke parts from A00 and A01 method") {

        @Override
        public void execute() throws Exception {
            sCtxFContent = smaliUtils.getSmaliFileContent(secureCtxFile.getAbsolutePath());
            List<LineData> methodStarts = smaliUtils.getContainLines(sCtxFContent, 
            ".method", "A0", "Landroid/content/Context;Landroid/content/Intent;");    
            
            if (methodStarts.size() == 0) {
                failure("No any A0X method found in IgSecureContext");
            } else {
                for (int i = 0; i < methodStarts.size(); i++) {
                    LineData methodStart = methodStarts.get(i);

                    if (methodStart.getContent().contains("A00")) {
                        Log.info("Method A00 found succesfully.");
                        MethodContent mContent = smaliUtils.getMethodContent(sCtxFContent, methodStart.getNum());
                        List<SmaliInstruction> instructions = getInvokeVirtualsFromMethod(mContent);
                        if (instructions.size() == 0 || instructions.size() > 1) {
                            failure("No any invoke-direct instr. found in A00");
                        } else {
                            invokeA00Line = instructions.get(0).getNum();
                            Log.info("Instruction found in A00");
                        }
                    }

                    if (methodStart.getContent().contains("A01")) {
                        Log.info("Method A01 found succesfully at line " + methodStart.getNum());
                        MethodContent mContent = smaliUtils.getMethodContent(sCtxFContent, methodStart.getNum());
                        List<SmaliInstruction> instructions = getInvokeVirtualsFromMethod(mContent);
                        if (instructions.size() == 0 || instructions.size() > 1) {
                            failure("No any invoke-direct instr. found in A01");
                        } else {
                            invokeA01Line = instructions.get(0).getNum();
                            Log.info("Instruction found in A01");
                        }
                    }
                }
                success("Invoke virtual instructions founded succesfully.");
            }
        }
    };

    InstafelTask replaceValuesBetweenInvoke = new InstafelTask("Use old method for A00 instruction") {

        @Override
        public void execute() throws Exception {
            String valueA00 = sCtxFContent.get(invokeA00Line);
            String valueA01 = sCtxFContent.get(invokeA01Line);
            SmaliInstruction instA00 = SmaliParser.parseInstruction(valueA00, invokeA00Line);
            SmaliInstruction instA01 = SmaliParser.parseInstruction(valueA01, invokeA01Line);
            Log.info("Old: " + valueA00.trim());

            if (
                instA00.getMethodName().equals(instA01.getMethodName()) || 
                instA00.getReturnType().equals(instA01.getReturnType())) {
                failure("Method names are same");
            }

            valueA00 = valueA00.replace(
                instA00.getMethodName(), instA01.getMethodName());
            valueA00 = valueA00.replace(
                instA00.getReturnType(), instA01.getReturnType());

            sCtxFContent.set(invokeA00Line, valueA00);
            Log.info("New: " + valueA00.trim());

            FileUtils.writeLines(secureCtxFile, sCtxFContent);
            success("Line modified succesfully.");
        }        
    };

    private List<SmaliInstruction> getInvokeVirtualsFromMethod(MethodContent mContent) {
        List<SmaliInstruction> instructions = new ArrayList<>();

        for (Map.Entry<Integer, String> line : mContent.lines.entrySet()) {
            int num = line.getKey();
            String content = line.getValue();

            Log.info("Line: " + num + " - " + content);

            if (content.contains("invoke-virtual")) {
                SmaliInstruction parsedInstruction = SmaliParser.parseInstruction(content, num);
                if (
                    parsedInstruction != null &&
                    parsedInstruction.getRegisters().length == 1 &&
                    !"Z".equals(parsedInstruction.getReturnType())
                ) {
                    Log.info("An invoke-virtual opcode found at line " + num);
                    instructions.add(parsedInstruction);
                }
            }
        }

        return instructions;
    }
}

