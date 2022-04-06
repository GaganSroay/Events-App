package com.example.events.Components;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Server {

    private final Context context;

    public Server(Context context) {
        this.context = context;
    }

    public void getEvent(String path, Result onResult) {
        HashMap<String, String> data = new HashMap<>();
        data.put("path", path);
        FirebaseVolleyRequest request = new FirebaseVolleyRequest(context, "events/get_event");
        postResuest(data, request, onResult);

    }

    public void editEvent(HashMap<String, String> data, Result onResult) {
        FirebaseVolleyRequest request = new FirebaseVolleyRequest(context, "events_service/edit_event");
        postResuest(data, request, onResult);
    }

    public void verifyIdTokken(Result onResult) {
        FirebaseVolleyRequest request = new FirebaseVolleyRequest(context, "user/verify_id_token");
        getRequest(request, onResult);
    }

    private void postResuest(HashMap<String, String> data, FirebaseVolleyRequest request, Result onResult) {
        request.makePostRequest(data, new FirebaseVolleyRequest.GetResult() {
            @Override
            public void onResult(String result) {
                try {
                    onResult.onResult(new JSONObject(result));
                } catch (JSONException e) {
                    onResult.onError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                onResult.onError(error);
            }
        });
    }

    private void getRequest(FirebaseVolleyRequest request, Result onResult) {
        request.makeGetRequest(new FirebaseVolleyRequest.GetResult() {
            @Override
            public void onResult(String result) {
                try {
                    onResult.onResult(new JSONObject(result));
                } catch (JSONException e) {
                    onResult.onError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                onResult.onError(error);
            }
        });
    }

    public interface Result {
        void onResult(JSONObject result);

        void onError(String error);
    }
}
