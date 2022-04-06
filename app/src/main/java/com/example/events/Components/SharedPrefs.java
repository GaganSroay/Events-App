package com.example.events.Components;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String FIREBASE = "firebase";

    public static void setIdToken(Context context, String idToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(FIREBASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("idToken", idToken);
        editor.commit();
    }

    public static String getIdToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(FIREBASE, Context.MODE_PRIVATE);
        return sharedPref.getString("idToken", null);
    }
}
