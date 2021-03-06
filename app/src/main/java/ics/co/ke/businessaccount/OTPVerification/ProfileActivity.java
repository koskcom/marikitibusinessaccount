package ics.co.ke.businessaccount.OTPVerification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;




import ics.co.ke.businessaccount.R;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ProfileActivity extends AppCompatActivity {

    String phoneNumber;
    TextView mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get saved phone number
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        phoneNumber = prefs.getString("phoneNumber", NULL);

        mobileNumber = findViewById(R.id.mobileNumber);
        mobileNumber.setText(phoneNumber);

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ProfileActivity.this, OTPMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
