package com.example.fad.farmers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fad.R;

import java.util.ArrayList;

public class listfarmers extends BaseAdapter {
    Context context;
    private final LayoutInflater layoutInflater;
    ArrayList<farmersdata> arrayList;

    public listfarmers  (Context context,ArrayList<farmersdata> arrayList){
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
    public View getView(int position, View tView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tView = inflater.inflate(R.layout.listfarmers, null);
        TextView code = (TextView) tView.findViewById(R.id.txtfcode);
        TextView fname = (TextView) tView.findViewById(R.id.txtfname);
        TextView mobileno = (TextView) tView.findViewById(R.id.txtmno);
        TextView mtype =(TextView)tView.findViewById(R.id.txtmytpe);
        Switch active = (Switch) tView.findViewById(R.id.active);
        active.setClickable(false);
        farmersdata fdata = arrayList.get(position);
        code.setText(fdata.getCode());
        fname.setText(fdata.getFname());
        mobileno.setText(fdata.getMobileno());
        mtype.setText(fdata.getMtype());
        active.setChecked(fdata.getActive().equals("1"));
        return tView;
    }

}
