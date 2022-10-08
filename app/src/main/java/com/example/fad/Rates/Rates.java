package com.example.fad.Rates;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Rates extends Fragment {
    WebView wvrates;
    Button bt,save;
    SQLiteDatabase db;
    DatabaseHelper mDatabaseHelper;
    LinearLayout layouttdate,layoutfdate;
    TextView txttdate,txtfdate,txtslno;
    private int mdate,mmonth,myear;
    EditText rbcode,txtfatmin,txtfatmax,txtsnfmin,txtsnfmax,txttsrate;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rates, container, false);
        mDatabaseHelper= new DatabaseHelper(Rates.this.getContext());
        rbcode = (EditText)v.findViewById(R.id.bcode);
        txtfatmin =(EditText)v.findViewById(R.id.txtfatmin);
        txtfatmax=(EditText)v.findViewById(R.id.txtfatmax);
        txtsnfmin=(EditText)v.findViewById(R.id.txtsnfmin);
        txtsnfmax=(EditText)v.findViewById(R.id.txtsnfmax);
        txttsrate=(EditText)v.findViewById(R.id.txttsrate);
        txttdate=(TextView)v.findViewById(R.id.txttdate);
        txtfdate=(TextView)v.findViewById(R.id.txtfdate);
        txtslno =(TextView)v.findViewById(R.id.txtslno);
        layoutfdate=(LinearLayout)v.findViewById(R.id.layoutfdate);
        layouttdate=(LinearLayout)v.findViewById(R.id.layoutdate);
        save =(Button)v.findViewById(R.id.save);
        String[] arraySpinner = new String[] {
                "Cow", "Buff"
        };
        Spinner rmtype = (Spinner)v.findViewById(R.id.spinnermtype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rmtype.setAdapter(adapter);

        txttdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                mdate=cal.get(Calendar.DATE);
                mmonth = cal.get(Calendar.MONTH);
                myear= cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int monthOfYear, int month, int date) {
                        month=month+1;
                        txttdate.setText(date + "/" + month + "/" + monthOfYear);
                    }
                },myear,mmonth,mdate);
                datePickerDialog.show();
            }
        });
        txtfdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                mdate=cal.get(Calendar.DATE);
                mmonth = cal.get(Calendar.MONTH);
                myear= cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int monthOfYear, int month, int date) {
                        month=month+1;
                        txtfdate.setText(date + "/" + month + "/" + monthOfYear);
                    }
                },myear,mmonth,mdate);
                datePickerDialog.show();
            }
        });


       /* wvrates =(WebView)v.findViewById(R.id.wvRateCard);
        bt=(Button)v.findViewById(R.id.btclick);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://www.priyamilk.com/webmail";
                wvrates.getSettings().setLoadsImagesAutomatically(true);
                wvrates.getSettings().setJavaScriptEnabled(true);
                wvrates.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wvrates.loadUrl(url);
            }
        });
        wvrates.setWebViewClient(new loadrates());*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbcode.getText().toString().isEmpty()||txtfatmin.getText().toString().isEmpty()||txtfatmax.getText().toString().isEmpty()||txtsnfmin.getText().toString().isEmpty()||txtsnfmax.getText().toString().isEmpty()||txttsrate.getText().toString().isEmpty()){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                    dialog.setMessage("Please fill all the fields");
                    dialog.setTitle("Error");
                    dialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }
                else {
                    if (txtslno.getText().equals("slno")) {
                        try {
                            //mDatabaseHelper.onCreate(db);
                            String bcode = rbcode.getText().toString();
                            String rcid ="1";
                            String mtype = rmtype.getSelectedItem().toString();
                            String dat=txtfdate.getText().toString();
                            long date=new SimpleDateFormat("dd/MM/yyyy").parse(dat).getTime();
                            java.sql.Date fdate=new java.sql.Date(date);
                            String tdat=txttdate.getText().toString();
                            long ttdate=new SimpleDateFormat("dd/MM/yyyy").parse(tdat).getTime();
                            java.sql.Date tdate=new java.sql.Date(ttdate);
                            String fatmin = txtfatmin.getText().toString();
                            String fatmax = txtfatmax.getText().toString();
                            String snfmin = txtsnfmin.getText().toString();
                            String snfmax = txtsnfmax.getText().toString();
                            String tsrate = txttsrate.getText().toString();
                            Boolean addData = mDatabaseHelper.addratesData(bcode,rcid, mtype, fdate, tdate, fatmin, fatmax, snfmin, snfmax, tsrate);
                            //System.out.println(rmtype.getSelectedItem().toString());
                            //refreshlistview();
                            //clearfields();
                            if (addData.equals(true)) {
                                rbcode.setText("");
                                txtfdate.setText("");
                                txttdate.setText("");
                                txtfatmin.setText("");
                                txtfatmax.setText("");
                                txtsnfmin.setText("");
                                txtsnfmax.setText("");
                                txttsrate.setText("");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Rates Saved");
                                dialog.setTitle("Success");
                                dialog.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            } else {
                                Snackbar.make(v, "ERROR comming from here...!", Snackbar.LENGTH_LONG).show();
                            }
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
                    else{
                        try {
                            //mDatabaseHelper.onCreate(db);
                            String slno = txtslno.getText().toString();
                            String bcode = rbcode.getText().toString();
                            String rcid ="1";
                            String mtype = rmtype.getSelectedItem().toString();
                            Date fdate = Date.valueOf(txtfdate.getText().toString());
                            Date tdate = Date.valueOf(txttdate.getText().toString());
                            String fatmin = txtfatmin.getText().toString();
                            String fatmax = txtfatmax.getText().toString();
                            String snfmin = txtsnfmin.getText().toString();
                            String snfmax = txtsnfmax.getText().toString();
                            String tsrate = txttsrate.getText().toString();
                            Boolean updateRate = mDatabaseHelper.updateratesData(slno,bcode,rcid, mtype, fdate, tdate, fatmin, fatmax, snfmin, snfmax, tsrate);
                            //System.out.println(rmtype.getSelectedItem().toString());
                            //refreshlistview();
                            //clearfields();
                            if (updateRate.equals(true)) {
                                rbcode.setText("");
                                txtfdate.setText("");
                                txttdate.setText("");
                                txtfatmin.setText("");
                                txtfatmax.setText("");
                                txtsnfmin.setText("");
                                txtsnfmax.setText("");
                                txttsrate.setText("");
                                save.setText("save");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Rates Updated");
                                dialog.setTitle("Success");
                                dialog.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            } else {
                                Snackbar.make(v, "ERROR!", Snackbar.LENGTH_LONG).show();
                            }
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
            }
        });
        if(getArguments()==null){

        }else {
            txtslno.setText(getArguments().getString("slno"));
            if(getArguments().getString("mtype").equals("Cow")){
                rmtype.setSelection(0);
            }
            else {
                rmtype.setSelection(1);
            }
            rbcode.setText(getArguments().getString("bcode"));
            txtfdate.setText(getArguments().getString("fdate"));
            txttdate.setText(getArguments().getString("tdate"));
            txtfatmin.setText(getArguments().getString("minfat"));
            txtfatmax.setText(getArguments().getString("maxfat"));
            txtsnfmin.setText(getArguments().getString("minsnf"));
            txtsnfmax.setText(getArguments().getString("maxsnf"));
            txttsrate.setText(getArguments().getString("tsrate"));
            save.setText("Update");
        }
        return v;
    }

}
