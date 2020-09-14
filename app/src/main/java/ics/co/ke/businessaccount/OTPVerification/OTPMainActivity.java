package ics.co.ke.businessaccount.OTPVerification;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

/*import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;*/
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Random;

import ics.co.ke.businessaccount.R;

public class OTPMainActivity extends AppCompatActivity {

    private Spinner spinner;
    TextInputEditText editTextCountryCode, editTextPhone;
    AppCompatButton buttonContinue;

    int randomnumber;

    // Initialize
    String username = "ics-kenya";    // use 'sandbox' for development in the test environment
    String apiKey = "a9bb8c53801ef0213c9e55805ba2b4d226b564c4d4042f5a9dd28332be21562f";       // use your sandbox app API key for development in the test environment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // AfricasTalking.initialize(username, apiKey);

      //  SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

        spinner = findViewById(R.id.editTextCountryCode);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countrynames));

        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String code = CountryData.countryareacodes[spinner.getSelectedItemPosition()];
        String number = editTextPhone.getText().toString().trim();

        /* Set the numbers you want to send to in international format */
        String[] recipients = new String[]{number};

        /* Set your message */
        String message = "Hi! Kindly Verify Your Account";

        /* Set your shortCode or senderId */
        String from = "MARIKITI"; // or "ABCDE"
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.isEmpty() || number.length() < 9) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }
                    /*try {
                        Random random = new Random();
                        randomnumber = random.nextInt(999999);
                        List<Recipient> response = sms.send(message, from, recipients, true);
                        for (Recipient recipient : response) {
                            System.out.print(recipient.number);
                            System.out.print(" : ");
                            System.out.println(recipient.status);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }*/


                String phoneNumber = "+" + code + number;

                Intent intent = new Intent(OTPMainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("randomnumber", randomnumber);
                startActivity(intent);
            }
        });
    }

   /* @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }*/
}
