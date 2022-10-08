package com.example.fad.farmers;

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
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
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

public class farmers extends Fragment {
    TextView fcode,fname,mobileno;
    SearchView searchView;
    Switch active;
    ArrayAdapter arrayAdapter;
    ArrayList<farmersdata> arrrayList;
    DatabaseHelper mDatabaseHelper;
    listfarmers myAdapter;
    SwipeMenuListView lvfarmers;
    MenuItem fav;
    public static String searchtext;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.farmers, container, false);
        fcode=(TextView)v.findViewById(R.id.txtfcode);
        fname=(TextView)v.findViewById(R.id.txtfname);
        mobileno=(TextView)v.findViewById(R.id.txtmno);
        active=(Switch)v.findViewById(R.id.active);
        lvfarmers=(SwipeMenuListView)v.findViewById(R.id.lvfarmers);
        arrrayList = new ArrayList<>();
        searchView =(SearchView)v.findViewById(R.id.searchview);
        setHasOptionsMenu(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchtext=searchView.getQuery().toString();
                getsearch();
                return false;
            }
        });
        mDatabaseHelper = new DatabaseHelper(getContext());
        refreshfarmersview();
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
        lvfarmers.setMenuCreator(creator);
        lvfarmers.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        farmersdata edata = arrrayList.get(position);
                        String eid = edata.getSlno();
                        String code = edata.getCode();
                        String name = edata.getFname();
                        String mno = edata.getMobileno();
                        String mtype = edata.getMtype();
                        String active = edata.getActive();
                        Bundle args = new Bundle();
                        args.putString("slno",eid);
                        args.putString("code",code);
                        args.putString("name",name);
                        args.putString("mno",mno);
                        args.putString("mtype",mtype);
                        args.putString("active",active);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        mfarmers f3 = new mfarmers();
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
                        dialog.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        farmersdata mdata = arrrayList.get(position);
                                        String slno = mdata.getSlno();
                                        mDatabaseHelper.deletefarmerrecord(slno);
                                        refreshfarmersview();
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
                    mfarmers f3 = new mfarmers();
                    ft.replace(R.id.fragment_container, f3);
                    ft.addToBackStack(null);
                    ft.commit();
                return true;
            }
        });
    }
    /*public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if(count == 0) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mfarmers f3 = new mfarmers();
            ft.replace(R.id.fragment_container, f3);
            ft.commit();
        }else {
            getFragmentManager().popBackStack();
        }
    }*/

    private void getsearch() {
        arrrayList = mDatabaseHelper.getfarmersearch();
        myAdapter = new listfarmers(getContext(), arrrayList);
        lvfarmers.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    private  void refreshfarmersview(){
        try {
            arrrayList = mDatabaseHelper.getfdata();
            myAdapter = new listfarmers(getContext(), arrrayList);
            lvfarmers.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
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

}
