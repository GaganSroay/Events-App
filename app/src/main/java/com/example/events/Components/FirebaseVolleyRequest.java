package com.example.events.Components;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FirebaseVolleyRequest {

    private static final String LOCAL_URL = "http://10.0.2.2:3000/";
    private static final String URL = LOCAL_URL;
    private final Context context;
    private final RequestQueue queue;
    private String idToken = null;
    private String link;

    public FirebaseVolleyRequest(Context context, Routes route) {
        this.context = context;
        switch (route) {
            case CREATE_EVENT:
                link = URL + "create_event/";
                break;
            case JOIN_EVENT:
                link = URL + "join_event/";
                break;
            case MESSAGE:
                link = URL + "message/";
                break;
            case NEW_USER:
                link = URL + "new_user/";
                break;
        }
        queue = Volley.newRequestQueue(context);
        getIdTokken();
    }


    public FirebaseVolleyRequest(Context context, String route) {
        this.context = context;
        link = URL + route + "/";
        queue = Volley.newRequestQueue(context);
        getIdTokken();
    }

    public void getIdTokken() {
        idToken = SharedPrefs.getIdToken(context);
    }

    public void makeRequest(Map<String, String> data, GetResult callback) {
        connectToServer(data, callback);
    }

    public void makeRequest(Map<String, String> data) {
        System.out.println("making Request    " + System.currentTimeMillis());
        makeRequest(data, new GetResult() {
            @Override
            public void onResult(String result) {
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    public void connectToServer(Map<String, String> data, GetResult callback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                callback::onResult,
                error -> {
                    callback.onError("error");
                    System.out.println(error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }

            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }

        };
        queue.add(stringRequest);
    }

    public void makePostRequest(Map<String, String> data, GetResult callback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                callback::onResult,
                error -> {
                    callback.onError("error");
                    System.out.println(error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }

            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }

        };
        queue.add(stringRequest);
    }

    public void makePostRequest(GetResult callback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                link,
                callback::onResult,
                error -> {
                    callback.onError("error");
                    System.out.println(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }

        };
        queue.add(stringRequest);
    }

    public void makeGetRequest(GetResult callback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                link,
                callback::onResult,
                error -> {
                    callback.onError("error");
                    System.out.println(error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };

        addSecurityPolicy(stringRequest);
        queue.add(stringRequest);
    }

    private void addSecurityPolicy(StringRequest request) {
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private Map<String, String> getAuthHeaders() {
        Map<String, String> params = new HashMap<String, String>();
        //params.put("Content-Type", "application/json; charset=utf-8");
        params.put("token", idToken);
        return params;
    }

    public enum Routes {
        CREATE_EVENT,
        JOIN_EVENT,
        MESSAGE,
        NEW_USER
    }

    public interface GetResult {
        void onResult(String result);

        void onError(String error);
    }

}