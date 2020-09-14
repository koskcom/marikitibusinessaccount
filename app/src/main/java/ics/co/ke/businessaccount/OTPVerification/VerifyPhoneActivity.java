package ics.co.ke.businessaccount.OTPVerification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;


import java.util.concurrent.TimeUnit;

import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.ui.activities.LoginActivity;


public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;


    ProgressBar progressBar;
    TextInputEditText editText;
    AppCompatButton buttonSignIn;

    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        //   mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String randomnumber = getIntent().getStringExtra("randomnumber");
        // sendVerificationCode(phoneNumber);

        code = (int) Double.parseDouble(String.valueOf(randomnumber));
        editText.setText(String.valueOf(code));
        // save phone number
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("randomnumber", randomnumber);
        editor.apply();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (code == Integer.valueOf(editText.getText().toString())) {
                    Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentRegister);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong OTP!", Toast.LENGTH_LONG).show();
                    // Navigate to RegisterActivity
                }
               /* String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);*/
            }
        });

    }




/*
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }*/
}
