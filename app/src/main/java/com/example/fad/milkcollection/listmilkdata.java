package com.example.fad.milkcollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fad.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class listmilkdata extends BaseAdapter {
    Context context;
    private final LayoutInflater layoutInflater;
    ArrayList <milkdata> arrayList;
    public void remove(int position) {
        arrayList.remove(position);
        notifyDataSetChanged();
    }
    public listmilkdata(Context context,ArrayList<milkdata> arrayList){
        this.context=context;
        this.arrayList=arrayList;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override

    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listmilkdata, null);
            TextView id = (TextView) convertView.findViewById(R.id.txtslno);
            TextView code = (TextView) convertView.findViewById(R.id.txtccode);
            TextView name = (TextView) convertView.findViewById(R.id.txtcname);
            TextView date = (TextView) convertView.findViewById(R.id.txtdate);
            TextView shift = (TextView) convertView.findViewById(R.id.txtshift);
            TextView mtype = (TextView) convertView.findViewById(R.id.txtmtype);
            TextView quantity = (TextView) convertView.findViewById(R.id.txtqty);
            TextView fat = (TextView) convertView.findViewById(R.id.txtfat);
            TextView snf = (TextView) convertView.findViewById(R.id.txtsnf);
            TextView rate = (TextView) convertView.findViewById(R.id.txtrate);
            TextView amount = (TextView) convertView.findViewById(R.id.txtamt);
            milkdata mdata = arrayList.get(position);
            id.setText(mdata.getSlno());
            code.setText(mdata.getCode());
            name.setText(mdata.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dat = sdf.format(mdata.getDate());
            date.setText(dat);
            shift.setText(mdata.getShift());
            mtype.setText(mdata.getMtype());
            quantity.setText(mdata.getQuantity());
            fat.setText(mdata.getFat());
            snf.setText(mdata.getSnf());
            rate.setText(mdata.getRate());
            amount.setText(mdata.getAmount());
        }
            return convertView;
    }

}