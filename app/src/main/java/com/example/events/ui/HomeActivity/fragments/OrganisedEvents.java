package com.example.events.ui.HomeActivity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.CreateEvent;
import com.example.events.R;
import com.example.events.ViewEvents.ViewOrganisedEvent;
import com.example.events.databinding.FragmentOrganisedEventsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrganisedEvents extends Fragment {

    private FragmentOrganisedEventsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    OrganisedEventListAdapter adapter;
    JSONArray evenstList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganisedEventsBinding.inflate(inflater, container, false);
        binding.createevent.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreateEvent.class));
        });

        evenstList = new JSONArray();
        binding.listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        showList();
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

    private void showList() {
        FirebaseVolleyRequest request = new FirebaseVolleyRequest(getActivity(), "events/organised");
        request.makeGetRequest(new FirebaseVolleyRequest.GetResult() {
            @Override
            public void onResult(String result) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    evenstList = new JSONArray(result);
                    if (evenstList.length() == 0) {
                        binding.noEventTextView.setVisibility(View.VISIBLE);
                    } else {
                        binding.listview.setVisibility(View.VISIBLE);
                        adapter = new OrganisedEventListAdapter(evenstList);
                        binding.listview.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.noEventTextView.setVisibility(View.VISIBLE);
                    binding.noEventTextView.setText("Error retrieving events");
                }
            }

            @Override
            public void onError(String error) {
            }
        });

    }

    class OrganisedEventListAdapter extends RecyclerView.Adapter<OrganisedEventListAdapter.ViewHolder> {

        JSONArray list;

        public OrganisedEventListAdapter(JSONArray list) {
            this.list = list;
        }

        @NonNull
        @Override
        public OrganisedEventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_joined_event, null);
            return new OrganisedEventListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrganisedEventListAdapter.ViewHolder holder, int position) {
            try {
                JSONObject obj = evenstList.getJSONObject(position);
                JSONObject eventData = obj.getJSONObject("eventData");
                JSONObject data = obj.getJSONObject("data");

                String eventName = eventData.getString("event_name");
                String eventDate = eventData.getString("event_date");
                String eventTime = eventData.getString("event_time");
                String eventId = eventData.getString("event_id");

                holder.eventName.setText(eventName);
                holder.eventDate.setText(eventDate);
                holder.eventTime.setText(eventTime + ", ");


                holder.itemView.setOnClickListener(v ->
                {
                    try {
                        Intent intent = new Intent(getActivity(), ViewOrganisedEvent.class);
                        intent.putExtra(C.reference, data.getString("refString"));
                        intent.putExtra("event_id", eventId);
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list.length();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView eventName;
            TextView eventDate;
            TextView eventTime;
            View itemView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                eventName = itemView.findViewById(R.id.eventname);
                eventDate = itemView.findViewById(R.id.date);
                eventTime = itemView.findViewById(R.id.time);
            }
        }
    }
}