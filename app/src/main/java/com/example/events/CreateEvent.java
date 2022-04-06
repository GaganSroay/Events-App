package com.example.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.Form.CreateForm;
import com.example.events.Form.FormData;
import com.example.events.Form.FormElement;
import com.example.events.databinding.ActivityCreateEventBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityCreateEventBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DatePickerDialog datePickerDialog;
    private boolean hasForm = false;
    private ArrayList<FormElement> formElements;
    private FormData formData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getBooleanExtra("hasForm", false)) {
            hasForm = true;
            formElements = (ArrayList<FormElement>) getIntent().getSerializableExtra("formdata");
            formData = new FormData(formElements);
            binding.formview.setFormData(formElements);
            binding.createformbutton.setVisibility(View.GONE);

        }

        binding.eventname.setText(getIntent().getStringExtra(C.event_name));
        binding.date.setText(getIntent().getStringExtra(C.event_date));
        binding.time.setText(getIntent().getStringExtra(C.event_time));
        binding.description.setText(getIntent().getStringExtra(C.description));


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(
                this,
                CreateEvent.this,
                year,
                month,
                day);


        binding.createbutton.setOnClickListener(v-> {
            System.out.println("Button CLicked    " + System.currentTimeMillis());
            String eventName = binding.eventname.getText().toString();
            String eventdate = binding.date.getText().toString();
            String desc = binding.description.getText().toString();
            String eventTime = binding.time.getText().toString();


            Map<String, String> userDetails = new HashMap<>();
            userDetails.put(C.event_name, eventName);
            userDetails.put(C.event_date, eventdate);
            userDetails.put(C.event_time, eventTime);
            userDetails.put(C.description, desc);
            userDetails.put("has_form", String.valueOf(hasForm));
            userDetails.put("ticket_required", "true");

            if (hasForm) {
                JSONArray jsonArray = formData.getJsonArray();
                System.out.println("json String is    " + jsonArray.toString());
                userDetails.put("form_data", jsonArray.toString());
            }

            System.out.println(userDetails);


            FirebaseVolleyRequest request = new FirebaseVolleyRequest(this, "events/create_event");

            request.makePostRequest(userDetails, new FirebaseVolleyRequest.GetResult() {
                @Override
                public void onResult(String result) {
                    System.out.println(result);
                    finish();
                }

                @Override
                public void onError(String error) {

                }
            });

        });


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(CreateEvent.this,
                (timePicker, h, m) -> {
                    boolean isPM = (h >= 12);
                    binding.time.
                            setText(String.format(
                                    "%02d:%02d %s",
                                    (h == 12 || h == 0) ? 12 : h % 12,
                                    minute,
                                    isPM ? "PM" : "AM"));
                },
                hour,
                minute,
                false
        );
        mTimePicker.setTitle("Select Time");

        binding.time.setOnClickListener(v -> mTimePicker.show());
        binding.time.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) mTimePicker.show();
        });

        binding.date.setOnClickListener(v -> datePickerDialog.show());
        binding.date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) datePickerDialog.show();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.date.setText(dayOfMonth + "/" + month + "/" + year);
    }

    public void createFormButton(View view) {
        Intent intent = new Intent(CreateEvent.this, CreateForm.class);
        intent.putExtra(C.event_name, binding.eventname.getText().toString());
        intent.putExtra(C.description, binding.description.getText().toString());
        intent.putExtra(C.event_date, binding.date.getText().toString());
        intent.putExtra(C.event_time, binding.time.getText().toString());
        startActivity(intent);
        finish();
    }


}