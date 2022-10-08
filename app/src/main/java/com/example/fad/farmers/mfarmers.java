package com.example.fad.farmers;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;
import com.google.android.material.snackbar.Snackbar;

public class mfarmers extends Fragment{
    EditText mcode,mfname,mlname,mmobiLeno;
    Spinner mmtype;
    Switch mactive;
    public static String sactive;
    Button save;
    SQLiteDatabase db;
    TextView sno;
    DatabaseHelper mfarmerHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_farmers, container, false);
        mcode=(EditText)v.findViewById(R.id.farmerid);
        mfname=(EditText)v.findViewById(R.id.fname);
        //mlname =(EditText)v.findViewById(R.id.lname);
        mmobiLeno =(EditText)v.findViewById(R.id.mno);
        mactive =(Switch)v.findViewById(R.id.switchactive);
        sno=(TextView)v.findViewById(R.id.txtslno);
        mfarmerHelper= new DatabaseHelper(mfarmers.this.getContext());
        save=(Button)v.findViewById(R.id.save);
        String[] arraySpinner = new String[] {
                "Cow", "Buff", "Both"
        };
        Spinner s = (Spinner)v.findViewById(R.id.spinnermtype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.mtype_adapter,R.layout.fragment_farmers);
        //mmtype.setAdapter(adapter);
        mcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(mcode.getText().toString().isEmpty()){

                }
                else {
                    try {
                        db = mfarmerHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("select * from farmerdata where code=" + mcode.getText().toString(), null);
                        if (cursor != null) {
                            if (cursor.getCount() > 0) {
                                cursor.moveToNext();
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Code : " + mcode.getText().toString() + " is Already Exists");
                                dialog.setTitle("Error");
                                dialog.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                mcode.setText("");
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                        }
                    }
                    catch (SQLException ex){
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    load();
                    if (mcode.getText().toString().isEmpty() || mfname.getText().toString().isEmpty() || mmobiLeno.getText().toString().isEmpty()) {
                        Snackbar.make(v, "please fill all the fields!", Snackbar.LENGTH_LONG).show();
                    } else {
                        if (sno.getText().equals("slno")) {
                            try {
                                String code = mcode.getText().toString();
                                String fname = mfname.getText().toString();
                                String mobileno = "+91"+mmobiLeno.getText().toString();
                                String mtype = s.getSelectedItem().toString();
                                String active = sactive;
                                Boolean addData = mfarmerHelper.addfData(code, fname, mobileno, mtype, sactive);
                                clearfields();
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Farmer Saved");
                                dialog.setTitle("Success");
                                dialog.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                            catch (Exception ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        } else {
                            try {
                                String slno = sno.getText().toString();
                                String code = mcode.getText().toString();
                                String fname = mfname.getText().toString();
                                String mobileno = mmobiLeno.getText().toString();
                                String mtype = s.getSelectedItem().toString();
                                String active = sactive;
                                Boolean updatefarmers = mfarmerHelper.updatefarmers(slno, code, fname, mobileno, mtype, sactive);
                                clearfields();
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Farmer Updated");
                                dialog.setTitle("Success");
                                dialog.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                            }
                                        });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                            catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }
                catch (SQLException ex){
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
        });
        if(getArguments()==null){

        }else {
            sno.setText(getArguments().getString("slno"));
            mcode.setText(getArguments().getString("code"));
            mfname.setText(getArguments().getString("name"));
            mmobiLeno.setText(getArguments().getString("mno"));
            if(getArguments().getString("mtype").equals("Cow"))
            {
                s.setSelection(0);
            }
            else if(getArguments().getString("mtype").equals("Buff")){
                s.setSelection(1);
            }
            else {
                s.setSelection(2);
            }
            mactive.setChecked(!getArguments().getString("active").equals("0"));
            save.setText("Update");
        }
        return v;
    }

    private void clearfields() {
        mcode.setText("");
        mfname.setText("");
        //mlname.setText("");
        mmobiLeno.setText("");
    }

    public void load(){
        if(mactive.isChecked()){
            sactive="1";
        }
        else {
            sactive="0";
        }
    }
    public mfarmers(){

    }
}
