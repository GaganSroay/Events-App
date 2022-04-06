package com.example.events.Form;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FormData {
    ArrayList<FormElement> formlist;
    JSONArray jsonArray;
    JSONArray jsonResponse;

    {
        formlist = new ArrayList<>();
        jsonArray = new JSONArray();
        jsonResponse = new JSONArray();
    }

    public FormData(ArrayList<FormElement> formlist) {
        this.formlist = formlist;
    }

    public FormData() {

    }

    public void addElement(String title, Type type) {
        formlist.add(new FormElement(title, type));
    }

    public JSONArray getJsonArray() {
        int i = 0;
        for (FormElement element : formlist) {
            try {
                JSONObject obj = element.getJsonObject();
                obj.put("sno", String.valueOf(i));
                jsonArray.put(obj);
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    public JSONArray getResponse() {
        int i = 0;
        for (FormElement element : formlist) {
            try {
                JSONObject obj = element.getJsonResponst();
                obj.put("sno", String.valueOf(i));
                jsonResponse.put(obj);
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonResponse;
    }

}
