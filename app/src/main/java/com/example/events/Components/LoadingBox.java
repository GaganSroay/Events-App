package com.example.events.Components;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingBox {
    public static ProgressDialog createSimpleLoading(Context context){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("loading...");
        progressdialog.setIndeterminate(true);
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    public static ProgressDialog createSimpleLoading(Context context, String message){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("loading...");
        progressdialog.setIndeterminate(true);
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    public static ProgressDialog createSimpleLoading(Context context, String title, String message){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("loading...");
        progressdialog.setIndeterminate(true);
        progressdialog.setCancelable(false);
        return progressdialog;
    }
}
