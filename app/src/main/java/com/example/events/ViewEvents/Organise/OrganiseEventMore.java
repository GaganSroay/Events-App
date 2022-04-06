package com.example.events.ViewEvents.Organise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.events.Components.C;
import com.example.events.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganiseEventMore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganiseEventMore extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;

    public static OrganiseEventMore newInstance(String ref) {
        OrganiseEventMore fragment = new OrganiseEventMore();
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
        return inflater.inflate(R.layout.fragment_organise_event_more, container, false);
    }
}