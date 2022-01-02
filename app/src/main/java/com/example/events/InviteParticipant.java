package com.example.events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.events.Components.C;
import com.example.events.databinding.ActivityInviteParticipantBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class InviteParticipant extends AppCompatActivity {

    private ActivityInviteParticipantBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteParticipantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        LinearLayout firestItem = (LinearLayout) getLayoutInflater().inflate(R.layout.list_invite_participant,null);
        firestItem.findViewById(R.id.cancel_button).setOnClickListener(v->((EditText) firestItem.findViewById(R.id.participant_number)).setText(""));
        binding.numberlist.addView(firestItem);
        binding.addfield.setOnClickListener(v->{
            LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.list_invite_participant,null);
            item.findViewById(R.id.cancel_button).setOnClickListener(v1 -> binding.numberlist.removeView(item));
            binding.numberlist.addView(item);
        });
        binding.donebutton.setOnClickListener(v->{
            String myNum = C.getNumber(getApplicationContext());

            ArrayList<String> numbers = getNumbers();
            WriteBatch batch = db.batch();

            for(String num: numbers){
                DocumentReference ref = db.collection(C.users).document(num).collection(C.events_invitations).document();
                HashMap<String,Object> data = new HashMap<>();
                data.put("role","participant");
                data.put("from",myNum);
                data.put(C.reference,getIntent().getStringExtra(C.reference));
                batch.set(ref,data);
            }
            batch.commit().addOnCompleteListener(task->{
                finish();
                if(task.isSuccessful()) System.out.println("Sucess");
                else System.out.println("Failed");
            });
        });
    }

    private ArrayList<String> getNumbers(){
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<binding.numberlist.getChildCount();i++){
            String num = ((EditText) binding.numberlist.getChildAt(i).findViewById(R.id.participant_number)).getText().toString();
            if(num.length()>0) list.add("+91"+num);
        }
        return list;
    }
}