package com.example.events.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.events.Components.C;
import com.example.events.HomeActivity;
import com.example.events.R;
import com.example.events.databinding.ActivityLoginBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;

    private static final String TAG = "Login_Activity";

    private Dialog dialog;
    private String verificationId;

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();
        binding.donebutton.setOnClickListener(v -> {
            if(binding.numberview.getText().length() == 10){
                number = "+91"+binding.numberview.getText();
                System.out.println("Got Number  "+number);
                verificationProcess(number);

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                System.out.println("onVerificationFailed");
                System.out.println(e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    System.out.println("InvalidCredentials");
                    System.out.println(e);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    System.out.println("TooManyRequests");
                    System.out.println(e);
                }

            }

            @Override
            public void onCodeSent(@NonNull final String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Login.this.verificationId = verificationId;
                dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.otp_verification_layout);
                dialog.findViewById(R.id.verifyButton).setOnClickListener(v -> {
                    String code = ((EditText) dialog.findViewById(R.id.otpfield)).getText().toString();
                    if(code.length() == 6)
                        codeEntered(code);
                });
                dialog.show();

                Log.d(TAG, "onCodeSent:" + verificationId);
            }

        };
    }
    private void verificationProcess(String number){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseFirestore.getInstance().collection("users")
                                .document(task.getResult().getUser().getUid()).get().addOnCompleteListener(shot -> {
                                    dialog.dismiss();
                                    C.setNumber(getApplicationContext(),number);
                                    if(shot.getResult().exists())
                                        startActivity(new Intent(Login.this, HomeActivity.class));
                                    else startActivity(new Intent(Login.this, SignupDetails.class).putExtra("phone",number));
                                    finish();
                                });

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        }
                    }
                });
    }

    private void codeEntered(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }



}