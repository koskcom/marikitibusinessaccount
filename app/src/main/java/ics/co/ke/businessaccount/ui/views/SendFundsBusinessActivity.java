package ics.co.ke.businessaccount.ui.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import net.steamcrafted.loadtoast.LoadToast;
import net.steamcrafted.loadtoast.MaterialProgressDrawable;

import java.text.DecimalFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.model.Transaction;
import ics.co.ke.businessaccount.pojo.Deposit;
import ics.co.ke.businessaccount.pojo.Sends;

public class SendFundsBusinessActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //declare constants used with shared preferences
    public static final String MY_BALANCE = "My_Balance";
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double SendEntered;
    int transferChoice; //spinner inde
    SharedPreferences.Editor myEditor;
    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.TransferSpinner)
    Spinner TransferSpinner;
    @BindView(R.id.ssRecipientNumber)
    Spinner ssRecipientNumber;
    @BindView(R.id.etmarikitipin)
    EditText etmarikitipin;
    @BindView(R.id.etRecipientNumber)
    EditText etRecipientNumber;
    @BindView(R.id.etamount)
    EditText etamount;
    @BindView(R.id.sendButton)
    AppCompatButton sendButton;
    DataBaseAdapter db;
    String agentno, accnttosentto, accountosentfrom;
    AppCompatButton btnAgentQrsScan;

    String[] deposit_type = {"-Send To-", "Current Account", "Savings Account", "Marikiti App Account", "Other Network"};
    ImageView imgdepo;
    String sentviaType, amount, pin, recipientNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_funds);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //receive checking and savings balance from menu activity
        Bundle extras = getIntent().getExtras();
        ImageView progressView = findViewById(R.id.progressdrawable);
        MaterialProgressDrawable drawable = new MaterialProgressDrawable(this, progressView);


        if (extras != null) {
            receivedBalanceC = extras.getString("balanceC");
            receivedBalanceS = extras.getString("balanceS");
        }

        db = new DataBaseAdapter(getApplicationContext());
        //set current balance of checking account
        cBalanceTV = findViewById(R.id.cBalanceTextView);
        cBalanceD = Double.parseDouble(String.valueOf(receivedBalanceC));
        cBalanceTV.setText(String.valueOf(currency.format(cBalanceD)));

        //set current balance of savings account
        sBalanceTV = findViewById(R.id.sBalanceTextView);
        sBalanceD = Double.parseDouble(String.valueOf(receivedBalanceS));
        sBalanceTV.setText(String.valueOf(currency.format(sBalanceD)));

        imgdepo = findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //declare transfer input amount, Spinner, and Button
        final EditText recipentno = findViewById(R.id.etRecipientNumber);
        final EditText amountET = findViewById(R.id.etamount);
        final EditText pin = findViewById(R.id.etmarikitipin);
        final Spinner TransferS = findViewById(R.id.TransferSpinner);
        Button sendB = findViewById(R.id.sendButton);

        Spinner sentviaType = findViewById(R.id.ssRecipientNumber);

        //Creating the ArrayAdapter instance having  list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deposit_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        sentviaType.setAdapter(arrayAdapter);

        //register deposit button with Event Listener class, and Event handler method
        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentviaType = Objects.requireNonNull(ssRecipientNumber.getSelectedItem()).toString().trim();
                String recipientNumber = Objects.requireNonNull(etRecipientNumber.getText()).toString().trim();
                String pin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();

                //  String valid = validateSpinneracc(TransferSpinner, transferChoice)
                if (TextUtils.isEmpty(recipientNumber)) {
                    etRecipientNumber.setError("Recipient Required");
                    return;
                }


                //check if transfer amount was entered
                else if (!TextUtils.isEmpty(amountET.getText())) {

                    SendEntered = Double.parseDouble(String.valueOf(amountET.getText()));
                    //get index of spinner string array
                    transferChoice = TransferS.getSelectedItemPosition();
                    //choose between two available transfer options
                    switch (transferChoice) {
                        //transfer funds from checking to savings
                        case 1:

                            //check if transfer amount is valid
                            if (cBalanceD >= SendEntered) {

                                //withdraw from checking
                                Sends sd = new Sends();
                                sd.setBalance(cBalanceD);
                                sd.setSends(SendEntered);
                                //set new checking balance
                                cNewBalance = sd.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //deposit to savings
                                Deposit dp = new Deposit();
                                dp.setBalance(sBalanceD);
                                dp.setDeposit(SendEntered);

                                //set new savings balance
                                sNewBalance = dp.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //reset transfer amount
                                SendEntered = 0;
                                if (validatepin(etmarikitipin.getText().toString())) {
                                    sentrequest();
                                }

                            }//end checking if transfer is valid
                            //transfer amount is not valid
                            else {
                                //send msg insufficient funds
                                noFundsMsg();
                            }//end transfer is not valid msg
                            return;

                        //transfer funds from savings  to checking
                        case 2:

                            //check if transfer amount is valid
                            if (sBalanceD >= SendEntered) {

                                //withdraw from savings
                                Sends sd = new Sends();
                                sd.setBalance(sBalanceD);
                                sd.setSends(SendEntered);
                                //set new savings balance
                                sNewBalance = sd.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //deposit to savings
                                Deposit dp = new Deposit();
                                dp.setBalance(sBalanceD);
                                dp.setDeposit(SendEntered);

                                //set new savings balance
                                sNewBalance = dp.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //reset transfer amount
                                SendEntered = 0;

                                if (validatepin(etmarikitipin.getText().toString())) {
                                    sentrequest();
                                }
                            }////end checking if transfer is valid
                            //transfer amount is not valid
                            else {
                                //send msg insufficient funds
                                noFundsMsg();
                            }//end transfer is not valid msg

                            return;
                    }//end switch transferChoice
                }//end check if transfer amount was entered
                //user didn't enter transfer amount
                else {

                    //send msg no amount entered
                    noAmountMsg();
                }//end transfer amount was not entered msg
            }//end if
        });//end onClickListener TransferButton
    }//end onCreate

    public void noFundsMsg() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(SendFundsBusinessActivity.this);
        alertdialog.setMessage("You Have InSufficient Balance");
        alertdialog.setCancelable(false);
        alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
        alertdialog.setTitle("Sorry !!!");
        alertdialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  finish();
                    }
                });
        AlertDialog alertDialog = alertdialog.create();
        alertDialog.show();

        //Toast.makeText(SendFundsBusinessActivity.this, "Insufficient funds! Please enter a valid transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//end noFundsMsg

    //function to prompt user for input
    public void noAmountMsg() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(SendFundsBusinessActivity.this);
        alertdialog.setMessage("Amount Required");
        alertdialog.setCancelable(false);
        alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
        alertdialog.setTitle("Sorry!!!");
        alertdialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  finish();
                    }
                });
        AlertDialog alertDialog = alertdialog.create();
        alertDialog.show();


        // Toast.makeText(SendFundsBusinessActivity.this, "Nothing entered! Please enter transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//noAmountMsg

    //validate pin
    private boolean validatepin(String pin) {

        if (TextUtils.isEmpty(pin)) {
            etmarikitipin.setError("Enter Pin Here");
            return false;
        } else if (pin.length() < 4) {
            etmarikitipin.setError("Must be 4 Characters");
            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

    }

    boolean validateSpinneracc(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-Transfer From-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Select Account To Transfer From", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //function to open and edit shared preferences file
    protected void onPause() {
        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();
        //save new checking and savings balance
        myEditor.putString(CHECKING_KEY, String.valueOf(cBalanceD));
        myEditor.putString(SAVINGS_KEY, String.valueOf(sBalanceD));
        myEditor.apply();
    }//end onPause

    public void sentrequest() {
        sentviaType = Objects.requireNonNull(ssRecipientNumber.getSelectedItem()).toString().trim();
        agentno = Objects.requireNonNull(etRecipientNumber.getText()).toString().trim();
        amount = Objects.requireNonNull(etamount.getText()).toString().trim();
        pin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();

        Transaction transaction = new Transaction();

        transaction.setAccount_type(sentviaType);
        transaction.setAgent_number(agentno);
        transaction.setAmount(amount);
        transaction.setUser_Pin(pin);

        //    db.addDeposits(transaction);

        new sentbtn().execute();
       /* boolean valid = validateSpinneracc(ssRecipientNumber, sentviaType);
        if (valid) {
            if (!db.checkpin(etmarikitipin.getText().toString().trim())) {
                db.insertpersonalsents(bSend);
                new sentbtn().execute();
            } else {
                Toast.makeText(SendFundsPersonalActivity.this, "Wrong User Pin Entered!", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private class sentbtn extends AsyncTask<Void, Void, Void> {

        final String text = "Sending Request...";
        final LoadToast lt = new LoadToast(SendFundsBusinessActivity.this)
                .setProgressColor(Color.GREEN)
                .setText(text)
                .setTranslationY(300)
                .setBorderColor(Color.LTGRAY)
                .show();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lt.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Thread.sleep(6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            runOnUiThread(new Runnable() {
                public void run() {
                    lt.success();
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(SendFundsBusinessActivity.this);
                    alertdialog.setMessage("Send Successfully!!!");
                    alertdialog.setCancelable(false);
                    alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
                    alertdialog.setTitle("Send Funds");
                    alertdialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertdialog.create();
                    alertDialog.show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}//end  TransactionActivity


