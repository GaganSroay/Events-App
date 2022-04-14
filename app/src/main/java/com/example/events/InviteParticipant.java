package com.example.events;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.C;
import com.example.events.Components.Server;
import com.example.events.databinding.ActivityInviteParticipantBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InviteParticipant extends AppCompatActivity {

    private ActivityInviteParticipantBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteParticipantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ref = db.document(getIntent().getStringExtra(C.reference));

        LinearLayout firestItem = (LinearLayout) getLayoutInflater().inflate(R.layout.list_invite_participant,null);
        firestItem.findViewById(R.id.cancel_button).setOnClickListener(v->((EditText) firestItem.findViewById(R.id.participant_number)).setText(""));
        binding.numberlist.addView(firestItem);
        binding.addfield.setOnClickListener(v->{
            LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.list_invite_participant,null);
            item.findViewById(R.id.cancel_button).setOnClickListener(v1 -> binding.numberlist.removeView(item));
            binding.numberlist.addView(item);
        });
        binding.donebutton.setOnClickListener(v-> {
            ArrayList<String> numbers = getNumbers();

            HashMap<String, String> data = new HashMap<>();
            JSONArray numJSONarray = new JSONArray();
            for (String num : numbers)
                numJSONarray.put(num);
            data.put("numbers", numJSONarray.toString());

            new Server(this).inviteParticipant(data, new Server.Result() {
                @Override
                public void onResult(JSONObject result) throws JSONException {

                }

                @Override
                public void onError(String error) {
                    System.out.println(error);
                }
            });

            /*
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
            });*/
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