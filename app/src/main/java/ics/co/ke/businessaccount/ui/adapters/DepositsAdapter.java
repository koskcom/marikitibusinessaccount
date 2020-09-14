package ics.co.ke.businessaccount.ui.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.pojo.DManager;
import ics.co.ke.businessaccount.ui.activities.BusinessDashboardActivity;
import ics.co.ke.businessaccount.ui.activities.ViewCustomer;
import ics.co.ke.businessaccount.ui.common.MyDialog.MyDialog;
import ics.co.ke.businessaccount.ui.views.DepositFundsActivity;


public class DepositsAdapter extends BaseAdapter {
    View row;
    ContactHolder contactHolder;

    View v;
    private Activity activity;
    Activity context;
    private ArrayList<DManager> dManagerlist;
    private LayoutInflater inflater = null;

    public DepositsAdapter(Context context, ArrayList<DManager> dManagerlist) {
        this.dManagerlist = dManagerlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return dManagerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        row = convertView;

        if(row==null){
            row = (row == null) ? inflater.inflate(R.layout.deposit_adapter, null) : row;

          //  LayoutInflater layoutInflater = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // row = layoutInflater.inflate(R.layout.deposit_adapter,parent,false);
            contactHolder = new ContactHolder();
            contactHolder.no = row.findViewById(R.id.no);
            contactHolder.textviewid = row.findViewById(R.id.textviewid);
            contactHolder.viewbtn = row.findViewById(R.id.btnview);
            contactHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {               // this is the onClick method for the custom button
                   /* Intent intent = new Intent(context, ViewCustomer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
//                    name = list.get(position).getName();
//                    Toast.makeText(getContext().getApplicationContext(), name+" is served ", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getContext().getApplicationContext(), "Position of "+name+" is "+position, Toast.LENGTH_LONG).show();


                    //   viewCustomer();
                  /*  callDialog cd = new callDialog();
                    cd.showBox();*/
                    //android.support.v4.app.FragmentManager fm = cd.getSupportFragmentManager();
                    //md.show(, "my_dialog");

                    //---------  another solution
//                    inflater = getActivity().getLayoutInflater();
//                    v = inflater.inflate(R.layout.custom_dialogbox, null);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setView(v).setPositiveButton("Send Treatment", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    builder.show();
                    //---------- another solution
                }
            });
          //  return v;

            row.setTag(contactHolder);
        }
        else{

            contactHolder = (ContactHolder)row.getTag();
        }

        DManager selecteditem = dManagerlist.get(position);
        contactHolder.no.setText("No:" + selecteditem.getId());
        contactHolder.textviewid.setText("Id No:" + selecteditem.getUser_national_id());

        return row;

    }

    public Activity getActivity() {
        return activity;
    }

    static class ContactHolder{
        TextView no;
        TextView textviewid;
        Button viewbtn;
    }

    class callDialog extends FragmentActivity {

        public void showBox(){
            Intent depositsintent = new Intent(getApplicationContext(), MyDialog.class);
            startActivity(depositsintent);
            /*MyDialog md = new MyDialog();
            md.show(getSupportFragmentManager(), "my_dialog_box");*/
        }
    }

    public void viewCustomer() {
        // get alert_dialog.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_view_customer_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

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
                       // Toast.makeText(getApplicationContext(), "Entered: " + nationalid.getText().toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "Entered: " + userInputamonut.getText().toString(), Toast.LENGTH_LONG).show();
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

    /*public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.deposit_adapter, null) : itemview;

        TextView viewbtn = (TextView) itemview.findViewById(R.id.btnview);
        TextView no = (TextView) itemview.findViewById(R.id.no);
        TextView textviewid = (TextView) itemview.findViewById(R.id.textviewid);

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //funtionality of edit button
            }
        });
        DManager selecteditem = dManagerlist.get(position);
        no.setText("No:" + selecteditem.getId());
        textviewid.setText("Id No:" + selecteditem.getUser_national_id());

        return itemview;

    }*/

}
