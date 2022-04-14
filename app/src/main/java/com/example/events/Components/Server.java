package com.example.events.Components;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Server {
    private static final String GET_EVENT = "events/get_event";
    private static final String GET_EVENTBODY = "events/get_body/";
    private static final String ADD_BODY_ELEMENT = "events/add_body_element";

    private static final String EDIT_EVENT = "events_service/edit_event";
    private static final String START_EVENT = "events_service/start_event/";
    private static final String VERIFY_TICKET = "events_service/verify_ticket";
    private static final String INVITE_PARTICIPANTS = "events_service/invite";

    private static final String VERIFY_ID_TOKEN = "user/verify_id_token";
    private static final String CHECK_USER = "user/check/";
    private static final String CREATE_USER = "user/create_user";

    private final Context context;

    public Server(Context context) {
        this.context = context;
    }

    public static FirebaseVolleyRequest.GetResult getResults(Result onResult) {
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

    public static HashMap<String, String> wrapInHashMap(String key, String value) {
        HashMap<String, String> data = new HashMap<>();
        data.put(key, value);
        return data;
    }

    public void inviteParticipant(HashMap<String, String> data, Result onResult) {
        postRequest(INVITE_PARTICIPANTS, data, onResult);
    }

    public void addBodyElement(HashMap<String, String> data, Result onResult) {
        postRequest(ADD_BODY_ELEMENT, data, onResult);
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

    public void getEventBody(String eventId, Result onResult) {
        getRequest(GET_EVENTBODY + eventId, onResult);
    }

    public void getEvent(String path, Result onResult) {
        postRequest(GET_EVENT, wrapInHashMap("path", path), onResult);
    }

    public interface Result {
        void onResult(JSONObject result) throws JSONException;

        void onError(String error);
    }

    public interface StringResult {
        void onResult(String result);

        void onError(String error);
    }


}
