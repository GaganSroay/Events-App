package com.example.events.Form;

import static com.example.events.Form.Type.CHECKBOX;
import static com.example.events.Form.Type.SUBTITLE;
import static com.example.events.Form.Type.TEXT;
import static com.example.events.Form.Type.TEXTAREA;
import static com.example.events.Form.Type.TEXTFIELD;
import static com.example.events.Form.Type.TITLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.C;
import com.example.events.CreateEvent;
import com.example.events.R;
import com.example.events.databinding.ActivityCreateFormBinding;
import com.example.events.databinding.CreateformAddElementBinding;

public class CreateForm extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateFormBinding binding;
    private FormData formdata;
    FormView formView;
    CreateformAddElementBinding cb;
    AlertDialog.Builder builder;
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateFormBinding.inflate(getLayoutInflater());
        formView = binding.createFormview;
        setContentView(binding.getRoot());
        formdata = new FormData();

        binding.createbutton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateForm.this, CreateEvent.class);
            intent.putExtra("formdata", formView.getFormData());
            intent.putExtra("hasForm", true);
            intent.putExtra(C.event_name, getIntent().getStringExtra(C.event_name));
            intent.putExtra(C.description, getIntent().getStringExtra(C.description));
            intent.putExtra(C.event_date, getIntent().getStringExtra(C.event_date));
            intent.putExtra(C.event_time, getIntent().getStringExtra(C.event_time));
            startActivity(intent);
            finish();
        });

    }

    public void addElementButton(View view) {

        LinearLayout container = (LinearLayout) getLayoutInflater().inflate(R.layout.createform_add_element, null);
        cb = CreateformAddElementBinding.bind(container);
        builder = new AlertDialog.Builder(this);
        builder.setView(container);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        alert = builder.show();
        cb.title.setOnClickListener(this);
        cb.subTitle.setOnClickListener(this);
        cb.text.setOnClickListener(this);
        cb.textinput.setOnClickListener(this);
        cb.textarea.setOnClickListener(this);
        cb.checkbox.setOnClickListener(this);

    }

    void recieveMetadata(Type type) {
        new GetFormMetadata(
                this,
                (d, t) -> formView.addElement(d, t),
                type
        );
        alert.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (cb.title.equals(v)) recieveMetadata(TITLE);
        else if (cb.subTitle.equals(v)) recieveMetadata(SUBTITLE);
        else if (cb.text.equals(v)) recieveMetadata(TEXT);
        else if (cb.textinput.equals(v)) recieveMetadata(TEXTFIELD);
        else if (cb.textarea.equals(v)) recieveMetadata(TEXTAREA);
        else if (cb.checkbox.equals(v)) recieveMetadata(CHECKBOX);
    }

    static class GetFormMetadata {
        Activity activity;

        public GetFormMetadata(Activity activity, OnResult onResult, Type type) {
            this.activity = activity;

            final EditText input = new EditText(activity);
            input.setHint("Enter Here");
            input.setLayoutParams(newLayoutParams(20, 0, 20, 0));
            LinearLayout container1 = new LinearLayout(activity);
            container1.setLayoutParams(newLayoutParams());
            container1.addView(input);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setMessage("Enter name of field");
            builder1.setView(container1);
            builder1.setPositiveButton("OK", (dialog, which) -> {
                String title = input.getText().toString();
                onResult.onData(title, type);
            });
            builder1.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder1.show();
        }

        private int dipToPixel(int pixel) {
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    pixel,
                    activity.getResources().getDisplayMetrics()
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

        interface OnResult {
            void onData(String data, Type type);
        }

    }


}