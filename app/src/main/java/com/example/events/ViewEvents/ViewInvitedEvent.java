package com.example.events.ViewEvents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.events.Components.C;
import com.example.events.databinding.ActivityViewInvitedEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ViewInvitedEvent extends AppCompatActivity {

    private ActivityViewInvitedEventBinding binding;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewInvitedEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.document(getIntent().getStringExtra(C.reference));
        ref.get().addOnCompleteListener(task ->{
            String event_name = task.getResult().get(C.event_name).toString();
            String date = task.getResult().get(C.event_date).toString();

            binding.eventname.setText(event_name);
            binding.eventdate.setText(date);
        });

        binding.joinbutton.setOnClickListener(v->{
            ref.get().addOnCompleteListener(task ->{
                String event_name = task.getResult().get(C.event_name).toString();
                String date = task.getResult().get(C.event_date).toString();

                HashMap<String,Object> data = new HashMap<>();
                data.put(C.event_name,event_name);
                data.put(C.event_date,date);
                data.put(C.reference,ref);

                db.collection(C.users).document(C.getNumber(getApplicationContext()))
                        .collection(C.joined_events).add(data).addOnCompleteListener(t1->
                        db.document(getIntent().getStringExtra(C.prev_reference)).delete().addOnCompleteListener(t2->{
                            finish();
                        })
                );

            });

        });
    }
}