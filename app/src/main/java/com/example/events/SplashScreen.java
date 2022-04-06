package com.example.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.events.Components.Server;
import com.example.events.Components.SharedPrefs;
import com.example.events.authentication.Login;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    String TOKEN_EXPIRE_CODE = "auth/id-token-expired";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
        new Server(this).verifyIdTokken(new Server.Result() {
            @Override
            public void onResult(JSONObject result) {
                try {
                    if (result.has("error")) {
                        JSONObject errorJson = result.getJSONObject("error");
                        if (errorJson.getString("code").equals(TOKEN_EXPIRE_CODE)) {
                            auth.getCurrentUser()
                                    .getIdToken(false)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            String idToken = task.getResult().getToken();
                                            SharedPrefs.setIdToken(getApplicationContext(), idToken);
                                            homeActivity();
                                        } else {
                                            System.out.println(task.getException().getMessage());
                                        }
                                    });
                        }
                    } else if (result.has("user_id")) homeActivity();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
            }
        });

        /*else auth.getCurrentUser()
                    .getIdToken(false)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            SharedPrefs.setIdToken(getApplicationContext(), idToken);

                        }
                        else{
                            System.out.println("##############    "+task.getException().getMessage());
                        }
                    }).addOnFailureListener(e ->
                        System.out.println("##############    "+e.getMessage()
                    ));*/

    }

    private void homeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}