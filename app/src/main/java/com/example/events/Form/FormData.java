package com.example.events.Form;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FormData {
    JSONArray list;
    public FormData(){
        list = new JSONArray();
    }

    public void addElement(String title,Type type) {
        try {
            list.put(new JSONObject().put(type.toString(),title));
        } catch (JSONException e) { }
    }

    public void printString(){
        System.out.println("############    "+list.toString());
    }


}
