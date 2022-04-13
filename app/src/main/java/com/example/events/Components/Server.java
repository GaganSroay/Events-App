package com.example.events.Components;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Server {
    private static final String GET_EVENT = "events/get_event";

    private static final String EDIT_EVENT = "events_service/edit_event";
    private static final String START_EVENT = "events_service/start_event/";
    private static final String VERIFY_TICKET = "events_service/verify_ticket";

    private static final String VERIFY_ID_TOKEN = "user/verify_id_token";
    private static final String CHECK_USER = "user/check/";
    private static final String CREATE_USER = "user/create_user";

    private final Context context;

    public Server(Context context) {
        this.context = context;
    }

    static FirebaseVolleyRequest.GetResult getResults(Result onResult) {
        return new FirebaseVolleyRequest.GetResult() {
            @Override
            public void onResult(String result) {
                try {
                    onResult.onResult(new JSONObject(result));
                } catch (JSONException e) {
                    onResult.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                onResult.onError(error);
            }
        };
    }

    public void getEvent(String path, Result onResult) {
        HashMap<String, String> data = new HashMap<>();
        data.put("path", path);
        postRequest(GET_EVENT, data, onResult);
    }

    public void verifyTicket(HashMap<String, String> data, Result onResult) {
        postRequest(VERIFY_TICKET, data, onResult);
    }

    public void createUser(HashMap<String, String> userDetails, Result onResult) {
        postRequest(CREATE_USER, userDetails, onResult);
    }

    public void checkUser(String userId, Result onResult) {
        getRequest(CHECK_USER + userId, onResult);
    }

    public void startEvent(String eventId, Result onResult) {
        getRequest(START_EVENT + eventId, onResult);
    }

    public void editEvent(HashMap<String, String> data, Result onResult) {
        postRequest(EDIT_EVENT, data, onResult);
    }

    public void verifyIdTokken(Result onResult) {
        getRequest(VERIFY_ID_TOKEN, onResult);
    }

    private void postRequest(String route, HashMap<String, String> data, Result onResult) {
        new FirebaseVolleyRequest(context, route).makePostRequest(data, getResults(onResult));
    }

    private void getRequest(String route, Result onResult) {
        new FirebaseVolleyRequest(context, route).makeGetRequest(getResults(onResult));
    }

    public interface Result {
        void onResult(JSONObject result);

        void onError(String error);
    }

    public interface StringResult {
        void onResult(String result);

        void onError(String error);
    }


}
