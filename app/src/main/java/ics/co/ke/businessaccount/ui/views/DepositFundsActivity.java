package ics.co.ke.businessaccount.ui.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import net.steamcrafted.loadtoast.LoadToast;
import net.steamcrafted.loadtoast.MaterialProgressDrawable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.pojo.Deposit;
import ics.co.ke.businessaccount.model.Transaction;

public class DepositFundsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    private DataBaseAdapter db;

    @BindView(R.id.ssDepositType)
    Spinner ssDepositType;
    @BindView(R.id.etAgentNumber)
    EditText etAgentNumber;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.DepositButton)
    AppCompatButton DepositButton;

    String deposittype, agentno, amount;
    AppCompatButton depositButton;
    AppCompatButton btnAgentQrsScan;
    String[] deposit_type = {"-Top Up From-", "Current Balance", "Savings Balance", "Credit Card", "M-Pesa", "Easy Pay", " Airtel Money"};
    String depositType;

    EditText agentNumber;

    ImageView imgdepo;
    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";

    //declare variables
    public String receivedBalance, receivedKey, totaldeposits; //data received from menu activity
    public double BalanceD, BalanceTotal;
    public double DepositEntered, NewBalance, TotalDeposits;
    TextView BalanceTV, BalanceTotalTV;
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseAdapter(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        ImageView progressView = findViewById(R.id.progressdrawable);
        MaterialProgressDrawable drawable = new MaterialProgressDrawable(this, progressView);

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
        BalanceTV = findViewById(R.id.BalanceTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));

        //scanner
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);

        //set visibility to gone

        surfaceView.setVisibility(View.GONE);
        etAgentNumber.setEnabled(false);
        etAgentNumber.setFocusable(false);

        Spinner sdepositType = findViewById(R.id.ssDepositType);
        sdepositType.setOnItemSelectedListener(this);
        //Setting the ArrayAdapter data on the Spinner
        //Creating the ArrayAdapter instance having  list
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deposit_type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sdepositType.setAdapter(arrayAdapter);

        //receive balance, key and title from menu activity

        //set current balance of checking or savings account
        btnAgentQrsScan = findViewById(R.id.btnAgentQrsScan);

        //declare deposit button
        Button DepositB = findViewById(R.id.DepositButton);
        //declare user deposit input amount

        final EditText agentNumber = findViewById(R.id.etAgentNumber);
        final EditText DepositET = findViewById(R.id.etAmount);

        //register deposit button with Event Listener class, and Event handler method
        DepositB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.setVisibility(View.GONE);
                //if deposit field is not empty, get deposit amount
                //  String agentNumber = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
                if (!TextUtils.isEmpty(agentNumber.getText())) {
                    agentno = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
                    if (!TextUtils.isEmpty(DepositET.getText())) {
                        DepositEntered = Double.parseDouble(String.valueOf(DepositET.getText()));
                        //create deposit object
                        Deposit dp = new Deposit();
                        dp.setBalance(BalanceD);
                        dp.setDeposit(DepositEntered);
                        //calculate new balance
                        NewBalance = dp.getNewBalance();
                        BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                        BalanceD = NewBalance;

                        //reset user deposit amount to zero
                        DepositEntered = 0;
                        sentrequest();
                    }//end if
                    //deposit filed is empty, prompt user to enter deposit amount
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DepositFundsActivity.this);
                        dialog.setTitle("Error!");
                        dialog.setMessage("Enter amount and try again!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  do nothing
                            }
                        });
                        dialog.create();
                        dialog.show();
                        // Toast.makeText(DepositFundsActivity.this, "Please enter amount and try again!", Toast.LENGTH_LONG).show();
                    }//end else
                    //clear deposit field

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DepositFundsActivity.this);
                    dialog.setTitle("Error!");
                    dialog.setMessage("Enter Agent number!");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  do nothing
                        }
                    });
                    dialog.create();
                    dialog.show();

                }

            }
        });//end DepositB OnClick

        btnAgentQrsScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                surfaceView.setVisibility(View.VISIBLE);
                initialiseDetectorsources();

                // surfaceView.setVisibility(View.GONE);

            }
        });

    }//end onCreate

    //function to open and edit shared preferences file

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        if (selection.equals("-Select Deposit Type-")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Function Required", Toast.LENGTH_SHORT);
        } else if (selection.equals("Agent Deposit")) {
            etAgentNumber.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            initialiseDetectorsources();
        } else if (selection.equals("Debit/Credit Card")) {
            surfaceView.setVisibility(View.GONE);
            etAgentNumber.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Directed to Third Party Bank API to\n" +
                    "complete transaction", Toast.LENGTH_SHORT).show();
          /*  Intent intent = new Intent(DepositAcitivity.this, ActivityFundsDasboard.class);
            startActivity(intent);*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    boolean validateSpinneracc(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-Top Up From-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void sentrequest() {
        depositType = Objects.requireNonNull(ssDepositType.getSelectedItem()).toString().trim();
        agentno = Objects.requireNonNull(etAgentNumber.getText()).toString().trim();
        amount = Objects.requireNonNull(etAmount.getText()).toString().trim();
        Transaction transaction = new Transaction();
        transaction.setAccount_type(depositType);
        transaction.setAgent_number(agentno);
        transaction.setAmount(amount);
      //  db.addDeposits(transaction);
        new Topupbtn().execute();
    }

    @Override
    public void onClick(View v) {
    }

    private class Topupbtn extends AsyncTask<Void, Void, Void> {
        final String text = "Sending Request...";
        final LoadToast lt = new LoadToast(DepositFundsActivity.this)
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

                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(DepositFundsActivity.this);
                    alertdialog.setMessage("Top Up Successfully!!!");
                    alertdialog.setCancelable(false);
                    alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
                    alertdialog.setTitle("Top Up Account");
                    alertdialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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

    //scanner
    private void initialiseDetectorsources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)//you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(DepositFundsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(DepositFundsActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    etAgentNumber.post(new Runnable() {
                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                etAgentNumber.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                etAgentNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                etAgentNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });

    }

    //function to open and edit shared preferences file
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();
        //save new checking or savings balance
        myEditor.putString(receivedKey, String.valueOf(BalanceD));
        myEditor.apply();
    }//end onPause

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsources();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}//end  TransactionActivity

