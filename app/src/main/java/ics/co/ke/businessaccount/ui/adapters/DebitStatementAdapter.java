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
import ics.co.ke.businessaccount.pojo.DebitPurchase;


public class DebitStatementAdapter extends BaseAdapter {
    Activity context;
    private ArrayList<DebitPurchase> debitPurchaselist;
    private LayoutInflater inflater = null;

    public DebitStatementAdapter(Context context, ArrayList<DebitPurchase> debitPurchaselist) {
        this.debitPurchaselist = debitPurchaselist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return (null != debitPurchaselist ? debitPurchaselist.size() : 0);
        //return dManagerlist.size();
    }


    @Override
    public Object getItem(int position) {
        return debitPurchaselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentview, ViewGroup parent) {
        View itemview = contentview;

        itemview = (itemview == null) ? inflater.inflate(R.layout.debit_purchase_statement_adapter_list, null) : itemview;

        TextView no = itemview.findViewById(R.id.no);
        TextView txtbankname = itemview.findViewById(R.id.txtviewbankname);
        TextView txtbankac = itemview.findViewById(R.id.txviewbankac);
        TextView txtmarikitino = itemview.findViewById(R.id.txtviewmarikitino);
        TextView txtamount = itemview.findViewById(R.id.txtviewamount);
        TextView transanctiontype = itemview.findViewById(R.id.txttransactiontype);

        DebitPurchase selecteditem = debitPurchaselist.get(position);

        no.setText(" " + selecteditem.getId());
        txtbankname.setText("  " + selecteditem.getBank__acc_namer());
        txtbankac.setText("  " + selecteditem.getBank_acc_number());
        txtmarikitino.setText("  " + selecteditem.getMarikiti_number());
        txtamount.setText("  " + selecteditem.getAmount());
        transanctiontype.setText("  " + selecteditem.getTrans_type());

        return itemview;

    }
}

