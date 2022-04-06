package com.example.events.Form;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.events.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FormElement implements Serializable {
    public Type type;
    public String title;
    transient View view;

    public FormElement(String title, Type type) {
        this.type = type;
        this.title = title;
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject item = new JSONObject();
        item.put("title", title);
        item.put("type", type.toString());
        return item;
    }


    public JSONObject getJsonResponst() throws JSONException {
        JSONObject item = new JSONObject();
        item.put("title", title);
        item.put("type", type.toString());
        switch (type) {
            case TITLE:
            case SUBTITLE:
            case TEXT:
                item.put("response", ((TextView) view).getText().toString());
                break;
            case TEXTAREA:
            case TEXTFIELD:
                item.put("response", ((TextInputEditText) view.findViewById(R.id.field)).getText().toString());
                break;
            case CHECKBOX:
                item.put("response", ((CheckBox) view).isChecked());
                break;
        }
        return item;
    }

}
