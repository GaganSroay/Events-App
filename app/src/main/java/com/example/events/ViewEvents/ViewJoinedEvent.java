package com.example.events.ViewEvents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.events.databinding.ActivityViewJoinedEventBinding;

public class ViewJoinedEvent extends AppCompatActivity {

    private ActivityViewJoinedEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewJoinedEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}