package ics.co.ke.businessaccount.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ics.co.ke.businessaccount.R;
import ics.co.ke.businessaccount.model.Transaction;
import ics.co.ke.businessaccount.pojo.CreditPurchase;

public class TransactionListAdapter extends BaseAdapter {
    Activity context;
    private ArrayList<Transaction> transactionlist;
    private LayoutInflater inflater = null;

    public TransactionListAdapter(Context context, ArrayList<Transaction> transactionlist) {
        this.transactionlist = transactionlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return (null != transactionlist ? transactionlist.size() : 0);
        //return dManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return transactionlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.credit_purchase_statement_list, null) : itemview;

        TextView customername = itemview.findViewById(R.id.textviewcustomername);
        TextView txtpphonenumber = itemview.findViewById(R.id.textviewphonenumber);
        TextView txtcredit = itemview.findViewById(R.id.textviewcredit);
        TextView txtdebit = itemview.findViewById(R.id.textviewdebit);
        TextView textdate = itemview.findViewById(R.id.textviewdate);
        TextView textcreditedid = itemview.findViewById(R.id.txtviewtransactionCreditId);
        TextView textdebitedid = itemview.findViewById(R.id.textviewdebitTransactionId);

        Transaction selecteditem = transactionlist.get(position);

        customername.setText(" Name: " + selecteditem.getUsername());
        txtpphonenumber.setText("  " + selecteditem.getPhone_Number());
        txtcredit.setText("  " + selecteditem.getCredit_amount());
        txtdebit.setText("  " + selecteditem.getDebit_amount());
        textdate.setText("  " + selecteditem.getDate());
        textcreditedid.setText("  " + selecteditem.getCredit_amount_Transaction_number());
        textdebitedid.setText("  " + selecteditem.getDebit_amount_Transaction_number());

        return itemview;

    }
}


