package ics.co.ke.businessaccount.ui.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import ics.co.ke.businessaccount.pojo.WManager;
import ics.co.ke.businessaccount.ui.activities.MonthYearPickerDialog;
import ics.co.ke.businessaccount.ui.adapters.WithdrawalManagerAdapter;

public class WithdrawalManagerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context cx;

    LayoutInflater layoutInflater;
    private DataBaseAdapter db;
    Context ctx;

    private RelativeLayout relativeLayout;

    WithdrawalManagerAdapter adapter;
    ListView itemsList;
    TextView textviewdate;
    TextView txtvwdate;
    ImageView imgdepo;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String monthYearStr;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");

    private ArrayList<WManager> wManagerArrayList = new ArrayList<WManager>();
    private HashMap<Integer, String> localhash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawal_manager_activity);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //   new fetchdata().execute();

        db = new DataBaseAdapter(getApplicationContext());
        getAlltransactions();
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
        /*txtvwdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        WithdrawalManagerActivity.this,

                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = month+"/"+day+"/"+year;
                txtvwdate.setText(date);
            }

        };*/
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

    private void getAlltransactions() {
        ListView list = findViewById(R.id.itemsList);

        ArrayList<WManager> wManagerlist = new ArrayList<>();
        wManagerlist.add(new WManager(1, "34516458"));
        wManagerlist.add(new WManager(2, "34516458"));
        wManagerlist.add(new WManager(3, "34516458"));
        wManagerlist.add(new WManager(4, "34516458"));
        wManagerlist.add(new WManager(5, "34516458"));
        wManagerlist.add(new WManager(6, "34516458"));
        wManagerlist.add(new WManager(7, "34516458"));
        wManagerlist.add(new WManager(8, "34516458"));
        wManagerlist.add(new WManager(9, "34516458"));
        wManagerlist.add(new WManager(10, "34516458"));
        wManagerlist.add(new WManager(11, "34516458"));
        wManagerlist.add(new WManager(12, "34516458"));
        wManagerlist.add(new WManager(13, "34516458"));
        wManagerlist.add(new WManager(14, "34516458"));
        wManagerlist.add(new WManager(15, "34516458"));
        wManagerlist.add(new WManager(16, "34516458"));
        wManagerlist.add(new WManager(17, "34516458"));
        wManagerlist.add(new WManager(18, "34516458"));
        wManagerlist.add(new WManager(19, "34516458"));
        wManagerlist.add(new WManager(20, "34516458"));
        wManagerlist.add(new WManager(8, "34516458"));
        wManagerlist.add(new WManager(9, "34516458"));
        wManagerlist.add(new WManager(10, "34516458"));
        wManagerlist.add(new WManager(11, "34516458"));
        wManagerlist.add(new WManager(12, "34516458"));
        wManagerlist.add(new WManager(13, "34516458"));
        wManagerlist.add(new WManager(14, "34516458"));
        wManagerlist.add(new WManager(15, "34516458"));
        wManagerlist.add(new WManager(16, "34516458"));
        wManagerlist.add(new WManager(17, "34516458"));
        wManagerlist.add(new WManager(18, "34516458"));
        wManagerlist.add(new WManager(19, "34516458"));
        wManagerlist.add(new WManager(20, "34516458"));
        wManagerlist.add(new WManager(8, "34516458"));
        wManagerlist.add(new WManager(9, "34516458"));
        wManagerlist.add(new WManager(10, "34516458"));
        wManagerlist.add(new WManager(11, "34516458"));
        wManagerlist.add(new WManager(12, "34516458"));
        wManagerlist.add(new WManager(13, "34516458"));
        wManagerlist.add(new WManager(14, "34516458"));
        wManagerlist.add(new WManager(15, "34516458"));
        wManagerlist.add(new WManager(16, "34516458"));
        wManagerlist.add(new WManager(17, "34516458"));
        wManagerlist.add(new WManager(18, "34516458"));
        wManagerlist.add(new WManager(19, "34516458"));
        wManagerlist.add(new WManager(20, "34516458"));
        wManagerlist.add(new WManager(8, "34516458"));
        wManagerlist.add(new WManager(9, "34516458"));
        wManagerlist.add(new WManager(10, "34516458"));
        wManagerlist.add(new WManager(11, "34516458"));
        wManagerlist.add(new WManager(12, "34516458"));
        wManagerlist.add(new WManager(13, "34516458"));
        wManagerlist.add(new WManager(14, "34516458"));
        wManagerlist.add(new WManager(15, "34516458"));
        wManagerlist.add(new WManager(16, "34516458"));
        wManagerlist.add(new WManager(17, "34516458"));
        wManagerlist.add(new WManager(18, "34516458"));
        wManagerlist.add(new WManager(19, "34516458"));
        wManagerlist.add(new WManager(20, "34516458"));

        adapter = new WithdrawalManagerAdapter(this, wManagerlist);
        list.setAdapter(adapter);


        /*List<DManager>depositslist = db.getAlltransactions();
        int listSize = depositslist.size();

        for (int i = 0; i < depositslist.size(); i++) {
            System.out.println("Document Number=== " + depositslist.get(i).getAgentnumber());

            String amount = depositslist.get(i).getAmount();

            if (amount != null) {
                DManagerArrayList.add(new DepositsManager(
                        depositslist.get(i).getId(),
                        depositslist.get(i).getDeptype(),
                        depositslist.get(i).getAgentnumber(),
                        depositslist.get(i).getAmount()

                ));
            }
            localhash.put(i, depositslist.get(i).getNid());
            adapter = new DepositsAdapter(this, depositsManagerArrayList);
            list.setAdapter(adapter);
        }


        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
               // viewCustomer();
            }
        });
        */
        /*viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new withdrawbtn().execute();
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