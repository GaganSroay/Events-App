package com.example.events.ViewEvents.Joined;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.events.Components.C;
import com.example.events.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinedEventMessages extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;

    public static JoinedEventMessages newInstance(String ref) {
        JoinedEventMessages fragment = new JoinedEventMessages();
        Bundle args = new Bundle();
        args.putString(C.reference, ref);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null)
            ref = db.document(getArguments().getString(C.reference));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_joined_event_messages, container, false);
    }


}