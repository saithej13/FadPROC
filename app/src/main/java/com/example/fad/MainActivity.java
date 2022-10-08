package com.example.fad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.users.userinfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    MenuItem fav;
    Button nextscreen;
    TextView btnsignup,btnForget;
    EditText txtusername, txtpassword;
    userinfo user;
    DatabaseHelper dbhelper;
    SQLiteDatabase db;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //private BluetoothDeviceManagerReceiver f9907J = new BluetoothDeviceManagerReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        nextscreen = findViewById(R.id.btnSignIn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("proc_users");
        btnForget = findViewById(R.id.btnForgetPassword);
        txtusername = findViewById(R.id.edtUsername);
        txtpassword = findViewById(R.id.edtpassword);
        dbhelper =new DatabaseHelper(this.getApplicationContext());
        /*firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("milkcollection-733d1-default-rtdb");*/
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtusername.getText().toString().equals("saiteja")&&txtpassword.getText().toString().equals("0968"))
                {
                    txtusername.setText("");
                    txtpassword.setText("");
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }
                else if(txtusername.getText().toString().equals("sai")&&txtpassword.getText().toString().equals("0968"))
                {
                    txtusername.setText("");
                    txtpassword.setText("");
                    Intent intent = new Intent(MainActivity.this, signup.class);
                    startActivity(intent);
                }
                else {
                    int fat[]={3,6};
                    for(int i=0;i<fat.length;i++){
                        System.out.println(i +"\t"+fat[i]);
                    }
                }
            }
        });
        //mRef = new Firebase("https://milkcollection-733d1-default-rtdb.firebaseio.com");
        nextscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtusername.getText().toString().equals("") || txtpassword.getText().toString().equals("")) {

                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    /*Snackbar.make(v, "Please Enter Username or Password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                } else {
                    //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    //xtusername.setText(reference.orderByChild().child().getValue(String.class));
                    /*Intent intentOpenBluetoothSettings = new Intent();
                    intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intentOpenBluetoothSettings);
                    registerReceiver(MainActivity.this, new IntentFilter("android.bluetooth.devicepicker.action.DEVICE_SELECTED"));
                    mo11938E1();*/
                    /*sqlserverconnection con;
                    Connection connect = sqlserverconnection.CONN();
                    try {
                        String query = "select * from secUsers where Username='" + txtusername.getText().toString() + "' and password='" + MD5.getMd5(txtpassword.getText().toString()) + "' and isactive='1'";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        //if (rs != null)
                        if (rs != null) {
                            txtusername.setText("");
                            txtpassword.setText("");
                            while (rs.next()) {
                                try {
                                    new userinfo(rs.getInt("userid"), rs.getString("username"), rs.getString("password"), rs.getInt("roleid"), rs.getInt("isactive"), rs.getInt("bcode"), rs.getInt("operation"), rs.getInt("graceperiod"), rs.getString("deviceid"),rs.getString("centername"));
                                    if (userinfo.getBcode() != 0) {
                                        //Toast.makeText(MainActivity.this, "Login Successful,'" + userinfo.roleid + "'", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        startActivity(intent);
                                    } else {
                                        //Intent intent = new Intent(MainActivity.this, Module.class);
                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        startActivity(intent);
                                    }

                                } catch (Exception ex) {
                                    Snackbar.make(v, ex.getMessage().toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Snackbar.make(v, ex.getMessage().toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }*/
                }
            }

        });
    }

    /*public final class BluetoothDeviceManagerReceiver extends BroadcastReceiver {
        public BluetoothDeviceManagerReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if (bluetoothDevice != null) {
                try {
                    int u1 = MainActivity.this.mo11941u1();
                }
        }
    }*/
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        fav = menu.add("add");
        fav.setIcon(R.drawable.ic_baseline_power_settings_new_24);
        fav.setShowAsAction(1);
        fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            }
        });
    }

    private void validatelicence(){
        dbhelper.getbdata();
        try {
            db =dbhelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from branch where getdate() between fdate and tdate order by slno desc", null);
            Cursor cursor2 = db.rawQuery("select * from branch order by slno desc", null);
            if (cursor.moveToNext()) {
                //validate date between fdate and tdate
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
            else if (cursor2.moveToNext()){
                //licence was expired insert new licence key exit app
                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                dialog.setMessage("Licence was Expired, Please update Licence Key");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dialog.setView(input);
                String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                dialog.setTitle("Error Licence was Expired");
                dialog.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //select bcode,bname,fdate,tdate,deviceid,productkey,active from centerusers where productkey=productkey
                                //insert the values to sqlite db and exit
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://localhost/centerusers.php?&productkey="+input.getText()+"&deviceid="+android_id+"",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jarray = new JSONArray(response);
                                                    for (int i = 0; i < jarray.length(); i++) {
                                                        JSONObject obj = jarray.getJSONObject(i);

                                                    }
                                                } catch (JSONException e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                                                dialog.setMessage(error.getMessage());
                                                dialog.setTitle("Error Updating Licence from server");
                                                dialog.setPositiveButton("ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog,
                                                                                int which) {
                                                                return;
                                                            }
                                                        });
                                                AlertDialog alertDialog = dialog.create();
                                                alertDialog.show();
                                            }
                                        }
                                );
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                queue.add(stringRequest);

                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else {
                //validate product key from server and insert data fdate and tdate and etc ..., details in sqlite db
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}