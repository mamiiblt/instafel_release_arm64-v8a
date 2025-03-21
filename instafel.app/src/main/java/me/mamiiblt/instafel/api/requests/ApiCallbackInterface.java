package me.mamiiblt.instafel.api.requests;

import me.mamiiblt.instafel.api.models.InstafelResponse;

public interface ApiCallbackInterface {
    void getResponse(InstafelResponse instafelResponse, int taskId);
    void getResponse(String rawResponse, int taskId);
}
