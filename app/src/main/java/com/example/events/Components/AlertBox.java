package com.example.events.Components;

import android.app.AlertDialog;
import android.content.Context;

public class AlertBox {
    public static void showAlert(Context context){
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void alertWindow(Context context){

    }
}
