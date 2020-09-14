package ics.co.ke.businessaccount.ui.common.MyDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import ics.co.ke.businessaccount.R;

/**
 * Created by Sohan on 7/13/2016.
 */
public class MyDialog extends DialogFragment {

    LayoutInflater inflater;
    View v;
    public Dialog  onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.activity_view_customer_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("Send Treatment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}