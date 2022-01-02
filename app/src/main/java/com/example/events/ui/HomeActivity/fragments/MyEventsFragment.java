package com.example.events.ui.HomeActivity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.events.Components.C;
import com.example.events.R;
import com.example.events.ViewEvents.ViewJoinedEvent;
import com.example.events.databinding.FragmentMyEventsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater);

        db = FirebaseFirestore.getInstance();
        db.collection(C.users).document(C.getNumber(getActivity())).collection(C.joined_events).get().addOnCompleteListener(task->{
            List<DocumentSnapshot> list = task.getResult().getDocuments();
            for(DocumentSnapshot item: list){
                ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.list_joined_event,null);
                ((TextView) view.findViewById(R.id.eventname)).setText(item.get(C.event_name).toString());
                ((TextView) view.findViewById(R.id.date)).setText(item.get(C.event_date).toString());
                binding.listview.addView(view);

                view.setOnClickListener(v->
                        startActivity(new Intent(getActivity(), ViewJoinedEvent.class)
                                .putExtra(C.reference,item.get(C.reference).toString()))
                );
            }
        });

        return binding.getRoot();
    }
}