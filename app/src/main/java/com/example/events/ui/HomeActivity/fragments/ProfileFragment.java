package com.example.events.ui.HomeActivity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.events.databinding.FragmentProfileBinding;
import com.example.events.Components.C;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db.collection(C.users).document(C.getNumber(getActivity())).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                binding.name.setText(document.get(C.name).toString());
                binding.email.setText(document.get(C.email).toString());
                binding.phone.setText(document.get(C.phone).toString());
            }
        });
        return binding.getRoot();
    }
}