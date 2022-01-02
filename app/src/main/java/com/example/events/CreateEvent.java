package com.example.events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.events.Components.C;
import com.example.events.authentication.SignupDetails;
import com.example.events.databinding.ActivityCreateEventBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityCreateEventBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(
                this,
                //AlertDialog.THEME_HOLO_LIGHT,
                CreateEvent.this,
                year,
                month,
                day);


        binding.createbutton.setOnClickListener(v->{
            String eventName = binding.eventname.getText().toString();
            String eventdate = binding.date.getText().toString();
            String desc = binding.description.getText().toString();

            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put(C.event_name, eventName);
            userDetails.put(C.event_date, eventdate);
            userDetails.put(C.description, desc);

            db.collection(C.events).add(userDetails).addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    String id = task.getResult().getId();
                    DocumentReference ref = db.collection(C.events).document(id);

                    Map<String,Object> ev = new HashMap<>();
                    ev.put("role","admin");
                    ev.put("reference",ref);
                    db.collection(C.users).document(C.getNumber(this)).collection(C.organised_events).document(id).set(ev)
                            .addOnCompleteListener(t1->{
                                if(t1.isSuccessful()) finish();
                            });
                }
            });
        });

        binding.date.setOnClickListener(v->{
            datePickerDialog.show();
        });
        binding.date.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                datePickerDialog.show();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.date.setText(dayOfMonth+"/"+month+"/"+year);
    }
}