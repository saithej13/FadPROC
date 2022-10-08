package com.example.fad.milkcollection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.fad.DataHelpers.DatabaseHelper;
import com.example.fad.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class milkcollection extends Fragment {
    ImageView shiftm, shifte;
    LinearLayout layoutdate;
    TextView textdate, fname, rrate, ramount, txtdate, subtotal, avgfat, avgsnf, totalamt, sno, mno;
    EditText fcode, qty, rfat, rsnf;
    private int mdate, mmonth, myear;
    Button save;
    public static String prnt;
    LinearLayout layoutshiftm, layoutshifte, layoutmtypeb, layoutmtypec;
    SQLiteDatabase db;
    MenuItem fav;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private PrintManager mPrintManager;
    private List<PrintJob> mPrintJobs;
    private PrintJob mCurrentPrintJob;
    private File pdfFile;
    private String externalStorageDirectory;
    private final Handler mPrintStartHandler = new Handler();
    private final Handler mPrintCompleteHandler = new Handler();
    Context mcontext;
    private String connectionInfo;
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final Boolean forceCLaim = true;

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;
    DatabaseHelper mDatabaseHelper;
    ArrayList<milkdata> arrayList;
    ArrayAdapter arrayAdapter;
    listmilkdata myAdapter;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static String dshift;
    public static String dmtype;
    public static Date ddate;
    public static String lvprmt;
    SwipeMenuListView lvdata;
    boolean dmtypelock;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_milkcollection, container, false);
        /*mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        mDeviceList = mUsbManager.getDeviceList();
        mDeviceIterator = mDeviceList.values().iterator();


        Toast.makeText(getContext(), "Device List Size: " + String.valueOf(mDeviceList.size()), Toast.LENGTH_SHORT).show();
        TextView textView = (TextView) v.findViewById(R.id.usbDevice);
        String usbDevice = "";
        //This is just testing what devices are connected
        while (mDeviceIterator.hasNext()) {
            UsbDevice usbDevice1 = mDeviceIterator.next();
            usbDevice += "\n" +
                    "DeviceID: " + usbDevice1.getDeviceId() + "\n" +
                    "DeviceName: " + usbDevice1.getDeviceName() + "\n" +
                    "DeviceClass: " + usbDevice1.getDeviceClass() + " - " + translateDeviceClass(usbDevice1.getDeviceClass()) + "\n" +
                    "DeviceSubClass: " + usbDevice1.getDeviceSubclass() + "\n" +
                    "VendorID: " + usbDevice1.getVendorId() + "\n" +
                    "ProductID: " + usbDevice1.getProductId() + "\n";

            int interfaceCount = usbDevice1.getInterfaceCount();
            Toast.makeText(getContext(), "INTERFACE COUNT: " + String.valueOf(interfaceCount), Toast.LENGTH_SHORT).show();

            mDevice = usbDevice1;

            if (mDevice == null) {
                Toast.makeText(getContext(), "mDevice is null", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "mDevice is not null", Toast.LENGTH_SHORT).show();
            }
            textView.setText(usbDevice);
        }

        if (mDevice == null) {
            Toast.makeText(getContext(), "mDevice is null", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "mDevice is not null", Toast.LENGTH_SHORT).show();
        }

        mPermissionIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        requireContext().registerReceiver(mUsbReceiver, filter);
        mUsbManager.requestPermission(mDevice, mPermissionIntent);*/
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_circle);
        //scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        this.setHasOptionsMenu(true);
        mno = (TextView) v.findViewById(R.id.mno);
        sno = (TextView) v.findViewById(R.id.usbDevice);
        shifte = (ImageView) v.findViewById(R.id.shifte);
        layoutshiftm = (LinearLayout) v.findViewById(R.id.layoutshiftm);
        layoutshifte = (LinearLayout) v.findViewById(R.id.layoutshifte);
        layoutmtypeb = (LinearLayout) v.findViewById(R.id.layoutmtypeb);
        layoutmtypec = (LinearLayout) v.findViewById(R.id.layoutmtypec);
        layoutdate = (LinearLayout) v.findViewById(R.id.layoutdate);
        textdate = (TextView) v.findViewById(R.id.textdate);
        txtdate = (TextView) v.findViewById(R.id.txtdate);
        fcode = (EditText) v.findViewById(R.id.farmerid);
        qty = (EditText) v.findViewById(R.id.mqty);
        rfat = (EditText) v.findViewById(R.id.mfat);
        rsnf = (EditText) v.findViewById(R.id.msnf);
        fname = (TextView) v.findViewById(R.id.fname);
        rrate = (TextView) v.findViewById(R.id.rate);
        ramount = (TextView) v.findViewById(R.id.amt);
        subtotal = (TextView) v.findViewById(R.id.subtotal);
        avgfat = (TextView) v.findViewById(R.id.avgfat);
        avgsnf = (TextView) v.findViewById(R.id.avgsnf);
        totalamt = (TextView) v.findViewById(R.id.totalamt);
        lvdata = (SwipeMenuListView) v.findViewById(R.id.listview);
        save = (Button) v.findViewById(R.id.save);
        arrayList = new ArrayList<>();
        setHasOptionsMenu(true);
        mDatabaseHelper = new DatabaseHelper(getContext());
        load();
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
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
        lvdata.setMenuCreator(creator);
        lvdata.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        milkdata edata = arrayList.get(position);
                        String eid = edata.getSlno();
                        String edate = String.valueOf(edata.getDate());
                        String eshift = edata.getShift();
                        String emtype = edata.getMtype();
                        String ecode = edata.getCode();
                        String ename = edata.getName();
                        String equantity = edata.getQuantity();
                        String efat = edata.getFat();
                        String esnf = edata.getSnf();
                        String erate = edata.getRate();
                        String eamount = edata.getAmount();
                        sno.setText(eid);
                        txtdate.setText(edate);
                        fcode.setText(ecode);
                        fname.setText(ename);
                        qty.setText(equantity);
                        rfat.setText(efat);
                        rsnf.setText(esnf);
                        rrate.setText(erate);
                        ramount.setText(eamount);
                        save.setText("Update");
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
                                        milkdata mdata = arrayList.get(position);
                                        String id = mdata.getSlno();
                                        String code = mdata.getCode();
                                        mDatabaseHelper.deletemilkrecord(id, code);
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
                        //Log.d(TAG, "onMenuItemClick: Item was Clicked"+rate);
                        break;
                }
                return false;
            }
        });
        rsnf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getrate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                getrate();
            }
        });
        rfat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rsnf.setEnabled(true);
                if (rsnf.getText().equals("Snf") || rsnf.getText().equals("")) {

                } else {
                    getrate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (qty.getText().toString().isEmpty()||qty.getText().toString().equals("")){

                }
            else {
                rfat.setEnabled(true);
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                    qty.setEnabled(false);
                    rfat.setEnabled(false);
                    rsnf.setEnabled(false);
                } else {
                    qty.setEnabled(true);
                    try {
                        db = mDatabaseHelper.getReadableDatabase();
                        Cursor cursor = db.rawQuery("select fname,mobileno,mtype from farmerdata where active='1' and code=" + fcode.getText().toString(), null);
                        if (cursor != null) {
                            if (cursor.getCount() > 0) {
                                cursor.moveToNext();
                                fname.setText(cursor.getString(0));
                                mno.setText(cursor.getString(1));
                                if(cursor.getString(2).equals("Cow")){
                                    dmtypelock=true;
                                    mtypec();
                                }
                                else if(cursor.getString(2).equals("Buff")){
                                    dmtypelock=true;
                                    mtypeb();
                                }
                                else if(cursor.getString(2).equals("Both")){
                                    dmtypelock=false;
                                }
                            } else {
                                fname.setText("");
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
                    } catch (SQLException ex) {
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    char tare = (char) 84;
                    outputStream.write(tare);
                    outputStream.flush();
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                if (fcode.getText().toString().isEmpty() || qty.getText().toString().isEmpty() || rfat.getText().toString().isEmpty() || rsnf.getText().toString().isEmpty() || rrate.getText().toString().isEmpty()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage("unable to save/update, Please check all the fields");
                    dialog.setTitle("Error");
                    dialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                } else {
                    if (sno.getText().equals("slno")) {
                        try {
                            getrate();
                            String code = fcode.getText().toString();
                            String name = fname.getText().toString();
                            String dat=textdate.getText().toString();
                            long date=new SimpleDateFormat("dd/MM/yyyy").parse(dat).getTime();
                            java.sql.Date tdate=new java.sql.Date(date);
                            String shift = dshift;
                            String mtype = dmtype;
                            String quantity = qty.getText().toString();
                            String fat = rfat.getText().toString();
                            String snf = rsnf.getText().toString();
                            String rate = rrate.getText().toString();
                            String amount = ramount.getText().toString();
                            Boolean addData = mDatabaseHelper.addData(code, name, tdate, shift, mtype, quantity, fat, snf, rate, amount);
                            if(addData) {
                                refreshlistview();
                                String smsvalue = "Date:" + date + ", Shift:" + shift + " \n Farmer Code:" + code + " \n Name:" + name + " \n Milk Type:" + mtype + " \n Qty:" + quantity + " \n Fat:" + fat + " \n Snf:" + snf + " \n Rate:" + rate + " \n Amount:" + amount + "";
                                clearfields();
                                prnt = " Date:" + tdate + ", Shift:" + shift + " \n Farmer Code:" + code + " \n Name:" + name + " \n " + mtype + " Milk \n Qty:" + quantity + " \n Fat:" + fat + " \n Snf:" + snf + " \n Rate:" + rate + " \n Amount:" + amount + "\n \n \n \n";
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                                dialog.setMessage("Would you like to Print?");
                                dialog.setTitle("Print");
                                dialog.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                try {
                                                    printData();
                                                } catch (IOException ex) {
                                                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                                }
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
                                if(readmessage()==1) {
                                    final PackageManager packageManager = getContext().getPackageManager();
                                    Intent eventIntentMessage = packageManager
                                            .getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(getContext()));
                                    Uri sms_uri = Uri.parse("smsto:" + mno.getText() + "");
                                    eventIntentMessage = new Intent(Intent.ACTION_SENDTO, sms_uri);
                                    eventIntentMessage.putExtra("sms_body", smsvalue);
                                    //eventIntentMessage.setAction(Intent.ACTION_SEND);
                                    startActivity(eventIntentMessage);
                                }
                                else if(readmessage()==2)
                                {
                                    boolean installed = appInstalledorNot("com.whatsapp");
                                    if (installed) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mno.getText() + "&text=" + smsvalue));
                                        startActivity(intent);
                                    }
                                }
                            }
                            if (addData == true) {
                                Snackbar.make(v, "Data was Inserted", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(v, "ERROR!", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (SQLException | ParseException ex) {
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
                    } else {
                        try {
                            getrate();
                            String slno = sno.getText().toString();
                            String code = fcode.getText().toString();
                            String name = fname.getText().toString();
                            String dat=textdate.getText().toString();
                            long date=new SimpleDateFormat("dd/MM/yyyy").parse(dat).getTime();
                            java.sql.Date tdate=new java.sql.Date(date);
                            String shift = dshift;
                            String mtype = dmtype;
                            String quantity = qty.getText().toString();
                            String fat = rfat.getText().toString();
                            String snf = rsnf.getText().toString();
                            String rate = rrate.getText().toString();
                            String amount = ramount.getText().toString();
                            Boolean updateData = mDatabaseHelper.updateData(slno, code, name, tdate, shift, mtype, quantity, fat, snf, rate, amount);
                            refreshlistview();
                            sno.setText("slno");
                            save.setText("save");
                            clearfields();
                            if (updateData == true) {
                                Snackbar.make(v, "Data was Updated", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(v, "ERROR!", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (SQLException | ParseException ex) {
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
        layoutdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                mdate = cal.get(Calendar.DATE);
                mmonth = cal.get(Calendar.MONTH);
                myear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int monthOfYear, int month, int date) {
                        month = month + 1;
                        textdate.setText(date + "/" + month + "/" + monthOfYear);
                        txtdate.setText(date + "-" + month + "-" + monthOfYear);
                        refreshlistview();
                    }
                }, myear, mmonth, mdate);
                datePickerDialog.show();
            }
        });

        layoutshiftm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutshiftm.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutshifte.setBackground(null);
                layoutshifte.setSelected(false);
                layoutshiftm.setSelected(true);
                dshift = "M";
                refreshlistview();
            }
        });
        shifte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutshifte.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutshiftm.setBackground(null);
                layoutshiftm.setSelected(false);
                layoutshifte.setSelected(true);
                dshift = "E";
                refreshlistview();
            }
        });
        layoutmtypec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dmtypelock){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage("Cannot change Milk type");
                    dialog.setTitle("Mtype Error");
                    dialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                else {
                    mtypec();
                }
            }
        });
        layoutmtypeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dmtypelock){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage("Cannot change Milk type");
                    dialog.setTitle("Mtype Error");
                    dialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                else {
                    mtypeb();
                }
            }
        });
        return v;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            mInterface = device.getInterface(0);
                            mEndPoint = mInterface.getEndpoint(0);
                            mConnection = mUsbManager.openDevice(device);
                            //setup();
                        }
                    } else {
                        //Log.d("SUB", "permission denied for device " + device);
                        Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
    private void mtypec(){
        layoutmtypec.setBackground(getResources().getDrawable(R.drawable.layout_border));
        layoutmtypeb.setBackground(null);
        layoutmtypeb.setSelected(false);
        layoutmtypec.setSelected(true);
        dmtype = "Cow";
    }
    int readmessage() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("fad", Context.MODE_PRIVATE);
        return sharedPref.getInt("message",0);
    }
    private void mtypeb(){
        layoutmtypeb.setBackground(getResources().getDrawable(R.drawable.layout_border));
        layoutmtypec.setBackground(null);
        layoutmtypec.setSelected(false);
        layoutmtypeb.setSelected(true);
        dmtype = "Buff";
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try {
            fav = menu.add("add");
            fav.setIcon(R.drawable.ic_print);
            fav.setShowAsAction(1);
            fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        printlvdata(1);
                        //fav.setIcon(R.drawable.printer_not_connected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

            });

            /*if (bluetoothDevice.getName().equals("BlueTooth Printer")) {
                fav = menu.add("add");
                fav.setIcon(R.drawable.ic_print);
                fav.setShowAsAction(1);
                fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            disconnectBT();
                            fav.setIcon(R.drawable.printer_not_connected);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }

                });
            } else {
                fav = menu.add("add");
                fav.setIcon(R.drawable.printer_not_connected);
                fav.setShowAsAction(1);
                fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            FindBluetoothDevice();
                            openBluetoothPrinter();
                            fav.setIcon(R.drawable.ic_print);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }

                });
            }*/
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void disconnectBT() throws IOException {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            Toast.makeText(getContext(), "Printer Disconnected.", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean appInstalledorNot(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        boolean appInstalled;
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    private void print(UsbDeviceConnection connection, UsbInterface intrface) {

        String test = "THIS IS A PRINT TEST";
        byte[] testBytes = test.getBytes();

        if (intrface == null) {
            Toast.makeText(getContext(), "INTERFACE IS NULL", Toast.LENGTH_SHORT).show();
        }
        if (connection == null) {
            Toast.makeText(getContext(), "CONNECTION IS NULL", Toast.LENGTH_SHORT).show();
        }
        if (forceCLaim == null) {
            Toast.makeText(getContext(), "FORCE CLAIM IS NULL", Toast.LENGTH_SHORT).show();
        }

        connection.claimInterface(intrface, forceCLaim);
        connection.bulkTransfer(mEndPoint, testBytes, testBytes.length, 0);

    }

    private void loadtotal() {
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

    private void getrate() {
        String snf = rsnf.getText().toString();
        String fat = rfat.getText().toString();
        String quantity = qty.getText().toString();
        if (snf.isEmpty() || fat.isEmpty()) {
            //Toast.makeText(getContext(),"Fat/Snf cannot be Empty..",Toast.LENGTH_LONG).show();
        } else {
            if (dmtype.equals("Cow")) {
                try {
                    db = mDatabaseHelper.getReadableDatabase();
                    String tdat=textdate.getText().toString();
                    long ttdate=new SimpleDateFormat("dd/MM/yyyy").parse(tdat).getTime();
                    java.sql.Date tdate=new java.sql.Date(ttdate);
                    System.out.println(tdate);
                    Cursor cursor = db.rawQuery("select tsrate from milkrates where mtype='Cow' and '"+tdate+"' between fdate and tdate and " + fat + " between fatmin and fatmax and " + snf + " between snfmin and snfmax", null);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            cursor.moveToNext();
                            Double cmtsrate = cursor.getDouble(cursor.getColumnIndex("tsrate"));
                            Double dsnf = Double.parseDouble(snf);
                            Double dfat = Double.parseDouble(fat);
                            Double dqty = Double.parseDouble(quantity);
                            Double rate = ((dfat + dsnf) * cmtsrate / 100);
                            String srate = String.format("%.1f", rate);
                            String amount = String.format("%.1f", rate * dqty);
                            rrate.setText(srate);
                            ramount.setText(amount);
                        }
                    }
                    db.close();
                } catch (SQLException | ParseException ex) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage(ex.getMessage());
                    dialog.setTitle("Rate Error");
                    dialog.setPositiveButton(ex.getMessage(),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            } else {
                try {
                    db = mDatabaseHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select tsrate from milkrates where mtype='Buff' and '"+ddate+"' between fdate and tdate and " + fat + " between fatmin and fatmax and " + snf + " between snfmin and snfmax", null);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            cursor.moveToNext();
                            Double bmtsrate = Double.parseDouble(cursor.getString(0));
                            Double dsnf = Double.parseDouble(snf);
                            Double dfat = Double.parseDouble(fat);
                            Double dqty = Double.parseDouble(quantity);
                            Double rate = (((dfat) * bmtsrate / 100) - (9 - dsnf));
                            String srate = String.format("%.1f", rate);
                            String amount = String.format("%.1f", rate * dqty);
                            rrate.setText(srate);
                            ramount.setText(amount);
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setMessage("Rate Not Found");
                            dialog.setTitle("Error");
                            dialog.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            rrate.setText("");
                                            ramount.setText("");
                                        }
                                    });
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        }
                    }
                } catch (SQLException ex) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage(ex.getMessage());
                    dialog.setTitle("Rate Error");
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

    private void clearfields() {
        qty.setEnabled(false);
        rfat.setEnabled(false);
        rsnf.setEnabled(false);
        fcode.setText("");
        fname.setText("");
        qty.setText("");
        rfat.setText("");
        rsnf.setText("");
        rrate.setText("0.0");
        ramount.setText("0.0");
    }

    private void load() {
        try {
            final Calendar cal = Calendar.getInstance();
            mdate = cal.get(Calendar.DATE);
            mmonth = cal.get(Calendar.MONTH);
            myear = cal.get(Calendar.YEAR);
            int fmonth = mmonth + 1;
            Calendar C = new GregorianCalendar();
            //FindBluetoothDevice();
            //openBluetoothPrinter();
            //FindBluetoothWeighingDevice();
            //openBluetoothWeighing();
            FindBluetoothAnalyzerDevice();
            openBluetoothAnalyzer();
            int hour = C.get(Calendar.HOUR_OF_DAY);
            if (hour < 16) {
                layoutshiftm.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutshifte.setBackground(null);
                layoutshifte.setSelected(false);
                layoutshiftm.setSelected(true);
                dshift = "M";
                layoutmtypec.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutmtypeb.setBackground(null);
                layoutmtypeb.setSelected(false);
                layoutmtypec.setSelected(true);
                dmtype = "Cow";
            } else {
                layoutshifte.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutshiftm.setBackground(null);
                layoutshiftm.setSelected(false);
                layoutshifte.setSelected(true);
                dshift = "E";
                layoutmtypec.setBackground(getResources().getDrawable(R.drawable.layout_border));
                layoutmtypeb.setBackground(null);
                layoutmtypeb.setSelected(false);
                layoutmtypec.setSelected(true);
                dmtype = "Cow";
            }
            textdate.setText(mdate + "/" + fmonth + "/" + myear);
            refreshlistview();
        } catch (Exception ex) {
            Log.d(ex.getMessage(), null);
        }
    }

    void printlvdata(int spacing) throws IOException {
        try {
            System.out.println(ddate);
            System.out.println(txtdate.getText());
            System.out.println(dshift);
            String line="----------------------------------------------";
            String head="Code,Name,MType,Qauntity,Fat,Snf,Rate,Amount";
            String linehead="\n"+line+"\n"+head+"\n"+line+"\n";
            //outputStream.write(linehead.getBytes());
            System.out.println(linehead);
            //SimpleDateFormat format = new SimpleDateFormat("DD/MM/yyyy");
            int fcode = 0, qty = 0, amt = 0, ltrfat = 0, ltrsnf = 0;
            for (int i = 0; i < arrayList.size(); i++) {
                milkdata u = arrayList.get(i);
                    String code=u.getCode();
                    String name =u.getName();
                    String mtype = u.getMtype();
                    String quantity = u.getQuantity();
                    String fat = u.getFat();
                    String snf = u.getSnf();
                    String rate = u.getRate();
                    String amount = u.getAmount();
                    //String lvprmt = "Quantity "+quantity  + "Fat" + String.valueOf(fat) + "Snf" + String.valueOf(snf) + "Rate" + String.valueOf(rate) + "Amount" + String.valueOf(amount);
                    lvprmt ="\n"+code+" "+name+" "+mtype+" "+quantity  + " " + fat + " " + snf + " " + rate + " " + amount;
                    //outputStream.write(lvprmt.getBytes());
                System.out.println(lvprmt);
                qty += Double.parseDouble(u.getQuantity());
                ltrfat += (Double.parseDouble(u.getFat()) * Double.parseDouble(u.getQuantity())) / 100;
                ltrsnf += Double.parseDouble(u.getSnf()) * Double.parseDouble(u.getQuantity()) / 100;
                amt += Double.parseDouble(u.getAmount());
            }
            String tqty=String.valueOf(qty);
            String avgfat=String.valueOf((ltrfat / qty) / 100);
            String avgsnf=String.valueOf((ltrsnf / qty) / 100);
            String total =String.valueOf(amt);
            System.out.println("\n"+"Avg Fat:"+avgfat+" Avg Snf:"+avgsnf+"\n"+"Total Qty:"+tqty+"\n"+"Total Amount:"+total);
            Toast.makeText(getContext(),"Printing Text...",Toast.LENGTH_LONG).show();

        //Toast.makeText(getContext(),lvprmt.toString(),Toast.LENGTH_LONG).show();
            //DecimalFormat df2 = new DecimalFormat("#.#");
            //subtotal.setText(String.valueOf(qty));
            //System.out.println((ltrfat / qty) / 100);
            //System.out.println("%.1f",(ltrfat/qty)/100);
            //avgfat.setText(String.valueOf((ltrfat / qty) / 100));
            //avgsnf.setText(String.valueOf((ltrsnf / qty) / 100));
            //totalamt.setText(String.valueOf(amt));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void printData() throws  IOException{
        try{
            //String smsvalue="Date:"+date+", Shift:"+shift+", Code:"+code+", Name:"+name+", Milk Type:"+mtype+", Qty:"+quantity+", Fat:"+fat+", Snf:"+snf+", Rate:"+rate+", Amount:"+amount+"";
            String msg = prnt;
            outputStream.write(msg.getBytes());
            Toast.makeText(getContext(),"Printing Text...",Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void FindBluetoothDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getContext(),"No Bluetooth Adapter found",Toast.LENGTH_LONG).show();
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){
                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("BlueTooth Printer")){
                        bluetoothDevice=pairedDev;
                        //Toast.makeText(getContext(),"Bluetooth Printer Attached: "+pairedDev.getName(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    void FindBluetoothAnalyzerDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getContext(),"No Bluetooth Adapter found",Toast.LENGTH_LONG).show();
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){
                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("HC-05")){
                        bluetoothDevice=pairedDev;
                        //Toast.makeText(getContext(),"Bluetooth Printer Attached: "+pairedDev.getName(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    void FindBluetoothWeighingDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getContext(),"No Bluetooth Adapter found",Toast.LENGTH_LONG).show();
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){
                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("HC-05")){
                        bluetoothDevice=pairedDev;
                        //Toast.makeText(getContext(),"Bluetooth Printer Attached: "+pairedDev.getName(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    void openBluetoothPrinter() throws IOException {
        try{
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();
            beginListenData();
        }catch (Exception ex){
            Log.d("Error ,",ex.getMessage());
        }
    }
    void openBluetoothWeighing() throws IOException {
        try{
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();
            beginListenweighData();
        }catch (Exception ex){
            Log.d("Error ,",ex.getMessage());
        }
    }
    void openBluetoothAnalyzer() throws IOException {
        try{
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();
            //receiveAnalyzerdata();
            thread=new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
            byte[] buffer = new byte[1024];
            int len;
            len = bluetoothSocket.getInputStream().read(buffer);
            byte[] data = Arrays.copyOf(buffer, len);
            int byteAvailable = inputStream.available();
            byte[] packetByte = new byte[byteAvailable];
            inputStream.read(packetByte);
            System.out.println(data);
            System.out.println("String "+inputStream.toString());
            System.out.println("available "+bluetoothSocket.getInputStream().available());
            //GetInHexFormat(packetByte);
            //beginListenweighData();
            //beginListenAnalyzerData();

                    }catch(Exception ex){
                        stopWorker=true;
                    }
                }
            }
        });
        thread.start();
        }catch (Exception ex){
            Log.d("Error ,",ex.getMessage());
        }
    }
    void beginListenData(){
        try{
            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];
            thread=new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, StandardCharsets.US_ASCII);
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(),data,Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void GetInHexFormat(String TextValue)
    {
        try
        {
            char arr[]=TextValue.toCharArray();
            ArrayList<String> lstValues = new ArrayList<String>();
            for (char c : arr)
            {
                StringBuilder sb = new StringBuilder(String.format("{0:X}", (int)c));
                lstValues.add(sb.toString());
                System.out.println(sb.toString() + "\n");
            }
            for (int i = 0; i < 3; i++)
            {
                if (lstValues.get(i).toString().length() == 3)
                    lstValues.set(i, "0" + lstValues.get(i));
                if (lstValues.get(i).toString().length() == 2)
                    lstValues.set(i, "00" + lstValues.get(i));
                if (lstValues.get(i).toString().length() == 1)
                    lstValues.set(i, "000" + lstValues.get(i));
            }
            String snf = lstValues.get(1).toString().substring(2,2) + "." + lstValues.get(1).toString().substring(0, 2);
            String fat = lstValues.get(0).toString().substring(2, 2) + "." + lstValues.get(0).toString().substring(0, 2);
            rfat.setText(fat);
            rsnf.setText(snf);

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }

    }
    /*public void GetInHexFormat(String TextValue)
    {
        try
        {
            char arr[]=TextValue.toCharArray();
            ArrayList<String> lstValues = new ArrayList<String>();
            for (char c : arr)
            {
                StringBuilder sb = new StringBuilder(String.format("{0:X}", (int)c));
                lstValues.add(sb.toString());
                System.out.println(sb.toString() + "\n");
            }
            for (int i = 0; i < 3; i++)
            {
                if (lstValues.get(i).toString().length() == 3)
                    lstValues.set(i, "0" + lstValues.get(i));
                if (lstValues.get(i).toString().length() == 2)
                    lstValues.set(i, "00" + lstValues.get(i));
                if (lstValues.get(i).toString().length() == 1)
                    lstValues.set(i, "000" + lstValues.get(i));
            }
            String snf = lstValues.get(1).toString().substring(2,2) + "." + lstValues.get(1).toString().substring(0, 2);
            String fat = lstValues.get(0).toString().substring(2, 2) + "." + lstValues.get(0).toString().substring(0, 2);
            rfat.setText(fat);
            rsnf.setText(snf);

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }

    }*/
    void beginListenweighData(){
        try{
            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];
            thread=new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, StandardCharsets.US_ASCII);
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //code for
                                                //qty.setText(data);
                                                try {
                                                    int i = 0;
                                                    String str = "";
                                                    String strWt = data.toString();
                                                    String[] strQty = strWt.split("");
                                                    if (strQty.length < 3)
                                                        strQty = strWt.split("\n");
                                                    i = strQty.length;
                                                    if (i > 0)
                                                        str = str + strQty[i - 9].toString();
                                                            str = str + strQty[i - 6].toString();
                                                            str = str + strQty[i - 5].toString();
                                                            str = str + strQty[i - 4].toString();
                                                            str = str + strQty[i - 3].toString();
                                                            str = str + strQty[i - 2].toString();
                                                    if (str.length() >= 1)
                                                        str = str.substring(0, str.length());
                                                    qty.setText(str);
                                                    //System.out.println(data.toString().trim());
                                                }
                                                catch (Exception ex){

                                                }

                                                /*Timer timer = new Timer();
                                                TimerTask timerTask = new TimerTask() {
                                                    @Override
                                                    public void run() {

                                                    }
                                                };
                                                timer.schedule(timerTask, 1);*/
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void receiveAnalyzerdata(){
        try {
            //byte[] buffer = new byte[1024];
            //int len;
            //noinspection InfiniteLoopStatement
            /*System.out.println(data);
            fname.setText(data.toString());*/
            byte[] buffer = new byte[1024];
            int len;
            len = inputStream.read(buffer);
            byte[] data = Arrays.copyOf(buffer, len);
            System.out.println(data);
            fname.setText(inputStream.available());
            fname.setText(inputStream.read());
            int byteAvailable = inputStream.available();
            byte[] packetByte = new byte[byteAvailable];
            fname.setText(inputStream.read(packetByte));
            while (true) {
                //len = inputStream.read(buffer);
                //len=0;
                //byte[] data = Arrays.copyOf(buffer,0);
                //System.out.println(data);
                //System.out.println(inputStream.read(buffer));
                //System.out.println(data.toString());

                //System.out.println(inputStream.read());
                //System.out.println(data);



            }
        }catch (Exception ex){

        }
    }
    void beginListenAnalyzerData(){
        try{
            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];
            thread=new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            System.out.println("inpuy "+inputStream.available());
                            //fname.setText(inputStream.available());
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);
                                System.out.println(inputStream.read(packetByte));
                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    System.out.println(b);
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.out.println(encodedByte);
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, StandardCharsets.US_ASCII);
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    //System.out.println(data.getBytes(StandardCharsets.UTF_8));
                                                    //System.out.println(data.getBytes());
                                                    System.out.println(data);
                                                    fname.setText(data.length());
                                                    GetInHexFormat(data);
                                                    /*StringBuilder res = new StringBuilder(data.length()*2);
                                                    int lower4 = 0x0F; //mask used to get lowest 4 bits of a byte
                                                    for(int i=0; i<data.length(); i++) {
                                                        int higher = (data.getBytes()[i] >> 4);
                                                        int lower = (data.getBytes()[i] & lower4);
                                                        if(higher < 10) res.append((char)('0' + higher));
                                                        else            res.append((char)('A' + higher - 10));
                                                        if(lower < 10)  res.append((char)('0' + lower));
                                                        else            res.append((char)('A' + lower - 10));
                                                        res.append(' '); //remove this if you don't want spaces between bytes
                                                    }
                                                    System.out.println(res.toString());*/
                                                }
                                                catch (Exception ex){

                                                }
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    /*public synchronized void parse(byte[] bArr) {
        this.sbMessage.append(new String(bArr));
        String sb = this.sbMessage.toString();
        if (this.ignoreInitialData) {
            if (sb.length() > this.mWsParams.getIgnoreThreshold()) {
                this.ignoreInitialData = false;
                this.sbMessage = new StringBuilder();
            }
        } else if (sb.length() > 12) {
            String[] splitUsingSeperator = this.parser.splitUsingSeperator(sb, this.mWsParams.getSeparator());
            if (splitUsingSeperator.length > 4) {
                String str = splitUsingSeperator[splitUsingSeperator.length - 2];
                if (this.parser.isValidFormat(str, this.mWsParams.getPrefix(), this.mWsParams.getSuffix())) {
                    double applyDivisionFactor = applyDivisionFactor(this.parser.getWeight(str, this.mWsParams.getPrefix(), this.mWsParams.getSuffix()));
                    this.sbMessage = new StringBuilder();
                    if (this.mListener != null) {
                        this.mListener.onNewData(applyDivisionFactor);
                    }
                } else {
                    //Util.displayErrorToast("Invalid weighingScale format " + str + ", Please check the prefix and suffix", this.mContext);
                }
            }
        }
    }*/
    private void refreshlistview() {
        try {
            String tdat=textdate.getText().toString();
            long ttdate=new SimpleDateFormat("dd/MM/yyyy").parse(tdat).getTime();
            java.sql.Date tdate=new java.sql.Date(ttdate);
            ddate=tdate;
            System.out.println(tdate);
            System.out.println(ddate);
            arrayList = mDatabaseHelper.getadata();
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
}
