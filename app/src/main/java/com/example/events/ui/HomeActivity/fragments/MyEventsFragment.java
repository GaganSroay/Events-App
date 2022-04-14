package com.example.events.ui.HomeActivity.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.JoinEvent;
import com.example.events.R;
import com.example.events.ViewEvents.ViewJoinedEvent;
import com.example.events.databinding.FragmentMyEventsBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;
    JoinedEventsListAdapter adapter;
    JSONArray evenstList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater);
        evenstList = new JSONArray();
        binding.listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        getList();
        binding.joinevent.setOnClickListener(v -> joinEvent());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
    }

    private void joinEvent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        alert.setTitle("Enter Event ID");
        alert.setView(edittext);
        alert.setPositiveButton("Join", (dialog, whichButton) -> {
            HashMap<String, String> data = new HashMap<>();
            data.put("event_id", edittext.getText().toString());
            String eventId = edittext.getText().toString();
            new FirebaseVolleyRequest(getActivity(), "events/query/" + eventId)
                    .makeGetRequest(new FirebaseVolleyRequest.GetResult() {
                        @Override
                        public void onResult(String result) {
                            try {
                                JSONObject res = new JSONObject(result);
                                System.out.println(res);
                                if (res.getBoolean("available")) {
                                    if (res.getBoolean("has_form")) {
                                        Intent intent = new Intent(getActivity(), JoinEvent.class);
                                        intent.putExtra(C.reference, res.getString("path"));
                                        intent.putExtra("event_id", res.getString("event_id"));
                                        System.out.println(res);
                                        startActivity(intent);
                                    } else new FirebaseVolleyRequest(
                                            getActivity(),
                                            "events/join_event/" + eventId)
                                            .makePostRequest(new FirebaseVolleyRequest.GetResult() {
                                                @Override
                                                public void onResult(String result) {
                                                    System.out.println(result);
                                                    getList();
                                                }

                                                @Override
                                                public void onError(String error) {
                                                }
                                            });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println(e.getMessage());
                            }
                        }

                        @Override
                        public void onError(String error) {
                            System.out.println("Error is " + error);
                        }
                    });
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });
        alert.show();
    }

    private void getList() {
        FirebaseVolleyRequest request = new FirebaseVolleyRequest(getActivity(), "events/joined");
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
                        adapter = new JoinedEventsListAdapter(evenstList);
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

    class JoinedEventsListAdapter extends RecyclerView.Adapter<JoinedEventsListAdapter.ViewHolder> {

        private final FirebaseMessaging messaging;
        JSONArray list;

        public JoinedEventsListAdapter(JSONArray list) {
            this.list = list;
            messaging = FirebaseMessaging.getInstance();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_joined_event, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                JSONObject obj = evenstList.getJSONObject(position);
                JSONObject eventData = obj.getJSONObject("eventData");
                JSONObject data = obj.getJSONObject("data");

                String eventName = eventData.getString("event_name");
                String eventDate = eventData.getString("event_date");
                String eventTime = eventData.getString("event_time");
                String eventId = eventData.getString("event_id");

                //messaging.subscribeToTopic(eventId);

                holder.eventName.setText(eventName);
                holder.eventDate.setText(eventDate);
                holder.eventTime.setText(eventTime + ", ");

                holder.itemView.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(getActivity(), ViewJoinedEvent.class);
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