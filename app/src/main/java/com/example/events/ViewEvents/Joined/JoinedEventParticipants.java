package com.example.events.ViewEvents.Joined;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.ViewEvents.ParticipantList;
import com.example.events.ViewEvents.Participants;
import com.example.events.ViewEvents.ParticipantsAdapter;
import com.example.events.databinding.FragmentJoinedEventParticipantsBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinedEventParticipants extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;
    LinearLayout view;

    FragmentJoinedEventParticipantsBinding binding;
    ParticipantList pList;
    RecyclerView listview;
    ParticipantsAdapter adapter;

    public static JoinedEventParticipants newInstance(String param1) {
        JoinedEventParticipants fragment = new JoinedEventParticipants();
        Bundle args = new Bundle();
        args.putString(C.reference, param1);
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
        binding = FragmentJoinedEventParticipantsBinding.inflate(inflater);

        listview = binding.listview;
        pList = new ParticipantList();
        adapter = new ParticipantsAdapter(pList);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.setAdapter(adapter);

        ref.collection("participants").addSnapshotListener((value, error) -> {
            for (DocumentChange doc : value.getDocumentChanges())
                pList.add(new Participants(doc.getDocument()));

            synchronized (adapter) {
                adapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }
}