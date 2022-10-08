package com.example.fad.reports;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;
import com.example.fad.milkcollection.listmilkdata;
import com.example.fad.milkcollection.milkdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class report extends Fragment {
    MenuItem fav;
    DatabaseHelper mDatabaseHelper;
    ArrayList <milkdata> arrayList;
    ArrayAdapter arrayAdapter;
    TextView subtotal,avgfat,avgsnf,totalamt;
    listmilkdata myAdapter;
    private int mdate, mmonth, myear;
    SwipeMenuListView lvdata;
    public static String dfshift,dfmtype,dfdate,dtdate,drfcode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reports, container, false);
        lvdata=(SwipeMenuListView)v.findViewById(R.id.listview);
        subtotal=(TextView)v.findViewById(R.id.subtotal);
        avgfat=(TextView)v.findViewById(R.id.avgfat);
        avgsnf=(TextView)v.findViewById(R.id.avgsnf);
        totalamt=(TextView)v.findViewById(R.id.totalamt);
        getdate();
        this.setHasOptionsMenu(true);
        refreshlistview();
        return v;
    }
    private void loadtotal(){
        float qty = 0, amt = 0, ltrfat = 0, ltrsnf = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            milkdata u = arrayList.get(i);
            qty += Double.parseDouble(u.getQuantity());
            ltrfat += (Double.parseDouble(u.getFat()) * Double.parseDouble(u.getQuantity())) / 100;
            ltrsnf += Double.parseDouble(u.getSnf()) * Double.parseDouble(u.getQuantity()) / 100;
            amt += Double.parseDouble(u.getAmount());
        }
        //DecimalFormat df2 = new DecimalFormat("#.#");
        subtotal.setText(String.valueOf(qty));
        //System.out.println((ltrfat / qty) / 100);
        //System.out.println("%.1f",(ltrfat/qty)/100);
        avgfat.setText(String.valueOf((ltrfat / qty) / 100));
        avgsnf.setText(String.valueOf((ltrsnf / qty) / 100));
        totalamt.setText(String.valueOf(amt));
    }
    private void getdate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(date);
        dfdate=formattedDate;
        dtdate=formattedDate;
    }
    private void refreshlistview() {
        try {
            arrayList = mDatabaseHelper.getrdata();
            myAdapter = new listmilkdata(getContext(), arrayList);
            lvdata.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            loadtotal();
        }catch (Exception ex){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(ex.getMessage());
            dialog.setTitle("Error");
            dialog.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        fav = menu.add("Filter");
        fav.setIcon(R.drawable.filter);
        fav.setShowAsAction(1);
        fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                filter_report f3 = new filter_report();
                ft.replace(R.id.fragment_container, f3);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });
    }
}
