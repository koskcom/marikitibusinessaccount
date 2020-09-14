package ics.co.ke.businessaccount.ui.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.model.Transaction;
import ics.co.ke.businessaccount.pojo.DManager;
import ics.co.ke.businessaccount.ui.activities.MonthYearPickerDialog;
import ics.co.ke.businessaccount.ui.adapters.DepositsAdapter;

public class DepositManagerActivity extends AppCompatActivity  {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context cx;
    LayoutInflater layoutInflater;
    private DataBaseAdapter db;
    Context ctx;
    private RelativeLayout relativeLayout;
    DepositsAdapter adapter;
    ListView itemsList;
    private ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();
    private HashMap<Integer, String> localhash = new HashMap<>();
    TextView txtvwdate;
    TextView textviewdate;
    AppCompatButton viewbtn;
    ImageView imgdepo;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String monthYearStr;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_manager_activity);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //viewbtn = (AppCompatButton) findViewById(R.id.Viewbtn);
        db = new DataBaseAdapter(getApplicationContext());
        new fetchdata().execute();


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


     /*   viewbtn=(AppCompatButton) findViewById(R.id.btnview);
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showCustomDialog();
                showTransparentDialog();
            }
        });*/
        imgdepo = findViewById(R.id.imgdepo);


        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        String currentDateandTime = sdf.format(new Date());
        txtvwdate.setText(currentDateandTime);


        textviewdate = findViewById(R.id.textviewdate);
        SimpleDateFormat sd = new SimpleDateFormat("dd/MMM/yyyy");
        String currentDateandTim = sd.format(new Date());
        textviewdate.setText(currentDateandTim);


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
    private class fetchdata extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mprogress = new ProgressDialog(DepositManagerActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.show();
            mprogress.setCancelable(false);
            mprogress.setCanceledOnTouchOutside(false);
            mprogress.setCancelable(false);
            mprogress.setIndeterminate(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getAlltransactions();
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
                    //   Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAlltransactions() {
        ListView list = findViewById(R.id.itemsList);

        ArrayList<DManager> dmanagerList = new ArrayList<>();
        dmanagerList.add(new DManager(1, "34516458"));
        dmanagerList.add(new DManager(2, "34516458"));
        dmanagerList.add(new DManager(3, "34516458"));
        dmanagerList.add(new DManager(4, "34516458"));
        dmanagerList.add(new DManager(5, "34516458"));
        dmanagerList.add(new DManager(6, "34516458"));
        dmanagerList.add(new DManager(7, "34516458"));
        dmanagerList.add(new DManager(8, "34516458"));
        dmanagerList.add(new DManager(9, "34516458"));
        dmanagerList.add(new DManager(10, "34516458"));
        dmanagerList.add(new DManager(11, "34516458"));
        dmanagerList.add(new DManager(12, "34516458"));
        dmanagerList.add(new DManager(13, "34516458"));
        dmanagerList.add(new DManager(14, "34516458"));
        dmanagerList.add(new DManager(15, "34516458"));
        dmanagerList.add(new DManager(16, "34516458"));
        dmanagerList.add(new DManager(17, "34516458"));
        dmanagerList.add(new DManager(18, "34516458"));
        dmanagerList.add(new DManager(19, "34516458"));
        dmanagerList.add(new DManager(20, "34516458"));

        adapter = new DepositsAdapter(this, dmanagerList);
        list.setAdapter(adapter);


       /* List<Transaction> transactionlist = db.getAllTransaction();
        int listSize = transactionlist.size();

        for (int i = 0; i < transactionlist.size(); i++) {
            System.out.println("Document Number=== " + transactionlist.get(i).getAmount());

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
                        transactionlist.get(i).getAgent_number(),
                        transactionlist.get(i).getAmount()

                ));
            }
            localhash.put(i, transactionlist.get(i).getId());
            adapter = new DepositsAdapter(this, transactionArrayList);
            list.setAdapter(adapter);
        }*/


        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
            }
        });
       /* viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCustomer();
            }
        });*/

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}