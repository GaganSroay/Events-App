package com.example.events;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.Form.FormView;
import com.example.events.databinding.ActivityJoinEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class JoinEvent extends AppCompatActivity {

    FirebaseFirestore db;
    DocumentReference ref;

    ActivityJoinEventBinding binding;

    FormView formView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJoinEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        formView = binding.formview;

        db = FirebaseFirestore.getInstance();
        String path_to_doc = getIntent().getStringExtra(C.reference);
        ref = db.document(path_to_doc);

        ref.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

            HashMap<String, Object> form = (HashMap<String, Object>) data.get("form");
            ArrayList<Object> formData = (ArrayList<Object>) form.get("form_data");
            //System.out.println(formData.toString());

            formView.setFormDataFromServer(formData);


            //DocumentSnapshot formData = (DocumentSnapshot) snapshot.get("form");
            //HashMap<String,Object> formData = (HashMap<String, Object>) snapshot.get("form");
            //System.out.println(formData.toString());
            //if(snapshot.get())
            //FieldPath bolean;

        });

        binding.Join.setOnClickListener(v -> {
            HashMap<String, String> data = new HashMap<>();
            data.put("event_id", getIntent().getStringExtra("event_id"));
            data.put("form_data", formView.getFormResponse());

            new FirebaseVolleyRequest(this, FirebaseVolleyRequest.Routes.JOIN_EVENT)
                    .makeRequest(data, new FirebaseVolleyRequest.GetResult() {
                        @Override
                        public void onResult(String result) {

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        });
    }
}