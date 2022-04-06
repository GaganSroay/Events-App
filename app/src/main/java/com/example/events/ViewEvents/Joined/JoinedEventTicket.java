package com.example.events.ViewEvents.Joined;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.events.Components.C;
import com.example.events.Components.FirebaseVolleyRequest;
import com.example.events.databinding.FragmentJoinedEventTicketBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JoinedEventTicket extends Fragment {

    DocumentReference ref;
    FirebaseFirestore db;
    FragmentJoinedEventTicketBinding binding;
    String ticket = null;

    public static JoinedEventTicket newInstance(String param1) {
        JoinedEventTicket fragment = new JoinedEventTicket();
        Bundle args = new Bundle();
        args.putString(C.reference, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null)
            ref = db.document(getArguments().getString(C.reference));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJoinedEventTicketBinding.inflate(inflater);

        showTicket(ticket);

        HashMap<String, String> data = new HashMap<>();
        data.put("path", ref.getPath());


        new FirebaseVolleyRequest(getActivity(), "events/get_ticket")
                .makePostRequest(data, new FirebaseVolleyRequest.GetResult() {
                    @Override
                    public void onResult(String result) {
                        try {
                            JSONObject res = new JSONObject(result);
                            ticket = res.getString("ticket");
                            showTicket(ticket);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        return binding.getRoot();
    }

    private void showTicket(String ticket) {
        if (ticket == null) return;
        if (ticket.length() == 0) return;

        int size = (int) dipToPixels(1000f);

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(
                    ticket,
                    BarcodeFormat.QR_CODE,
                    size,
                    size
            );
        } catch (WriterException e) {
            e.printStackTrace();
        }

        binding.qrcodeView.setImageBitmap(bitmap);
    }

    public float dipToPixels(float dipValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                getResources().getDisplayMetrics());
    }

}