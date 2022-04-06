package com.example.events.ViewEvents;

import com.google.firebase.firestore.DocumentSnapshot;

public class Message {
    static String myUid = null;
    public String uid;
    public String message;
    public String messageId;
    public String timestamp;
    public boolean me = false;

    public Message(String message, String uid) {
        this.message = message;
        this.uid = uid;
    }

    public Message(String message, String uid, boolean me) {
        this.message = message;
        this.uid = uid;
        this.me = me;
    }

    public Message(DocumentSnapshot doc) {
        message = doc.getString("message");
        uid = doc.getString("user");
        messageId = doc.getString("messageId");
        timestamp = doc.getString("timestamp");
    }

    public Message(DocumentSnapshot doc, boolean me) {
        message = doc.getString("message");
        uid = doc.getString("user");
        messageId = doc.getString("messageId");
        timestamp = doc.get("timestamp").toString();
        this.me = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
