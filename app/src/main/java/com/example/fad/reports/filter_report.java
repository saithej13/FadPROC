package com.example.fad.reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;

import java.util.Calendar;

public class filter_report extends Fragment {
    LinearLayout fromdate,todate;
    Spinner shift,mtype;
    private int mdate,mmonth,myear;
    EditText fcode;
    TextView frname,fdate,tdate;
    Button apply;
    SQLiteDatabase db;
    DatabaseHelper mDatabaseHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_report, container, false);
        fromdate =(LinearLayout)v.findViewById(R.id.layoutfromdate);
        todate =(LinearLayout)v.findViewById(R.id.layouttodate);
        fdate=(TextView)v.findViewById(R.id.txtfromdate);
        tdate=(TextView)v.findViewById(R.id.txttodate);
        fcode=(EditText)v.findViewById(R.id.farmerid);
        frname=(TextView)v.findViewById(R.id.fname);
        apply=(Button)v.findViewById(R.id.apply);
        this.setHasOptionsMenu(true);
        String[] arraySpinner = new String[] {
                "Both", "Cow", "Buff"
        };
        Spinner s = (Spinner)v.findViewById(R.id.spinnermtype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        String[] arraySpinnershift = new String[] {
                "Both", "M", "E"
        };
        Spinner shift = (Spinner)v.findViewById(R.id.spinnershift);
        ArrayAdapter<String> adaptershift = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, arraySpinnershift);
        adaptershift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shift.setAdapter(adaptershift);
        fromdate.setOnClickListener(new View.OnClickListener() {
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
                        fdate.setText(date+"-"+month+"-"+monthOfYear);
                    }
                },myear,mmonth,mdate);
                datePickerDialog.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
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
                        tdate.setText(date+"-"+month+"-"+monthOfYear);
                    }
                },myear,mmonth,mdate);
                datePickerDialog.show();
            }
        });
        fcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
                //Cursor c = sqLiteDatabase.rawQuery("select name from farmerdata where code='"+fcode.getText().toString()+"'",null);
                //fname.setText(c.getString(c.getColumnIndex("fname")).toString()+c.getString(c.getColumnIndex("lname")).toString());
                String code = fcode.getText().toString();
                if (code.isEmpty()) {

                } else {
                    try {
                        db = mDatabaseHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("select fname from farmerdata where active='1' and code=" + fcode.getText().toString(), null);
                        if (cursor != null) {
                            if (cursor.getCount() > 0) {
                                cursor.moveToNext();
                                frname.setText(cursor.getString(0));
                            } else {
                                frname.setText("");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Code : " + fcode.getText().toString() + " Does not Exists or Not Active");
                                dialog.setTitle("Error");
                                dialog.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                fcode.setText("");
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                        }
                    }catch (SQLException ex){
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
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("fdate",fdate.getText().toString());
                args.putString("tdate",tdate.getText().toString());
                args.putString("fcode",fcode.getText().toString());
                args.putString("shift",shift.getSelectedItem().toString());
                args.putString("mtype",s.getSelectedItem().toString());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                report f3 = new report();
                f3.setArguments(args);
                ft.replace(R.id.fragment_container, f3);
                ft.commit();
            }
        });
        return v;
    }

}
