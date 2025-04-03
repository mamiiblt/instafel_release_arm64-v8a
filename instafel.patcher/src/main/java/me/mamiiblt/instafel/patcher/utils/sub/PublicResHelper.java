package me.mamiiblt.instafel.patcher.utils.sub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.resources.Resources;
import me.mamiiblt.instafel.patcher.resources.types.TPublic;
import me.mamiiblt.instafel.patcher.utils.Log;

public class PublicResHelper {

    public static class LastResourceIDs {
        public JSONObject data = new JSONObject();

        public LastResourceIDs() {
            data.put("attr", 0);
            data.put("color", 0);
            data.put("drawable", 0);
            data.put("id", 0);
            data.put("layout", 0);
            data.put("string", 0);
            data.put("style", 0);
            data.put("xml", 0);
        }

        public int get(String type) {
            return data.getInt(type);
        }

        public void set(String type, int val) {
            data.put(type, val);
        }
    }

    public static String convertToHex(int value) {
        return "0x" + Integer.toHexString(value);
    }

    public static int parseHex(String hex) {
        return Integer.parseInt(hex.substring(2), 16);
    }

    public static void updateRclass(Resources<TPublic> iflPublic, File RFile) throws IOException {
        List<String> lines = FileUtils.readLines(RFile, "utf-8");
        Map<String, String> ids = new HashMap<>();
        for (TPublic tPublic : iflPublic) {
            ids.put(tPublic.getName(), tPublic.getId());
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("ifl_")) {
                String lName = line.split("static ")[1].split(":I = ")[0];
                lines.set(i, ".field public static " + lName + ":I = " + ids.get(lName));
            }
        }
        FileUtils.writeLines(RFile, lines);
        Log.info("Class " + RFile.getName() + " succesfully updated.");
    }

    public static Map<String, List<Integer>> getIDsWithCategory(Resources<TPublic> publicValues) {
        Map<String, List<Integer>> categorizedIGPublics = new HashMap<>();
        
        for (TPublic item : publicValues) {
            String iType = item.getType();
            categorizedIGPublics.putIfAbsent(iType, new ArrayList<>());
            categorizedIGPublics.get(iType).add(Integer.parseInt(item.getId().substring(2), 16));
        }

        return categorizedIGPublics;
    }

    public static LastResourceIDs getBiggestResourceID(Map<String, List<Integer>> categorizedIDs) {
        LastResourceIDs lastResourceIds = new LastResourceIDs();
        for (Map.Entry<String, List<Integer>> entry : categorizedIDs.entrySet()) {
            switch (entry.getKey()) {
                case "attr":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("attr")) {
                            lastResourceIds.set("attr", id);
                        }
                    }
                    break;
                case "color":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("color")) {
                            lastResourceIds.set("color", id);
                        }
                    }
                    break;
                case "string":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("string")) {
                            lastResourceIds.set("string", id);
                        }
                    }
                    break;
                case "id":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("id")) {
                            lastResourceIds.set("id", id);
                        }
                    }
                    break;
                case "layout":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("layout")) {
                            lastResourceIds.set("layout", id);
                        }
                    }
                    break;
                case "drawable":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("drawable")) {
                            lastResourceIds.set("drawable", id);
                        }
                    }
                    break;
                case "xml":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("xml")) {
                            lastResourceIds.set("xml", id);
                        }
                    }
                    break;
                case "style":
                    for (int id : entry.getValue()) {
                        if (id > lastResourceIds.get("style")) {
                            lastResourceIds.set("style", id);
                        }
                    }
                    break;
            }
        }

        return lastResourceIds;
    }
}
