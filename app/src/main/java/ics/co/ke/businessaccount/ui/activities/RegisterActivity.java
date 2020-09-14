package ics.co.ke.businessaccount.ui.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import ics.co.ke.businessaccount.OTPVerification.OTPMainActivity;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.model.User;
import ics.co.ke.businessaccount.viewLicencedialog.EasyLicensesDialogCompat;
import ics.co.ke.businessaccount.viewLicencedialog.licensesdialog.LicensesDialog;
import ics.co.ke.businessaccount.viewLicencedialog.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import ics.co.ke.businessaccount.viewLicencedialog.licensesdialog.model.Notice;
import ics.co.ke.businessaccount.viewLicencedialog.licensesdialog.model.Notices;
import ics.co.ke.businessaccount.ui.widgets.InputValidation;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private NestedScrollView nestedScrollView;
    private EditText textInputEditTextidnumber;
    private EditText textInputEditTextPhoneNumber;
    private EditText textInputEditTextPin;
    private Button appCompatButtonRegister;
    private TextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    private DataBaseAdapter db;
    TextView appCompatTextViewtermsandconditions;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //   app = (FMAPI) getApplication();
        db = new DataBaseAdapter(getApplicationContext());

        initViews();
        initListeners();
        initObjects();
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputEditTextidnumber = findViewById(R.id.textInputEditTextID);
        textInputEditTextPhoneNumber = findViewById(R.id.textInputEditTextPhoneNumber);
        textInputEditTextPin = findViewById(R.id.textInputEditTextPin);

        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);
        appCompatTextViewtermsandconditions = findViewById(R.id.appCompatTextViewtermsandconditions);

        appCompatTextViewtermsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMultipleProgrammaticClick();
                //showAboutDialog();
            }
        });
    }

    public void onMultipleIncludeOwnClick() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }


    public void onMultipleProgrammaticClick() {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("Marikiti Money Manager Terms and Conditions Agreement", "https://marikiti.app/wp-content/uploads/2020/07/marikiti-app-terms-and-conditions-money-anager.pdf", " ", new GnuLesserGeneralPublicLicense21()));
        notices.addNotice(new Notice("Trader And Vendor Terms and Conditions Agreement", "https://marikiti.app/wp-content/uploads/2020/07/trader-and-vendor-terms-and-conditions.pdf", " ", new GnuLesserGeneralPublicLicense21()));
        notices.addNotice(new Notice("Marikiti App User Terms and Conditions Agreement", "https://marikiti.app/wp-content/uploads/2020/07/marikiti-app-user-terms-and-conditions.pdf", " ", new GnuLesserGeneralPublicLicense21()));
        notices.addNotice(new Notice("Marikiti App Terms and Conditions Agreement Food Vendor", "https://marikiti.app/wp-content/uploads/2020/07/marikiti-app-terms-and-conditions-food-vendor.pdf", " ", new GnuLesserGeneralPublicLicense21()));
        notices.addNotice(new Notice("Marikiti Trader Contract", "https://marikiti.app/wp-content/uploads/2020/07/marikiti-trader-contract.pdf", " ", new GnuLesserGeneralPublicLicense21()));
        new LicensesDialog.Builder(this)
                .setNotices(notices)
                .setIncludeOwnLicense(false )
                .build()
                .show();
    }

    public void showLicensesDialog(View view) {
        new EasyLicensesDialogCompat(this)
                .setTitle("Licenses")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
    public void showAboutDialog() {
        String msg = getResources().getString(R.string.abouttext);
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.DialogStyle).create();
        alertDialog.setTitle("Terms and conditions");
        alertDialog.setMessage(Html.fromHtml(msg));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "View on web",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://marikit.app")));
                    }
                });
        alertDialog.show();
        TextView messageView = alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        messageView.setTextColor(Color.parseColor("#757575"));
        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(this);
        db = new DataBaseAdapter(this);
        user = new User();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewLoginLink:
                Intent i = new Intent(this, LoginActivity.class);
                finish();
                break;
        }
    }

    private class registerbtn extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mprogress = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setMessage("Registering...Please wait!");
            mprogress.setCancelable(false);
            mprogress.setIndeterminate(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (mprogress.isShowing()) {
                mprogress.dismiss();
            }
            runOnUiThread(new Runnable() {
                public void run() {

                    // Toast.makeText(getApplicationContext(), "Proceed To Verify Registration!", Toast.LENGTH_LONG).show();
                    // Navigate to RegisterActivity
                    Intent intentRegister = new Intent(getApplicationContext(), OTPMainActivity.class);
                    startActivity(intentRegister);
                    finish();
                    //   Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        //  textInputLayoutname =
        String name = Objects.requireNonNull(textInputEditTextidnumber.getText()).toString().trim();
        String phoneno = Objects.requireNonNull(textInputEditTextPhoneNumber.getText()).toString().trim();
        String pin = Objects.requireNonNull(textInputEditTextPin.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            textInputEditTextidnumber.setError("Field Required");
            return;
        }

        if (TextUtils.isEmpty(phoneno)) {
            textInputEditTextPhoneNumber.setError("Field Required");
            return;
        }
        if (TextUtils.isEmpty(pin)) {
            textInputEditTextPin.setError("Field Required");
            return;
        } else if (!db.checkUser(textInputEditTextPhoneNumber.getText().toString().trim())) {
            user.setIdno(textInputEditTextidnumber.getText().toString().trim());
            user.setPhoneNumber(textInputEditTextPhoneNumber.getText().toString().trim());
            user.setPassword(textInputEditTextPin.getText().toString().trim());

            db.addUser(user);
            new registerbtn().execute();
            // Snack Bar to show success message that record saved successfully
            emptyInputEditText();
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_ID_exists), Snackbar.LENGTH_LONG).show();
        }
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextidnumber.setText(null);
        textInputEditTextPhoneNumber.setText(null);
        textInputEditTextPin.setText(null);

    }
}
