package com.example.events.ViewEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.ViewEvents.Organise.OrganiseEventAboutPage;
import com.example.events.ViewEvents.Organise.OrganiseEventMessage;
import com.example.events.ViewEvents.Organise.OrganiseEventParticipant;
import com.example.events.ViewEvents.Organise.TicketScannerActivity;
import com.example.events.databinding.ActivityViewOrganisedEventBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewOrganisedEvent extends AppCompatActivity {

    private ActivityViewOrganisedEventBinding binding;
    private FirebaseFirestore db;
    DocumentReference ref;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrganisedEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        ref = db.document(getIntent().getStringExtra(C.reference));
        eventId = getIntent().getStringExtra("event_id");

        ViewOrganisedEventPagerAdapter adapter = new ViewOrganisedEventPagerAdapter(
                getSupportFragmentManager(),
                ref.getPath(),
                eventId
        );
        binding.viewPager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.viewPager);

        updateUI();

        binding.scannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketScannerActivity.class);
            intent.putExtra("event_id", eventId);
            startActivity(intent);
        });
        /*binding.inviteparticipantbutton.setOnClickListener(v->
                startActivity(new Intent(this, InviteParticipant.class)
                        .putExtra(C.reference,getIntent().getStringExtra(C.reference)))
        );*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        new FirebaseVolleyRequest(ViewOrganisedEvent.this, "events_service/get_event_role/" + eventId)
                .makeGetRequest(new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        try {
                            JSONObject res = new JSONObject(result);
                            String role = res.getString("role");
                            if (role.equals("a")) {
                                binding.role.setVisibility(View.VISIBLE);
                                binding.role.setText("Role : Admin");
                            } else if (role.equals("p")) {
                                binding.role.setVisibility(View.INVISIBLE);
                                binding.role.setText("Role : Participant");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
        HashMap<String, String> path = new HashMap<>();
        path.put("path", ref.getPath());
        new FirebaseVolleyRequest(this, "events/get_event")
                .makePostRequest(path, new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        try {
                            JSONObject res = new JSONObject(result);
                            String eventName = res.getString("event_name");
                            String eventDate = res.getString("event_date");
                            String eventTime = res.getString("event_time");

                            binding.eventname.setText(eventName);
                            binding.eventdate.setText(eventDate + ",  " + eventTime);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public static class ViewOrganisedEventPagerAdapter extends FragmentPagerAdapter {

        private final String ref;
        private final String[] TAB_TITLES = new String[]{
                "About",
                "Messages",
                "Participants"
        };
        String eventId;

        public ViewOrganisedEventPagerAdapter(FragmentManager fm, String ref, String eventId) {
            super(fm);
            this.ref = ref;
            this.eventId = eventId;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OrganiseEventAboutPage.newInstance(ref, eventId);
                case 1:
                    return OrganiseEventMessage.newInstance(ref);
                default:
                    return OrganiseEventParticipant.newInstance(ref, eventId);
            }
        }

        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }

        public int getCount() {
            return TAB_TITLES.length;
        }

    }
}