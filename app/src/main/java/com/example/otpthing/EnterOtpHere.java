package com.example.otpthing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtpHere extends AppCompatActivity {

    //Declaring variables to give a hold on components
    EditText etInput1,etInput2,etInput3,etInput4,etInput5,etInput6;
    Button btnVerify;
    String getOtpBackend;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp_here);

        //Assigning the components ids to variables
        btnVerify = findViewById(R.id.btnVerify);
        etInput1 = findViewById(R.id.etInput1);
        etInput2 = findViewById(R.id.etInput2);
        etInput3 = findViewById(R.id.etInput3);
        etInput4 = findViewById(R.id.etInput4);
        etInput5 = findViewById(R.id.etInput5);
        etInput6 = findViewById(R.id.etInput6);
        progressBar = findViewById(R.id.progress);


        //Getting the mobile number which was passes by the intent from the entermobilenumber activity.
        TextView tvMobileNum = findViewById(R.id.tvPhoneNum);
        tvMobileNum.setText(String.format("+91-%s", getIntent().getStringExtra("mobileNumber")));

        //Getting "backendOTP"
        getOtpBackend = getIntent().getStringExtra("backendOTP");


        //Adding OnClickListener to the button, which will check the correctness of otp entered.
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Writing conditions, to verify if user has entered correct otp.
                if(!etInput1.getText().toString().trim().isEmpty() &&
                        !etInput2.getText().toString().trim().isEmpty() &&
                        !etInput3.getText().toString().trim().isEmpty() &&
                        !etInput4.getText().toString().trim().isEmpty() &&
                        !etInput5.getText().toString().trim().isEmpty() &&
                        !etInput6.getText().toString().trim().isEmpty()) {

                    String code = etInput1.getText().toString() +
                            etInput2.getText().toString() +
                            etInput3.getText().toString() +
                            etInput4.getText().toString() +
                            etInput5.getText().toString() +
                            etInput6.getText().toString();

                    if(getOtpBackend!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        btnVerify.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getOtpBackend,code);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        btnVerify.setVisibility(View.VISIBLE);

                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(EnterOtpHere.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else {
                        Toast.makeText(EnterOtpHere.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(EnterOtpHere.this, "Otp  verified", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EnterOtpHere.this, "Please enter all number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberOTPMove();

//        findViewById(R.id.clickResend).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        TextView resend = findViewById(R.id.clickResend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobileNumber"),
                        60,
                        TimeUnit.SECONDS,
                        EnterOtpHere.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtpHere.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String newbackendOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(newbackendOTP, forceResendingToken);
                                getOtpBackend = newbackendOTP;
                                Toast.makeText(EnterOtpHere.this, "OTP sent.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });


        //onCreate ends here
    }


    // Creating method so that it moves cursor when text is filled.
    private void numberOTPMove() {
        etInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Adding condition, if the text field doesn't remain empty, then move focus to input text 2.
                if(!s.toString().trim().isEmpty()){
                    etInput2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Adding condition, if the text field doesn't remain empty, then move focus to input text 2.
                if(!s.toString().trim().isEmpty()){
                    etInput3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Adding condition, if the text field doesn't remain empty, then move focus to input text 2.
                if(!s.toString().trim().isEmpty()){
                    etInput4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etInput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Adding condition, if the text field doesn't remain empty, then move focus to input text 2.
                if(!s.toString().trim().isEmpty()){
                    etInput5.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etInput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Adding condition, if the text field doesn't remain empty, then move focus to input text 2.
                if(!s.toString().trim().isEmpty()){
                    etInput6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    //Main class ends here
}