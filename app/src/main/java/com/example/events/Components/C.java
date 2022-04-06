package com.example.events.Components;

import android.content.Context;
import android.content.SharedPreferences;

public class C {
    public static final String users = "users";
    public static final String name = "name";
    public static final String email = "email";
    public static final String birth = "birth";
    public static final String phone = "phone";



    public static final String events = "events";
    public static final String event_name = "event_name";
    public static final String event_date = "event_date";
    public static final String event_time = "event_time";
    public static final String description = "description";

    public static final String organised_events = "organised_events";
    public static final String events_invitations = "events_invitations";
    public static final String joined_events = "joined_events";


    public static final String reference = "ref";
    public static final String prev_reference = "prev_reference";



    public static String getNumber(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("phone","nothing");
    }

    public static void setNumber(Context context,String number){
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("phone", number);
        editor.commit();
    }

}
