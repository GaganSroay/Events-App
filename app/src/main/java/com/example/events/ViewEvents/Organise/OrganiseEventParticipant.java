package com.example.events.ViewEvents.Organise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.ViewEvents.ParticipantList;
import com.example.events.ViewEvents.Participants;
import com.example.events.ViewEvents.ParticipantsAdapter;
import com.example.events.databinding.FragmentOrganiseEventParticipantBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganiseEventParticipant extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;
    FragmentOrganiseEventParticipantBinding binding;
    ParticipantList pList;


    RecyclerView listview;
    ParticipantsAdapter adapter;


    public static OrganiseEventParticipant newInstance(String ref) {
        OrganiseEventParticipant fragment = new OrganiseEventParticipant();
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
        binding = FragmentOrganiseEventParticipantBinding.inflate(inflater);
        listview = binding.listview;
        pList = new ParticipantList();
        adapter = new ParticipantsAdapter(pList);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.setAdapter(adapter);

        ref.collection("participants").addSnapshotListener((value, error) -> {
            for (DocumentChange doc : value.getDocumentChanges()) {
                Participants part = new Participants(doc.getDocument());
                pList.add(part);
            }
            synchronized (adapter) {
                adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }


}