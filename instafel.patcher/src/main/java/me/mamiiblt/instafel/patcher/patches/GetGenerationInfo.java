package me.mamiiblt.instafel.patcher.patches;

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
    author = "mamiiblt"
)
public class GetGenerationInfo extends InstafelPatch {

    private PEnvironment pEnvironment = getEnv();
    private PConfig pConfig = getConfig();
    private String API_BASE = null; 
    private OkHttpClient httpClient = new OkHttpClient();
    private boolean isProdMode = false;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        this.API_BASE = pEnvironment.getString(PEnvironment.Keys.API_BASE, "api.mamiiblt.me/ifl");
        this.isProdMode = pConfig.getBoolean(PConfig.Keys.prod_mode, isProdMode);
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
                    .addHeader("Authorization", pConfig.getString(PConfig.Keys.manager_token, "null"))
                    .build();
                Response res = httpClient.newCall(iflVersionRequest).execute();

                if (!res.isSuccessful()) {
                    failure("Request failed for iflVersionRequest, code: " + res.code());
                }

                JSONObject iflVRequestParsed = new JSONObject(res.body().string());
                int IFL_VERSION = iflVRequestParsed.getInt("ifl_version");
                pEnvironment.setInteger(PEnvironment.Keys.IFL_VERSION, IFL_VERSION + 1);
                Log.info("Instafel version for this generation is " + (IFL_VERSION + 1));
                success("IFL version succesfully saved to env");
            } else {
                failure("You are using non-prod mode generator, skip this patch.");
            }
        }
    };

    InstafelTask getGenerationID = new InstafelTask("Generate Generation ID from API") {

        @Override
        public void execute() throws Exception {
            if (isProdMode) {

                Request genIDRequest = new Request.Builder()
                .url("https://" + API_BASE + "/manager_new/createGenerationId")
                .addHeader("Authorization", pConfig.getString(PConfig.Keys.manager_token, "null"))
                .build();
                Response res = httpClient.newCall(genIDRequest).execute();

                if (!res.isSuccessful()) {
                    failure("Request failed for genIDRequest, code: " + res.code());
                }

                JSONObject genIdResParsed = new JSONObject(res.body().string());
                String GEN_ID = genIdResParsed.getString("generation_id");
                pEnvironment.setString(PEnvironment.Keys.GENERATION_ID, GEN_ID);
                Log.info("Generation ID for this generation is " + GEN_ID);
                success("Generation ID succesfully saved to env");
           }
        }
    };
}