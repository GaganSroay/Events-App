package com.example.events.ViewEvents.Organise;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.events.Components.C;
import com.example.events.Components.Server;
import com.example.events.databinding.ActivityEditEventBinding;
import com.example.events.databinding.BodyImageElementBinding;
import com.example.events.databinding.BodyLinkElementBinding;
import com.example.events.databinding.EditEventAddViewBinding;
import com.example.events.databinding.EditEventLinkViewBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class EditEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

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
        binding.eventsavebutton.setOnClickListener(v -> onSavePress());

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
            if (hasFocus) mTimePicker.show();
        });

        binding.date.setOnClickListener(v -> datePickerDialog.show());
        binding.date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) datePickerDialog.show();
        });

        binding.starteventbutton.setOnClickListener(v -> {
            server.startEvent(eventId, new Server.Result() {
                @Override
                public void onResult(JSONObject result) {
                    System.out.println(result.toString());
                }

                @Override
                public void onError(String error) {
                    System.err.println(error);
                }
            });
        });

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addButton(View view) {

        AlertDialog.Builder builder;
        EditEventAddViewBinding addViewBinding = EditEventAddViewBinding.inflate(getLayoutInflater());
        builder = new AlertDialog.Builder(this);
        builder.setView(addViewBinding.getRoot());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.show();
        addViewBinding.link.setOnClickListener(v -> {
            alert.dismiss();
            EditEventLinkViewBinding lb = EditEventLinkViewBinding.inflate(getLayoutInflater());
            AlertDialog.Builder b1 = new AlertDialog.Builder(this);
            b1.setView(lb.getRoot());
            b1.setPositiveButton("Okay", (dialog, which) -> {
                String name = lb.name.getText().toString();
                String data = lb.link.getText().toString();
                String type = "link";

                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("name", name);
                reqData.put("type", type);
                reqData.put("data", data);
                reqData.put("event_id", eventId);

                server.addBodyElement(reqData, new Server.Result() {
                    @Override
                    public void onResult(JSONObject result) {
                        System.out.println(result);
                        if (!result.has("error"))
                            alert.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println(error);
                    }
                });
            });
            b1.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            b1.show();

        });
        addViewBinding.image.setOnClickListener(this);
        addViewBinding.document.setOnClickListener(this);
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
                if (result.has("success")) finish();
            }

            @Override
            public void onError(String error) {
                System.out.println("Error is " + error);
            }
        });

    }

    private void updateUI() {
        server.getEvent(ref.getPath(), new Server.Result() {
            @Override
            public void onResult(JSONObject result) {
                try {
                    binding.eventname.setText(result.getString("event_name"));
                    binding.date.setText(result.getString("event_date"));
                    binding.time.setText(result.getString("event_time"));
                    //binding.description.setText(result.getString("event_description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        new Server(this)
                .getEventBody(eventId, new Server.Result() {
                    @Override
                    public void onResult(JSONObject result) throws JSONException {
                        binding.body.removeAllViews();
                        String description = result.getString("description");
                        binding.description.setText(description);
                        if (result.has("body")) {
                            JSONArray bodyArray = result.getJSONArray("body");
                            if (bodyArray.length() > 0) {
                                for (int i = 0; i < bodyArray.length(); i++) {
                                    JSONObject bodyElement = bodyArray.getJSONObject(i);
                                    addBodyElement(binding.body, bodyElement);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println(error);
                    }
                });
    }

    public void addBodyElement(LinearLayout container, JSONObject bodyElement) throws JSONException {
        String type = bodyElement.getString("type");
        String name = bodyElement.getString("name");
        String data = bodyElement.getString("data");
        if (type.equals("link")) {
            BodyLinkElementBinding linkBinding = BodyLinkElementBinding.inflate(getLayoutInflater());
            linkBinding.linkTitle.setText(name);
            linkBinding.linkLink.setText(data);
            container.addView(linkBinding.getRoot());
        } else if (type.equals("image")) {
            BodyImageElementBinding linkBinding = BodyImageElementBinding.inflate(getLayoutInflater());
            linkBinding.imageTitle.setText(name);
            Glide.with(this)
                    .load(data)
                    .into(linkBinding.imageImage);
            container.addView(linkBinding.getRoot());

            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.date.setText(dayOfMonth + "/" + month + "/" + year);
    }

    @Override
    public void onClick(View v) {

    }
}