package me.mamiiblt.instafel.patcher.patches.ifl_general;

import java.util.List;

import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.PEnvironment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@PatchInfo (
    name = "Get Generation Info",
    shortname = "get_generation_info",
    desc = "Grab IFL Version and Generation ID from API",
    author = "mamiiblt",
    listable = false,
    runnable = false
)
public class GetGenerationInfo extends InstafelPatch {

    private String API_BASE = null; 
    private OkHttpClient httpClient = new OkHttpClient();
    private boolean isProdMode = false;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        this.API_BASE = PEnvironment.getString(PEnvironment.Keys.API_BASE, "api.mamiiblt.me/ifl");
        this.isProdMode = PConfig.getBoolean(PConfig.Keys.prod_mode, isProdMode);
        return List.of(
            getIflVersion,
            getGenerationID
        );
    }

    InstafelTask getIflVersion = new InstafelTask("Get last IFL version from API") {

        @Override
        public void execute() throws Exception {
            if (isProdMode) {
                Request iflVersionRequest = new Request.Builder()
                    .url("https://" + API_BASE + "/manager_new/lastInstafelData")
                    .addHeader("Authorization", PConfig.getString(PConfig.Keys.manager_token, "null"))
                    .build();
                Response res = httpClient.newCall(iflVersionRequest).execute();

                if (!res.isSuccessful()) {
                    failure("Request failed for iflVersionRequest, code: " + res.code());
                }

                JSONObject iflVRequestParsed = new JSONObject(res.body().string());
                int IFL_VERSION = iflVRequestParsed.getInt("ifl_version");
                PEnvironment.setInteger(PEnvironment.Keys.INSTAFEL_VERSION, (IFL_VERSION + 1));
                Log.info("Instafel version for this generation is " + (IFL_VERSION + 1));
                success("IFL version succesfully saved to env");
            } else {
                success("You are using non-prod mode generator, skipping this patch.");
            }
        }
    };

    InstafelTask getGenerationID = new InstafelTask("Generate Generation ID from API") {

        @Override
        public void execute() throws Exception {
            if (isProdMode) {
                Request genIDRequest = new Request.Builder()
                .url("https://" + API_BASE + "/manager_new/createGenerationId")
                .addHeader("Authorization", PConfig.getString(PConfig.Keys.manager_token, "null"))
                .build();
                Response res = httpClient.newCall(genIDRequest).execute();

                if (!res.isSuccessful()) {
                    failure("Request failed for genIDRequest, code: " + res.code());
                }

                JSONObject genIdResParsed = new JSONObject(res.body().string());
                String GEN_ID = genIdResParsed.getString("generation_id");
                PEnvironment.setString(PEnvironment.Keys.GENID, GEN_ID);
                Log.info("Generation ID for this generation is " + GEN_ID);
                success("Generation ID succesfully saved to env");
            } else {
                success("You are using non-prod mode generator, skipping this patch.");
            }
        }
    };
}