package ics.co.ke.businessaccount.ui.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ics.co.ke.businessaccount.R;

public class CustomerCareActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ImageView imgdepo;

    private Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_care_activity);
        ButterKnife.bind(this);

        Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonOne = findViewById(R.id.button_one);
        buttonTwo = findViewById(R.id.button_two);
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        imgdepo = findViewById(R.id.imgdepo);
        imgdepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }//end onCreate
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_one:
                String numOne = "0721542158";
                makePhoneDial(numOne);
                break;

            case R.id.button_two:
                String numTwo = "0724514582";
                makePhoneDial(numTwo);
                break;

        }
    }


    private void makePhoneDial(String number) {

        Intent i = new Intent(Intent.ACTION_DIAL);
        if(number.isEmpty()) {

            i.setData(Uri.parse("tel:7788994455"));
        } else {
            i.setData(Uri.parse("tel:"+number));
        }
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}













