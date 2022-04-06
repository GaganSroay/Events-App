package com.example.events.ViewEvents.Organise;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.C;
import com.example.events.Components.Server;
import com.example.events.databinding.ActivityEditEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class EditEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DocumentReference ref;
    FirebaseFirestore db;

    ActivityEditEventBinding binding;

    Server server;
    String eventId;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        ref = db.document(getIntent().getStringExtra(C.reference));
        eventId = getIntent().getStringExtra("event_id");

        server = new Server(this);

        server.getEvent(ref.getPath(), new Server.Result() {
            @Override
            public void onResult(JSONObject result) {
                try {
                    binding.eventname.setText(result.getString("event_name"));
                    binding.date.setText(result.getString("event_date"));
                    binding.time.setText(result.getString("event_time"));
                    binding.description.setText(result.getString("event_description"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        binding.eventsavebutton.setOnClickListener(v -> {
            onSavePress();
        });


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(
                this,
                this,
                year,
                month,
                day);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(this,
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
            if (hasFocus)
                mTimePicker.show();
        });


        binding.date.setOnClickListener(v -> datePickerDialog.show());
        binding.date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                datePickerDialog.show();
        });


    }

    public void onSavePress() {
        String eventName = binding.eventname.getText().toString();
        String eventTime = binding.time.getText().toString();
        String eventDate = binding.date.getText().toString();
        String description = binding.description.getText().toString();


        HashMap<String, String> data = new HashMap<>();

        data.put(C.event_name, eventName);
        data.put(C.event_date, eventDate);
        data.put(C.event_time, eventTime);
        data.put("event_description", description);

        data.put("eventId", eventId);

        server.editEvent(data, new Server.Result() {
            @Override
            public void onResult(JSONObject result) {
                if (result.has("success"))
                    finish();
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.date.setText(dayOfMonth + "/" + month + "/" + year);
    }
}