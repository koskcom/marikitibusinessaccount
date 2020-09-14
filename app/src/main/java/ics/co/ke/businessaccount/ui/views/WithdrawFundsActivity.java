package ics.co.ke.businessaccount.ui.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.steamcrafted.loadtoast.LoadToast;
import net.steamcrafted.loadtoast.MaterialProgressDrawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.database.DataBaseAdapter;
import ics.co.ke.businessaccount.model.Transaction;

public class WithdrawFundsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";

    //declare variables
    public String receivedBalance, receivedKey, receivedTitle; //data received from menu activity
    public double BalanceD;
    public double DepositEntered, NewBalance, WithdrawEntered;
    TextView BalanceTV, TitleTV;
    public DecimalFormat currency = new DecimalFormat("Ksh: ###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;
    public static final String CHECKING_KEY = "checking_key";
    public static final String SAVINGS_KEY = "savings_key";
    //declare variables
    public String receivedBalanceC, receivedBalanceS; //data received from menu activity
    TextView cBalanceTV, sBalanceTV;
    public double cBalanceD, sBalanceD, cNewBalance, sNewBalance;
    public double SendEntered;
    int transferChoice; //spinner inde

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etmarikitipin)
    EditText etmarikitipin;


    private DataBaseAdapter db;

    EditText traderno;
    EditText agentNumber;
    EditText withdrawtET;
    EditText marikitipin;
    ImageView imgdepo;
    String  amount, pin;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private String message = "";
    private String type = "";
    private Button button_generate;
    private EditText editText1;
    private Spinner type_spinner;
    private ImageView imageView;
    private int size = 660;
    private int size_width = 660;
    private int size_height = 264;

    private TextView success_text;
    private ImageView success_imageview;

    private String time;

    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseAdapter(getApplicationContext());

        ImageView progressView = findViewById(R.id.progressdrawable);
        MaterialProgressDrawable drawable = new MaterialProgressDrawable(this, progressView);

        //scanner
       // toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
       // surfaceView = findViewById(R.id.surface_view);
        //set visibility to gone
       // surfaceView.setVisibility(View.GONE);

        //receive balance, key and title from menu activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            receivedBalance = extras.getString("balance");
            receivedKey = extras.getString("key");

        }


        //set current balance of checking or savings account
        BalanceTV = findViewById(R.id.cBalanceTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));


        imgdepo = findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set current balance of checking or savings account
       // btnAgentQrsScan = (AppCompatButton) findViewById(R.id.btnAgentQrsScan);

        //declare deposit button
     //   Button withdrawbtn = (Button) findViewById(R.id.WithdrawButton);
        //declare user deposit input amount
        //final Spinner ssDepositType = (Spinner) findViewById(R.id.ssDepositType);
        // traderno = (EditText) findViewById(R.id.ettraderNumber);
        //  agentNumber = (EditText) findViewById(R.id.etAgentNumber);
        withdrawtET = findViewById(R.id.etAmount);
        marikitipin = findViewById(R.id.etmarikitipin);

      /*  btnAgentQrsScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
        //register withdraw button with Event Listener class, and Event handler method
        /*withdrawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if withdraw field is not empty, get withdraw amount
                String marikitipin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();


                if (TextUtils.isEmpty(marikitipin)) {
                    etmarikitipin.setError("Field Required");
                    return;
                } else if (!TextUtils.isEmpty(withdrawtET.getText())) {

                    WithdrawEntered = Double.parseDouble(String.valueOf(withdrawtET.getText()));
                    //check if there's enough money to withdraw in the acoount
                    if (BalanceD >= WithdrawEntered) {
                        //create withdraw object
                        Withdraw wd = new Withdraw();
                        wd.setBalance(BalanceD);
                        wd.setWithdraw(WithdrawEntered);

                        //calculate new balance
                        NewBalance = wd.getNewBalance();

                        BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                        BalanceD = NewBalance;
                        //reset user withdraw amount to zero
                        WithdrawEntered = 0;
                        sentrequest();

                    }//end 2nd if
                    //there's not enough money in the account, prompt user for valid input
                    else
                        Toast.makeText(WithdrawFundsActivity.this, "Insufficient funds! Please enter a valid withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }//end 1st if
                //withdraw filed is empty, prompt user to enter withdraw amount
                else {
                    Toast.makeText(WithdrawFundsActivity.this, "Nothing entered! Please enter withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }

                //clear withdraw field

                withdrawtET.setText(null);
                etmarikitipin.setText(null);
            }
        });*///end Withdraw onClick



        //generate qrs
        message = "";
        type = "QR Code";
        button_generate = findViewById(R.id.generate_button);
        editText1 = findViewById(R.id.etAmount);
        type_spinner = findViewById(R.id.type_spinner);
        imageView = findViewById(R.id.image_imageview);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0: type = "QR Code";break;
                    case 1: type = "Barcode";break;
                    case 2: type = "Data Matrix";break;
                    case 3: type = "PDF 417";break;
                    case 4: type = "Barcode-39";break;
                    case 5: type = "Barcode-93";break;
                    case 6: type = "AZTEC";break;
                    default: type = "QR Code";break;
                }
                Log.d("type", type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = editText1.getText().toString();

                if (message.equals("") || type.equals(""))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(WithdrawFundsActivity.this);
                    dialog.setTitle("Error!");
                    dialog.setMessage("Invalid input!");
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
                else
                {
                    Bitmap bitmap = null;
                    try
                    {
                        bitmap = CreateImage(message, type);
                        myBitmap = bitmap;
                    }
                    catch (WriterException we)
                    {
                        we.printStackTrace();
                    }
                    if (bitmap != null)
                    {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        });

    }

    public Bitmap CreateImage(String message, String type) throws WriterException
    {
        BitMatrix bitMatrix = null;
        // BitMatrix bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);
        switch (type)
        {
            case "QR Code": bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);break;
            case "Barcode": bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_128, size_width, size_height);break;
            case "Data Matrix": bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.DATA_MATRIX, size, size);break;
            case "PDF 417": bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.PDF_417, size_width, size_height);break;
            case "Barcode-39":bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_39, size_width, size_height);break;
            case "Barcode-93":bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_93, size_width, size_height);break;
            case "AZTEC": bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.AZTEC, size, size);break;
            default: bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, size, size);break;
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int [] pixels = new int [width * height];
        for (int i = 0 ; i < height ; i++)
        {
            for (int j = 0 ; j < width ; j++)
            {
                if (bitMatrix.get(j, i))
                {
                    pixels[i * width + j] = 0xff000000;
                }
                else
                {
                    pixels[i * width + j] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public void saveBitmap (Bitmap bitmap, String message, String bitName)
    {

        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        String fileName = message + "_at_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + second + "_"  + millisecond;
        time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "." + millisecond;
        File file;

        String fileLocation;

        String folderLocation;

        if(Build.BRAND.equals("Xiaomi") )
        {
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/AndroidBarcodeGenerator/";
        }
        else
        {
            fileLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/" + fileName + bitName ;
            folderLocation = Environment.getExternalStorageDirectory().getPath()+"/DCIM/AndroidBarcodeGenerator/";
        }

        Log.d("file_location", fileLocation);

        file = new File(fileLocation);

        File folder = new File(folderLocation);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        if (file.exists())
        {
            file.delete();
        }


        FileOutputStream out;

        try
        {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // Save image
            saveBitmap(myBitmap, message, ".jpg");

            LayoutInflater layoutInflater = LayoutInflater.from(WithdrawFundsActivity.this);
            View view = layoutInflater.inflate(R.layout.success_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawFundsActivity.this);
            builder.setTitle("Summary");
            builder.setCancelable(false);
            builder.setView(view);
            success_text = view.findViewById(R.id.success_text);
            success_text.setText(message + "\n\n" + time);
            success_imageview = view.findViewById(R.id.success_imageview);
            success_imageview.setImageBitmap(myBitmap);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onPause() {

        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();

        //save new checking or savings balance
        myEditor.putString(receivedKey, String.valueOf(BalanceD));
        myEditor.apply();

    }//end onPause
    //function to open and edit shared preferences file


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        if (selection.equals("Agent Deposit")) {
            surfaceView.setVisibility(View.GONE);


        } else if (selection.equals("-Select Type-")) {
            surfaceView.setVisibility(View.GONE);

            Toast.makeText(this, "Function Required", Toast.LENGTH_SHORT);
        } else if (selection.equals("Debit/Credit Card")) {
            surfaceView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean validateSpinneracc(Spinner spinner, String error) {

        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("-Select Type-")) {
                selectedTextView.setError(error);
                Toast.makeText(getApplicationContext(), "Field Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void sentrequest() {
        amount = Objects.requireNonNull(etAmount.getText()).toString().trim();
        pin = Objects.requireNonNull(etmarikitipin.getText()).toString().trim();

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setUser_Pin(pin);

      //  db.addWithdrawals(transaction);

        new withdrawbtn().execute();

    }

    @Override
    public void onClick(View v) {

    }

    private class withdrawbtn extends AsyncTask<Void, Void, Void> {

        final String text = "Sending Request...";
        final LoadToast lt = new LoadToast(WithdrawFundsActivity.this)
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
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(WithdrawFundsActivity.this);
                    alertdialog.setMessage("Withdrawal Successfully!!!");
                    alertdialog.setCancelable(false);
                    alertdialog.setIcon(getDrawable(R.drawable.marikiti_logo));
                    alertdialog.setTitle("Withdraw");
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}//end  TransactionActivity

