package com.example.fad.Rates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fad.R;

import java.util.ArrayList;

public class listrates extends BaseAdapter {
    Context context;
    private final LayoutInflater layoutInflater;
    ArrayList<ratesdata> arrayList;
    public listrates(Context context,ArrayList<ratesdata> arrayList){
        this.context=context;
        this.arrayList=arrayList;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listmrates, null);
        TextView bcode = (TextView)convertView.findViewById(R.id.txtbcode);
        TextView fdate = (TextView)convertView.findViewById(R.id.txtfdate);
        TextView tdate =(TextView) convertView.findViewById(R.id.txttdate);
        TextView mtype =(TextView) convertView.findViewById(R.id.txtmtype);
        TextView fatmin =(TextView) convertView.findViewById(R.id.txtfatmin);
        TextView fatmax=(TextView) convertView.findViewById(R.id.txtfatmax);
        TextView snfmin =(TextView) convertView.findViewById(R.id.txtsnfmin);
        TextView snfmax =(TextView) convertView.findViewById(R.id.txtsnfmax);
        TextView tsrate =(TextView) convertView.findViewById(R.id.txttsrate);
        ratesdata rdata = arrayList.get(position);
        bcode.setText(rdata.getBcode());
        fdate.setText(String.valueOf(rdata.getFdate()));
        tdate.setText(String.valueOf(rdata.getTdate()));
        mtype.setText(rdata.getMtype());
        fatmin.setText(rdata.getFatmin());
        fatmax.setText(rdata.getFatmax());
        snfmin.setText(rdata.getSnfmin());
        snfmax.setText(rdata.getSnfmax());
        tsrate.setText(rdata.getTsrate());
        return convertView;
    }
}
