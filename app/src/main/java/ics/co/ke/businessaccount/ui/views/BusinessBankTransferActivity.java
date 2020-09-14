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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.steamcrafted.loadtoast.LoadToast;
import net.steamcrafted.loadtoast.MaterialProgressDrawable;

import java.text.DecimalFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.pojo.Transfer;
import ics.co.ke.businessaccount.model.Transaction;

public class BusinessBankTransferActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //declare constants used with shared preferences
    public static final String MY_BALANCE = "My_Balance";
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double TransferEntered;
    int transferChoice; //spinner index

    ImageView imgdepo;
    private DataBaseAdapter db;
    EditText Transferpin;
    EditText banknumber;
    EditText bankaccount_number;
    EditText amount_entered;

    SharedPreferences.Editor myEditor;

    String bankname, bankaccountnumber, amount, accounttype, pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_transfer_activity);

        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        db = new DataBaseAdapter(getApplicationContext());

        ImageView progressView = findViewById(R.id.progressdrawable);
        MaterialProgressDrawable drawable = new MaterialProgressDrawable(this, progressView);

        //receive checking and savings balance from menu activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            receivedBalanceC = extras.getString("balanceC");
            receivedBalanceS = extras.getString("balanceS");
        }
        imgdepo = findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //set current balance of checking account
        cBalanceTV = findViewById(R.id.cBalanceTextView);
        cBalanceD = Double.parseDouble(String.valueOf(receivedBalanceC));
        cBalanceTV.setText(String.valueOf(currency.format(cBalanceD)));

        //set current balance of savings account
        sBalanceTV = findViewById(R.id.sBalanceTextView);
        sBalanceD = Double.parseDouble(String.valueOf(receivedBalanceS));
        sBalanceTV.setText(String.valueOf(currency.format(sBalanceD)));


        //declare transfer input amount, Spinner, and Button
        banknumber = findViewById(R.id.etbanknumber);
        bankaccount_number = findViewById(R.id.etAccountnumber);
        amount_entered = findViewById(R.id.etAmount);
        Transferpin = findViewById(R.id.etmarikitipin);
        final Spinner TransferS = findViewById(R.id.TransferSpinner);
        Button transferB = findViewById(R.id.sendButton);

        //register transfer button with Event Listener class, and Event handler method
        transferB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankname = Objects.requireNonNull(banknumber.getText()).toString().trim();
                bankaccountnumber = Objects.requireNonNull(bankaccount_number.getText()).toString().trim();
                amount = Objects.requireNonNull(amount_entered.getText()).toString().trim();
                pin = Objects.requireNonNull(Transferpin.getText()).toString().trim();

                if (TextUtils.isEmpty(bankname)) {
                    banknumber.setError("Enter Bank Number");
                    return ;
                }   if (TextUtils.isEmpty(bankaccountnumber)) {
                    bankaccount_number.setError("Enter Account Number");
                    return ;
                }  if (TextUtils.isEmpty(pin)) {
                    Transferpin.setError("Pin Required");
                    return ;
                }
                //check if transfer amount was entered
                if (!TextUtils.isEmpty(amount_entered.getText()))
                {
                    TransferEntered = Double.parseDouble(String.valueOf(amount_entered.getText()));
                    //get index of spinner string array
                    transferChoice = TransferS.getSelectedItemPosition();
                    //choose between two available transfer options
                    switch (transferChoice)
                    {
                        //transfer funds from checking to savings
                        case 1:
                            //check if transfer amount is valid
                            if (cBalanceD >= TransferEntered) {
                                //withdraw from checking
                                Transfer td = new Transfer();
                                td.setBalance(cBalanceD);
                                td.setTransfer(TransferEntered);
                                //set new checking balance
                                cNewBalance = td.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;

                                //deposit to savings
                               /* Deposit dp = new Deposit();
                                dp.setBalance(sBalanceD);
                                dp.setDeposit(TransferEntered);

                                //set new savings balance
                                sNewBalance = dp.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;
                                */
                                //reset transfer amount
                                TransferEntered = 0;
                                if (validatepin(Transferpin.getText().toString())) {
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
                            if (sBalanceD >= TransferEntered) {
                                //withdraw from savings
                                Transfer td = new Transfer();
                                td.setBalance(sBalanceD);
                                td.setTransfer(TransferEntered);
                                //set new savings balance
                                sNewBalance = td.getNewBalance();

                                sBalanceTV.setText(String.valueOf(currency.format(sNewBalance)));
                                sBalanceD = sNewBalance;

                                //deposit to checking
                                /*Deposit dp = new Deposit();
                                dp.setBalance(cBalanceD);
                                dp.setDeposit(TransferEntered);

                                //set new checking balance
                                cNewBalance = dp.getNewBalance();

                                cBalanceTV.setText(String.valueOf(currency.format(cNewBalance)));
                                cBalanceD = cNewBalance;*/

                                //reset transfer amount
                                TransferEntered = 0;
                                if (validatepin(Transferpin.getText().toString())) {
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

    //validate pin
    private boolean validatepin(String pin) {

        if (TextUtils.isEmpty(pin)) {
            Transferpin.setError("Enter Pin Here");
            return false;
        } else if (pin.length() < 4) {
            Transferpin.setError("Must be 4 Characters");
            return false;
        }

        return true;
    }
    //function to open and edit shared preferences file
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

    //function to display insufficient funds message
    public void noFundsMsg() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(BusinessBankTransferActivity.this);
        alertdialog.setMessage("You Have InSufficient Balance");
        alertdialog.setCancelable(false);
        alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
        alertdialog.setTitle("Sorry !!!");
        alertdialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // finish();
                    }
                });
        AlertDialog alertDialog = alertdialog.create();
        alertDialog.show();

        //Toast.makeText(SendFundsBusinessActivity.this, "Insufficient funds! Please enter a valid transfer amount and try again!", Toast.LENGTH_LONG).show();
    }//end noFundsMsg

    //function to prompt user for input
    public void noAmountMsg() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(BusinessBankTransferActivity.this);
        alertdialog.setMessage("Amount Required");
        alertdialog.setCancelable(false);
        alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
        alertdialog.setTitle("Sorry!!!");
        alertdialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // finish();
                    }
                });
        AlertDialog alertDialog = alertdialog.create();
        alertDialog.show();


        // Toast.makeText(SendFundsBusinessActivity.this, "Nothing entered! Please enter transfer amount and try again!", Toast.LENGTH_LONG).show();
    }

    public void pinMsg() {
        final EditText Transferpin = findViewById(R.id.etmarikitipin);
    }//noAmountMsg

    public void sentrequest() {
        bankname = Objects.requireNonNull(banknumber.getText()).toString().trim();
        bankaccountnumber = Objects.requireNonNull(bankaccount_number.getText()).toString().trim();
        amount = Objects.requireNonNull(amount_entered.getText()).toString().trim();
        pin = Objects.requireNonNull(Transferpin.getText()).toString().trim();

        Transaction transaction = new Transaction();

        transaction.setBank_Name(bankname);
        transaction.setBank_account_number(bankaccountnumber);
        transaction.setAmount(amount);
        transaction.setUser_Pin(pin);

        // db.addDeposits(transaction);
        new sentbtn().execute();

    }

    //end TransferActivity
    private class sentbtn extends AsyncTask<Void, Void, Void> {
        final String text = "Sending Request...";
        final LoadToast lt = new LoadToast(BusinessBankTransferActivity.this)
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
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(BusinessBankTransferActivity.this);
                    alertdialog.setMessage("Transfer Successfully!!!");
                    alertdialog.setCancelable(false);
                    alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
                    alertdialog.setTitle("Bank Transfer");
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
    //function to open and edit shared preferences file

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}//end TransferActivity
