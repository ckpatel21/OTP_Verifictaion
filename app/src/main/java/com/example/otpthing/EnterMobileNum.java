package com.example.otpthing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterMobileNum extends AppCompatActivity {

    //Declaring variables to give a hold on components
    EditText enterPhoneNum;
    Button btnGetOtp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_num);

        //Assigning the components ids to variables
        enterPhoneNum = findViewById(R.id.editPhoneNum);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        progressBar = findViewById(R.id.progress);

        //Adding OnClickListener to the button, which will check the correctness of Number entered.
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Writing conditions, to verify if user has entered correct number.
                //First condition checks if at all user entered anything yet.
                if(!enterPhoneNum.getText().toString().trim().isEmpty()){
                    //Second condition checks if user entered valid 10 digit number or not.
                    if(enterPhoneNum.getText().toString().trim().length() == 10){

                        progressBar.setVisibility(View.VISIBLE);
                        btnGetOtp.setVisibility(View.INVISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + enterPhoneNum.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                EnterMobileNum.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        btnGetOtp.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        btnGetOtp.setVisibility(View.VISIBLE);
                                        Toast.makeText(EnterMobileNum.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCodeSent(@NonNull String backendOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(backendOTP, forceResendingToken);
                                        progressBar.setVisibility(View.GONE);
                                        btnGetOtp.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(EnterMobileNum.this,EnterOtpHere.class);
                                        intent.putExtra("mobileNumber", enterPhoneNum.getText().toString());
                                        intent.putExtra("backendOTP",backendOTP);
                                        startActivity(intent);
                                    }
                                }
                        );
                        //if the number is valid, take me to the next activity where user enters the otp.
//                        Intent intent = new Intent(EnterMobileNum.this,EnterOtpHere.class);
//                        intent.putExtra("mobileNumber", enterPhoneNum.getText().toString());
//                        startActivity(intent);
                    } else {
                        //if number is !valid, make toast
                        Toast.makeText(EnterMobileNum.this, "Please enter valid number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //if user hasnot entered number, make toast
                    Toast.makeText(EnterMobileNum.this, "Enter Mobile Number.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}