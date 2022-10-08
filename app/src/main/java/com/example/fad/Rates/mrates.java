package com.example.fad.Rates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;

import java.util.ArrayList;

public class mrates extends Fragment {
    ArrayList<ratesdata> arrrayList;
    DatabaseHelper mDatabaseHelper;
    listrates myAdapter;
    SwipeMenuListView lvrates;
    MenuItem fav;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mrates, container, false);
        arrrayList = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(getContext());
        lvrates = (SwipeMenuListView) v.findViewById(R.id.lvrates);
        setHasOptionsMenu(true);

        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem editItem = new SwipeMenuItem(getContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                editItem.setWidth(170);
                editItem.setIcon(R.drawable.ic_baseline_edit_24);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_action_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        lvrates.setMenuCreator(creator);
        lvrates.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        ratesdata edata = arrrayList.get(position);
                        String eid = edata.getSlno();
                        String bcode = edata.getBcode();
                        String fdate = edata.getFdate();
                        String tdate = edata.getTdate();
                        String mtype = edata.getMtype();
                        String minfat = edata.getFatmin();
                        String maxfat = edata.getFatmax();
                        String minsnf = edata.getSnfmin();
                        String maxsnf = edata.getSnfmax();
                        String tsrate = edata.getTsrate();
                        Bundle args = new Bundle();
                        args.putString("slno",eid);
                        args.putString("bcode",bcode);
                        args.putString("fdate",String.valueOf(fdate));
                        args.putString("tdate",String.valueOf(tdate));
                        args.putString("mtype",mtype);
                        args.putString("minfat",minfat);
                        args.putString("maxfat",maxfat);
                        args.putString("minsnf",minsnf);
                        args.putString("maxsnf",maxsnf);
                        args.putString("tsrate",tsrate);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Rates f3 = new Rates();
                        f3.setArguments(args);
                        ft.replace(R.id.fragment_container, f3);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 1:
                        // delete

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setMessage("Are you sure to Delete ?");
                        dialog.setTitle("Delete");
                        dialog.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ratesdata mdata = arrrayList.get(position);
                                        String slno = mdata.getSlno();
                                        mDatabaseHelper.deleteraterecord(slno);
                                        refreshlistview();
                                    }
                                });
                        dialog.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        return;
                                    }
                                });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                        //String code = mdata.getCode();
                        //Log.d(TAG, "onMenuItemClick: Item was Clicked"+rate);
                        //mDatabaseHelper.deletemilkrecord(id,code);
                        break;
                }
                return false;
            }
        });
        refreshlistview();
        lvrates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        fav = menu.add("add");
        fav.setIcon(R.drawable.ic_add_circle);
        fav.setShowAsAction(1);
        fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Rates f3 = new Rates();
                ft.replace(R.id.fragment_container, f3);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });
    }

    private void refreshlistview() {
        try {
            arrrayList = mDatabaseHelper.getratesData();
            myAdapter = new listrates(getContext(), arrrayList);
            lvrates.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            //System.out.println(arrrayList.size());
        } catch (Exception ex) {
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
}

