package com.example.fad.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fad.R;
import com.example.fad.Rates.Rates;

public class milkdataview extends Fragment{
    MenuItem fav;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.milkdataview, container, false);
        setHasOptionsMenu(true);
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
                ft.commit();
                return true;
            }
        });
    }
}
