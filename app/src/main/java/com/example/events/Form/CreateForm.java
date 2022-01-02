package com.example.events.Form;

import static com.example.events.Form.Type.CHECKBOX;
import static com.example.events.Form.Type.SUBTITLE;
import static com.example.events.Form.Type.TEXT;
import static com.example.events.Form.Type.TEXTAREA;
import static com.example.events.Form.Type.TEXTFIELD;
import static com.example.events.Form.Type.TITLE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.events.R;
import com.example.events.databinding.ActivityCreateFormBinding;
import com.example.events.databinding.CreateformAddElementBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateForm extends AppCompatActivity {

    private ActivityCreateFormBinding binding;
    private FormData formdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        formdata = new FormData();
    }

    public void addElementButton(View view){
        formdata.printString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout container = (LinearLayout) getLayoutInflater().inflate(R.layout.createform_add_element,null);
        CreateformAddElementBinding cb = CreateformAddElementBinding.bind(container);

        builder.setView(container);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alert = builder.show();

        cb.title.setOnClickListener(v->{
            addTextElement(TITLE);
            alert.dismiss();
        });
        cb.subTitle.setOnClickListener(v->{
            addTextElement(SUBTITLE);
            alert.dismiss();
        });
        cb.text.setOnClickListener(v->{
            addTextElement(TEXT);
            alert.dismiss();
        });
        cb.textinput.setOnClickListener(v->{
            addInputElement(TEXTFIELD);
            alert.dismiss();
        });
        cb.textarea.setOnClickListener(v->{
            addInputElement(TEXTAREA);
            alert.dismiss();
        });
        cb.checkbox.setOnClickListener(v->{
            addInputElement(CHECKBOX);
            alert.dismiss();
        });
    }

    private void addInputElement(Type type){
        final EditText input = new EditText(this);
        input.setHint("Enter Here");
        input.setLayoutParams(newLayoutParams(20,0,20,0));

        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(newLayoutParams());
        container.addView(input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter name of field");
        builder.setView(container);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String title = input.getText().toString();
            switch (type){
                case TEXTAREA: addViewtoList(getTextInputArea(input.getText().toString()),title, type);break;
                case CHECKBOX: addViewtoList(getCheckbox(input.getText().toString()),title, type);break;
                default: addViewtoList(getTextInputField(input.getText().toString()),title, type);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addViewtoList(View view,String title,Type type){
        binding.formview.addView(view);
        formdata.addElement(title, type);
    }


    private void addTextElement(Type type){
        final EditText input = new EditText(this);
        input.setHint("Enter Here");
        input.setLayoutParams(newLayoutParams(20,0,20,0));
        String title;
        switch (type){
            case SUBTITLE: title = "Enter Sub Title here";break;
            case TITLE: title = "Enter Title here";break;
            default : title = "Enter Text here";
        }
        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(newLayoutParams());
        container.addView(input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setView(container);
        builder.setPositiveButton("OK", (dialog, which) -> {
            addViewtoList(getTextView(input.getText().toString(),type),input.getText().toString(), type);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private CheckBox getCheckbox(String text){
        CheckBox cb = new CheckBox(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dipToPixel(10),0,0,0);
        cb.setText(text);
        return cb;
    }

    private LinearLayout getTextInputField(String lable){
        LinearLayout tin = (LinearLayout) getLayoutInflater().inflate(R.layout.text_input_field,null);
        ((TextInputLayout) tin.findViewById(R.id.holder)).setHint(lable);
        return tin;
    }

    private LinearLayout getTextInputArea(String lable){
        LinearLayout tin = getTextInputField(lable);
        TextInputEditText ted = ((TextInputEditText) tin.findViewById(R.id.field));
        ted.setMinLines(6);
        ted.setGravity(Gravity.TOP|Gravity.START);
        return tin;
    }

    private TextView getTextView(String text){
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        tv.setLayoutParams(newLayoutParams());
        return tv;
    }

    private TextView getTextView(String text,Type type){
        TextView tv = getTextView(text);
        if(type == TEXT) tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        else if(type == SUBTITLE) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            tv.setTextColor(Color.rgb(30,30,30));
            tv.setTypeface(null, Typeface.BOLD);
        }
        else if(type == TITLE) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(Color.rgb(10,10,10));
        }
        return tv;
    }

    private int dipToPixel(int pixel){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pixel,
                getResources().getDisplayMetrics()
        );
    }

    private LinearLayout.LayoutParams newLayoutParams(){ return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); }
    private LinearLayout.LayoutParams newLayoutParams(int top,int bottom){ return newLayoutParams(0,0,top,0,bottom); }
    private LinearLayout.LayoutParams newLayoutParams(int left,int top,int right,int bottom){ return newLayoutParams(0,left,top,right,bottom); }
    private LinearLayout.LayoutParams newLayoutParams(int type){ return newLayoutParams(type,0,0,0,0); }
    private LinearLayout.LayoutParams newLayoutParams(int type,int left,int top,int right,int bottom){
        LinearLayout.LayoutParams params;
        switch(type){
            case 1: params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            case 2: params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            default: params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.setMargins(
                dipToPixel(left),
                dipToPixel(top),
                dipToPixel(right),
                dipToPixel(bottom));
        return params;
    }

}