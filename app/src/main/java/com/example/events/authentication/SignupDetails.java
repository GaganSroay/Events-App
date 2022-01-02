package com.example.events.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.events.HomeActivity;
import com.example.events.databinding.ActivitySignupDetailsBinding;
import com.example.events.Components.C;
import com.example.events.Components.LoadingBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ActivitySignupDetailsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, SignupDetails.this,2000,0,1);
        loadingBar = LoadingBox.createSimpleLoading(this);

        binding.donebutton.setOnClickListener(v ->{
            String name = binding.name.getText().toString();
            String email = binding.email.getText().toString();
            String birth = binding.dateofbirth.getText().toString();
            String phone = getIntent().getStringExtra("phone");

            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put(C.name, name);
            userDetails.put(C.email, email);
            userDetails.put(C.birth, birth);
            userDetails.put(C.phone,phone);

            if(auth.getCurrentUser() != null){
                loadingBar.show();
                db.collection(C.users).document(phone).set(userDetails).addOnSuccessListener(aVoid ->{
                    loadingBar.dismiss();
                    startActivity(new Intent(SignupDetails.this, HomeActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    loadingBar.dismiss();
                    System.out.println("Exception");
                    System.out.println(e.getMessage());
                });
            }
        });

        binding.dateofbirth.setOnClickListener(v->{
            datePickerDialog.show();
        });
        binding.dateofbirth.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                datePickerDialog.show();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.dateofbirth.setText(dayOfMonth+"/"+month+"/"+year);
    }
}