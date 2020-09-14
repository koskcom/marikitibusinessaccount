package ics.co.ke.businessaccount.ui.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import net.steamcrafted.loadtoast.LoadToast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.model.Transaction;
import ics.co.ke.businessaccount.pojo.CreditPurchase;
import ics.co.ke.businessaccount.ui.activities.MonthYearPickerDialog;
import ics.co.ke.businessaccount.ui.adapters.CredStateMentAdapter;
import ics.co.ke.businessaccount.ui.adapters.TransactionListAdapter;

public class CreditPurchaseStatementActivity extends AppCompatActivity {


    ImageView imgdepo;
    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";

    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;
    public double DepositEntered, NewBalance, WithdrawEntered;
    TextView BalanceTV, TitleTV;
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context cx;

    LayoutInflater layoutInflater;
    private DataBaseAdapter db;
    Context ctx;

    private RelativeLayout relativeLayout;
    TextView txtvwdate;
    TextView txtviewday;
    TransactionListAdapter adapter2;
    CredStateMentAdapter adapter;
    ListView itemsList;
    String monthYearStr;
    @BindView(R.id.TextViewReversetransaction)
    EditText TextViewReversetransaction;
    @BindView(R.id.ReverseButton)
    AppCompatButton ReverseButton;

    String reverseTR;


    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    /*AppCompatButton Viewbtn;
    AppCompatButton viewbtn;
*/
    private ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();
    private HashMap<Integer, String> localhash = new HashMap<>();

    private ArrayList<CreditPurchase> creditPurchaseArrayList = new ArrayList<CreditPurchase>();
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_purchase);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getAlltransactions();

        //viewbtn = (AppCompatButton) findViewById(R.id.Viewbtn);
        db = new DataBaseAdapter(getApplicationContext()).open();
        txtvwdate = findViewById(R.id.textviewbsDate);

        txtvwdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        monthYearStr = year + "-" + (month + 1) + "-" + i2;
                        txtvwdate.setText(formatMonthYear(monthYearStr));
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        });


        ReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseTR = Objects.requireNonNull(TextViewReversetransaction.getText()).toString().trim();
                if (validateTRID(TextViewReversetransaction.getText().toString())) {
                    new reverseTransactionbtn().execute();
                }
                else {
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        String currentDateandTime = sdf.format(new Date());
        txtvwdate.setText(currentDateandTime);

        txtviewday = findViewById(R.id.textviewday);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/E", Locale.US);
        Calendar calendar2 = Calendar.getInstance();
        String weekday2 = sdf2.format(calendar2.getTime());
        txtviewday.setText(weekday2);


        Bundle extras = getIntent().getExtras();

        imgdepo = findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (extras != null) {
            receivedBalance = extras.getString("balance");
            receivedKey = extras.getString("key");

        }
        BalanceTV = findViewById(R.id.cBalanceTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));

    }

    private boolean validateTRID(String reverseTR) {

        if (TextUtils.isEmpty(reverseTR)) {
            TextViewReversetransaction.setError("Enter Transaction Number Here");
            return false;
        } else if (reverseTR.length() < 9 || reverseTR.length() > 9) {
            TextViewReversetransaction.setError("Must be 9 Characters");
            return false;
        } else if (!reverseTR.contains("MTC")) {
            TextViewReversetransaction.setError("Must Start with MTC or MTD");
            return false;
        }

        return true;
    }

    String formatMonthYear(String str) {
        Date date = null;
        try {
            date = input.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }


    private class reverseTransactionbtn extends AsyncTask<Void, Void, Void> {
        final String text = "Sending Reversal Request...";
        final LoadToast lt = new LoadToast(CreditPurchaseStatementActivity.this)
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
                    //  finish();
                    lt.success();

                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(CreditPurchaseStatementActivity.this);
                    alertdialog.setMessage("Please wait for 5 minutes as we process your reversal request!!!");
                    alertdialog.setCancelable(false);
                    alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
                    alertdialog.setTitle("Reversal Sent Successfully!!!");
                    alertdialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextViewReversetransaction.setText(null);
                                    //  finish();
                                }
                            });
                    AlertDialog alertDialog = alertdialog.create();
                    alertDialog.show();
                    /*// Toast.makeText(getApplicationContext(), "Successfully Sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DepositFundsActivity.this, BusinessDashboardActivity.class);
                    startActivity(intent);*/
                }
            });
        }
    }

    private void getAlltransactions() {
        //Creating a list view items
        ListView list = findViewById(R.id.itemsList);
        ArrayList<CreditPurchase> creditPurchaseList = new ArrayList<>();
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", "200", " ", "", "MTC202809", " ", "2020/09/08"));
        creditPurchaseList.add(new CreditPurchase(1, "KOSKEI", "+254724917075", " ", "-200", "", " ", "MTD202809", "2020/09/08"));


        adapter = new CredStateMentAdapter(this, creditPurchaseList);
        list.setAdapter(adapter);


      /*  ListView list = (ListView) findViewById(R.id.itemsList);
        List<Transaction> transactionlist = db.getAllDepositsTransactionReport();
        int listSize = transactionlist.size();

        for (int i = 0; i < transactionlist.size(); i++) {
            System.out.println("Amount deposited=== " + transactionlist.get(i).getAmount());

            String amount = transactionlist.get(i).getAmount();

            if (amount != null) {
                transactionArrayList.add(new Transaction(
                        transactionlist.get(i).getId(),
                        transactionlist.get(i).getTransaction_id(),
                        transactionlist.get(i).getUsername(),
                        transactionlist.get(i).getNational_id(),
                        transactionlist.get(i).getPhone_Number(),
                        transactionlist.get(i).getUser_Pin(),
                        transactionlist.get(i).getAccount_type(),
                        transactionlist.get(i).getBank_Name(),
                        transactionlist.get(i).getBank_account_number(),
                        transactionlist.get(i).getType_of_account(),
                        transactionlist.get(i).getRecipient_number(),
                        transactionlist.get(i).getDate(),
                        transactionlist.get(i).getTrader_id(),
                        transactionlist.get(i).getTrader_username(),
                        transactionlist.get(i).getPayment_mode(),
                        transactionlist.get(i).getTrader_number(),
                        transactionlist.get(i).getAmount(),
                        transactionlist.get(i).getAgent_number(),
                        transactionlist.get(i).getCredit_amount(),
                        transactionlist.get(i).getDebit_amount(),
                        transactionlist.get(i).getCredit_amount_Transaction_number(),
                        transactionlist.get(i).getDebit_amount_Transaction_number()

                ));
            }
            adapter = new TransactionListAdapter(this, transactionArrayList);
            list.setAdapter(adapter);
        }*/
    }

    public void viewCustomer() {
        // get alert_dialog.xml view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.activity_view_customer_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getApplicationContext());

        // set alert_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText nationalid = promptsView.findViewById(R.id.etnationalid);
        final EditText userInputamonut = promptsView.findViewById(R.id.etAmaunt);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setMessage("Customer Details")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        Toast.makeText(getApplicationContext(), "Entered: " + nationalid.getText().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Entered: " + userInputamonut.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    //function to open and edit shared preferences file
    protected void onPause() {

        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();

        //save new checking or savings balance
        myEditor.putString(receivedKey, String.valueOf(BalanceD));
        myEditor.apply();

    }//end onPause


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}

