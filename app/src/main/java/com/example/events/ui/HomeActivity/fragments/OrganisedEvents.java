package com.example.events.ui.HomeActivity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.events.Components.C;
import com.example.events.CreateEvent;
import com.example.events.R;
import com.example.events.ViewEvents.ViewOrganisedEvent;
import com.example.events.databinding.FragmentOrganisedEventsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrganisedEvents extends Fragment {

    private FragmentOrganisedEventsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganisedEventsBinding.inflate(inflater,container,false);
        binding.createevent.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), CreateEvent.class));
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        showList();
    }

    private void showList(){
        db.collection(C.users).document(C.getNumber(getActivity())).collection(C.organised_events).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                for(DocumentSnapshot event: list){
                    ConstraintLayout item = (ConstraintLayout) getLayoutInflater().inflate(R.layout.list_organised_event,null);
                    binding.listview.removeAllViews();
                    DocumentReference ref = (DocumentReference) event.get(C.reference);
                    ref.get().addOnCompleteListener(t -> {
                        String event_name = t.getResult().get(C.event_name).toString();
                        String date = t.getResult().get(C.event_date).toString();

                        ((TextView) item.findViewById(R.id.eventname)).setText(event_name);
                        ((TextView) item.findViewById(R.id.date)).setText(date);
                        binding.listview.addView(item);
                    });
                    item.setOnClickListener(v-> startActivity(new Intent(getActivity(), ViewOrganisedEvent.class).putExtra(C.reference,ref.getPath())));
                }
            }
        });
    }
}