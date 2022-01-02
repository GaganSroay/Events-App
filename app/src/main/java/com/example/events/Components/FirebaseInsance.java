package com.example.events.Components;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseInsance {
    public FirebaseAuth auth;
    public FirebaseFirestore db;
    public String UID;
    public FirebaseInsance(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        auth.getUid();
    }
}
