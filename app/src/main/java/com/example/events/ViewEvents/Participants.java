package com.example.events.ViewEvents;

import com.google.firebase.firestore.DocumentSnapshot;

public class Participants {
    String name;
    String role;
    String UID;
    boolean verified = false;
    boolean isTitle = false;

    public Participants(String Name, String role) {
        this.name = name;
        this.role = role;
    }

    public Participants(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public Participants(DocumentSnapshot doc) {
        this.name = doc.getString("user");
        this.UID = doc.getString("user");
        this.role = doc.getString("role");

        if (doc.contains("participated")) {
            //System.out.println("CONTAINS PARTICI{ATES  "+doc.getBoolean("participated"));
            verified = doc.getBoolean("participated");
        }

    }
}
