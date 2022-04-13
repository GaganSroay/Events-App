package com.example.events.ViewEvents.Organise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.Server;
import com.example.events.databinding.ActivityTicketScannerBinding;

import org.json.JSONObject;

import java.util.HashMap;

public class TicketScannerActivity extends AppCompatActivity {

    ActivityTicketScannerBinding binding;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventId = getIntent().getStringExtra("event_id");


        binding.donebutton.setOnClickListener(v -> {
            String ticket = binding.tickettextview.getText().toString();
            HashMap<String, String> data = new HashMap<>();
            data.put("event_id", eventId);
            data.put("ticket", ticket);
            new Server(this).verifyTicket(data, new Server.Result() {
                @Override
                public void onResult(JSONObject result) {
                    System.out.println(result);
                }

                @Override
                public void onError(String error) {

                }
            });

        });

        /*
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();*/
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/
}