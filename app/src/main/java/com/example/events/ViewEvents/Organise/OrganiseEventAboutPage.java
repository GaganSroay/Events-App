package com.example.events.ViewEvents.Organise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.events.Components.C;
import com.example.events.Components.Server;
import com.example.events.databinding.BodyImageElementBinding;
import com.example.events.databinding.BodyLinkElementBinding;
import com.example.events.databinding.FragmentOrganiseEventAboutPageBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrganiseEventAboutPage extends Fragment {

    FragmentOrganiseEventAboutPageBinding binding;
    DocumentReference ref;
    FirebaseFirestore db;
    String eventId;
    JSONObject body = null;

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
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        new Server(getActivity())
                .getEventBody(eventId, new Server.Result() {
                    @Override
                    public void onResult(JSONObject result) throws JSONException {
                        binding.body.removeAllViews();
                        String description = result.getString("description");
                        binding.description.setText(description);
                        if (result.has("body")) {
                            JSONArray bodyArray = result.getJSONArray("body");
                            if (bodyArray.length() > 0) {
                                for (int i = 0; i < bodyArray.length(); i++) {
                                    JSONObject bodyElement = bodyArray.getJSONObject(i);
                                    addBodyElement(binding.body, bodyElement);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println(error);
                    }
                });

    }

    public void addBodyElement(LinearLayout container, JSONObject bodyElement) throws JSONException {
        String type = bodyElement.getString("type");
        String name = bodyElement.getString("name");
        String data = bodyElement.getString("data");
        if (type.equals("link")) {
            BodyLinkElementBinding linkBinding = BodyLinkElementBinding.inflate(getLayoutInflater());
            linkBinding.linkTitle.setText(name);
            linkBinding.linkLink.setText(data);
            container.addView(linkBinding.getRoot());
        } else if (type.equals("image")) {
            BodyImageElementBinding linkBinding = BodyImageElementBinding.inflate(getLayoutInflater());
            linkBinding.imageTitle.setText(name);
            Glide.with(getActivity())
                    .load(data)
                    .into(linkBinding.imageImage);
            container.addView(linkBinding.getRoot());

            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
        }
    }
}

