package com.focusx.brokerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {
    private EditText otpEditText;
    private Button verifyOtpButton;

    private FirebaseAuth firebaseAuth;

    private String verificationId; // Store the verification ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_otpverification);

        otpEditText = findViewById(R.id.editTextOTP);
        verifyOtpButton = findViewById(R.id.buttonVerifyOTP);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Retrieve the verification ID passed from the previous activity
        verificationId = getIntent().getStringExtra("verificationId");

        // Set a click listener for the "Verify OTP" button
        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString();
                verifyPhoneNumberWithCode(otp);
            }
        });
    }

    private void verifyPhoneNumberWithCode(String code) {
        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // Sign in with the credential
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign-in successful, redirect to the home screen.
                                // You can add the code for this redirection here.
                                Intent intent = new Intent(OTPVerification.this, HomePage.class);
                                startActivity(intent);
                            } else {
                                // Sign-in failed, handle the error.
                                Log.d("errorver", "errorver");
                            }
                        }
                    });
        } else {
            Log.d("verificationId", "Verification ID is null");
        }
    }
}
