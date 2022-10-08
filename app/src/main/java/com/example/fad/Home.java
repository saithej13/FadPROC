package com.example.fad;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fad.Rates.mrates;
import com.example.fad.farmers.farmers;
import com.example.fad.milkcollection.milkcollection;
import com.example.fad.reports.report;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    public static String CURRENT_TAG = "Home";
    MenuItem fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_home);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().getDisplayOptions();
        //getSupportActionBar().setIcon(R.drawable.ic_add_circle);
        //setHasOptionsMenu(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            CURRENT_TAG="Home";
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*fav = menu.add("add");
        fav.setIcon(R.drawable.ic_print);
        fav.setShowAsAction(1);
        fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //print(mConnection, mInterface);
                return true;
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            if (item.getItemId() == android.R.id.home) { // use android.R.id
                toggle();
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                /*case R.id.nav_Home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new load_devices()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;*/
                case R.id.nav_Collection:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new milkcollection()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_Farmers:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new farmers()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_Rates:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new mrates()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_Setttings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new settings()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                case R.id.nav_Reports:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new report()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    break;
            }

            return true;
        }
}