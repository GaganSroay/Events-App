package com.example.events.ViewEvents.Organise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.databinding.ActivityParticipantResponseBinding;

public class ParticipantResponse extends AppCompatActivity {

    ActivityParticipantResponseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParticipantResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}