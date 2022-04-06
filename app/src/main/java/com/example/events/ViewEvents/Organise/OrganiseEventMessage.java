package com.example.events.ViewEvents.Organise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.ViewEvents.ChatAdapter;
import com.example.events.ViewEvents.Message;
import com.example.events.databinding.FragmentOrganiseEventMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class OrganiseEventMessage extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;
    FragmentOrganiseEventMessageBinding binding;
    FirebaseVolleyRequest request;
    ArrayList<Message> messages;

    RecyclerView listview;
    ChatAdapter adapter;
    String UID;
    boolean listening = false;


    public static OrganiseEventMessage newInstance(String ref) {
        OrganiseEventMessage fragment = new OrganiseEventMessage();
        Bundle args = new Bundle();
        args.putString(C.reference, ref);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        ref = db.document(getArguments().getString(C.reference));
        UID = FirebaseAuth.getInstance().getUid();

        HashMap<String, String> eventPath = new HashMap<>();
        eventPath.put("path", ref.getPath());

        new FirebaseVolleyRequest(getActivity(), "events/get_event_id")
                .makePostRequest(eventPath, new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        try {
                            JSONObject res = new JSONObject(result);
                            String eventId = res.getString("event_id");
                            request = new FirebaseVolleyRequest(getActivity(), "message/" + eventId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });

        messages = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrganiseEventMessageBinding.inflate(inflater);
        listview = binding.recyclerview;
        adapter = new ChatAdapter(messages, getActivity());
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.setAdapter(adapter);

        binding.sendButton.setOnClickListener(v -> {
            String message = binding.messageField.getText().toString();
            if (message.length() > 0) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message", message);
                request.makePostRequest(data, new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        System.out.println("############    " + result);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                binding.messageField.setText("");
            }
        });

        if (!listening)
            ref.collection("messages").addSnapshotListener((value, error) -> {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    boolean me = doc.getDocument().get("user").equals(UID);
                    messages.add(new Message(doc.getDocument(), me));
                }
                synchronized (adapter) {
                    adapter.notifyDataSetChanged();
                }
            });
        listening = true;

        return binding.getRoot();
    }
}