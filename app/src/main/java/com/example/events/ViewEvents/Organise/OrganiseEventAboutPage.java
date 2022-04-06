package com.example.events.ViewEvents.Organise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.events.Components.C;
import com.example.events.Components.Server;
import com.example.events.databinding.FragmentOrganiseEventAboutPageBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class OrganiseEventAboutPage extends Fragment {

    FragmentOrganiseEventAboutPageBinding binding;
    DocumentReference ref;
    FirebaseFirestore db;
    String eventId;

    public static OrganiseEventAboutPage newInstance(String ref, String eventId) {
        OrganiseEventAboutPage fragment = new OrganiseEventAboutPage();
        Bundle args = new Bundle();
        args.putString(C.reference, ref);
        args.putString("event_id", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        ref = db.document(getArguments().getString(C.reference));
        eventId = getArguments().getString("event_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrganiseEventAboutPageBinding.inflate(inflater);

        binding.editEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditEvent.class);
            intent.putExtra(C.reference, ref.getPath());
            intent.putExtra("event_id", eventId);
            startActivity(intent);
        });

        updateUI();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        new Server(getActivity())
                .getEvent(ref.getPath(), new Server.Result() {
                    @Override
                    public void onResult(JSONObject result) {
                        try {
                            binding.description.setText(result.getString("event_description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println(error);
                    }
                });
    }
}

