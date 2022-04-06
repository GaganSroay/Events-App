package com.example.events.ui.HomeActivity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.events.Components.C;
import com.example.events.R;
import com.example.events.ViewEvents.ViewInvitedEvent;
import com.example.events.databinding.FragmentInvitationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InvitationsFragment extends Fragment {

    private FragmentInvitationsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvitationsBinding.inflate(inflater);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        db.collection(C.users)
                .document(auth.getUid())
                .collection(C.events_invitations)
                .get().addOnCompleteListener(task -> {
            List<DocumentSnapshot> list = task.getResult().getDocuments();
            for (DocumentSnapshot item : list) {
                db.document(item.getString(C.reference)).get().addOnCompleteListener(t -> {
                    ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.list_invitations, null);
                    ((TextView) view.findViewById(R.id.eventName)).setText(t.getResult().get(C.event_name).toString());
                    ((TextView) view.findViewById(R.id.datetime)).setText(t.getResult().get(C.event_date).toString());
                    binding.invitationslist.addView(view);

                    view.setOnClickListener(v ->
                            startActivity(new Intent(getActivity(), ViewInvitedEvent.class)
                                    .putExtra(C.reference,item.getString(C.reference))
                                    .putExtra(C.prev_reference,item.getReference().getPath()))
                    );
                });
            }

        });
        return binding.getRoot();
    }
}