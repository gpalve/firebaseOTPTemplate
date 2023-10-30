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

public class MainActivity extends AppCompatActivity {
    private EditText phoneNumberEditText;
    private Button sendOtpButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        sendOtpButton = findViewById(R.id.buttonSendOtp);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Set a click listener for the "Send OTP" button
        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                sendOtp(phoneNumber);
            }
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        // The verificationId will be generated and passed to the callbacks.
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    // Auto-verification completed, you can use the credential to sign in.
                    Log.d("onsuccess", "loggedin");
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // Handle verification failure, e.g., display an error message.
                    Log.d("onsuccess", "failed");
                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    // The verification code has been sent to the user's phone.
                    // You can now navigate to the OTPVerification activity, passing the verificationId.

                    Intent intent = new Intent(MainActivity.this, OTPVerification.class);
                    intent.putExtra("verificationId", verificationId);
                    startActivity(intent);
                }
            };
}
