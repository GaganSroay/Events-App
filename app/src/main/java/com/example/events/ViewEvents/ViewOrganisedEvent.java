package com.example.events.ViewEvents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.events.Components.C;
import com.example.events.InviteParticipant;
import com.example.events.databinding.ActivityViewOrganisedEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewOrganisedEvent extends AppCompatActivity {

    private ActivityViewOrganisedEventBinding binding;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrganisedEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.document(getIntent().getStringExtra(C.reference));

        ref.get().addOnCompleteListener(task ->{
            String event_name = task.getResult().get(C.event_name).toString();
            String date = task.getResult().get(C.event_date).toString();

            binding.eventname.setText(event_name);
            binding.eventdate.setText(date);
        });

        binding.inviteparticipantbutton.setOnClickListener(v->
                startActivity(new Intent(this, InviteParticipant.class)
                        .putExtra(C.reference,getIntent().getStringExtra(C.reference)))
        );

    }
}