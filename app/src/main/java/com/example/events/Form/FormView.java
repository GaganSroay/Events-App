package com.example.events.Form;

import static com.example.events.Form.Type.SUBTITLE;
import static com.example.events.Form.Type.TEXT;
import static com.example.events.Form.Type.TITLE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.events.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class FormView extends LinearLayout {

    FormData formData;
    Context context;
    LayoutInflater inflater;

    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formData = new FormData();
        this.setOrientation(VERTICAL);
    }

    public FormView(Context context) {
        super(context);
        this.context = context;
    }

    public FormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }


    public void updateFormView() {
        this.removeAllViews();
        for (FormElement element : formData.formlist) {
            switch (element.type) {
                case TEXTFIELD:
                    element.view = getTextInputField(element.title);
                    break;
                case TEXTAREA:
                    element.view = getTextInputArea(element.title);
                    break;
                case TEXT:
                case TITLE:
                case SUBTITLE:
                    element.view = getTextView(element.title, element.type);
                    break;
                case CHECKBOX:
                    element.view = getCheckbox(element.title);
            }
            this.addView(element.view);
        }
    }

    public ArrayList<FormElement> getFormData() {
        return formData.formlist;
    }

    public void setFormData(ArrayList<FormElement> data) {
        formData = new FormData(data);
        updateFormView();
    }

    public void setFormDataFromServer(ArrayList<Object> data) {
        formData = new FormData();
        for (Object obj : data) {
            HashMap<String, String> objmap = (HashMap<String, String>) obj;
            formData.addElement(
                    objmap.get("title"),
                    Type.valueOf(objmap.get("type"))
            );
        }
        updateFormView();
    }

    public void addElement(String title, Type type) {
        formData.addElement(title, type);
        updateFormView();
    }

    public String getFormResponse() {
        return formData.getResponse().toString();
    }

    private CheckBox getCheckbox(String text) {
        CheckBox cb = new CheckBox(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dipToPixel(10), 0, 0, 0);
        cb.setText(text);
        return cb;
    }

    private LinearLayout getTextInputField(String lable) {
        LinearLayout tin = (LinearLayout) inflater.inflate(R.layout.text_input_field, null);
        ((TextInputLayout) tin.findViewById(R.id.holder)).setHint(lable);
        return tin;
    }

    private LinearLayout getTextInputArea(String lable) {
        LinearLayout tin = getTextInputField(lable);
        TextInputEditText ted = tin.findViewById(R.id.field);
        ted.setMinLines(6);
        ted.setGravity(Gravity.TOP | Gravity.START);
        return tin;
    }

    private TextView getTextView(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tv.setLayoutParams(newLayoutParams());
        return tv;
    }

    private TextView getTextView(String text, Type type) {
        TextView tv = getTextView(text);
        if (type == TEXT) tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        else if (type == SUBTITLE) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.rgb(30, 30, 30));
            tv.setTypeface(null, Typeface.BOLD);
        } else if (type == TITLE) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(Color.rgb(10, 10, 10));
        }
        return tv;
    }

    private int dipToPixel(int pixel) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pixel,
                context.getResources().getDisplayMetrics()
        );
    }

    private LinearLayout.LayoutParams newLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams newLayoutParams(int top, int bottom) {
        return newLayoutParams(0, 0, top, 0, bottom);
    }

    private LinearLayout.LayoutParams newLayoutParams(int left, int top, int right, int bottom) {
        return newLayoutParams(0, left, top, right, bottom);
    }

    private LinearLayout.LayoutParams newLayoutParams(int type) {
        return newLayoutParams(type, 0, 0, 0, 0);
    }

    private LinearLayout.LayoutParams newLayoutParams(int type, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params;
        switch (type) {
            case 1:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            case 2:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            default:
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.setMargins(
                dipToPixel(left),
                dipToPixel(top),
                dipToPixel(right),
                dipToPixel(bottom));
        return params;
    }
}
