package com.example.fad;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fad.DataHelpers.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class settings extends Fragment {
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
   LinearLayout printer,analyser,wscale;
   TextView lblprinter;
   Button connect,print,disconnect;
    DatabaseHelper msettingsHelper;

    SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        printer = (LinearLayout) v.findViewById(R.id.printer);
        analyser = (LinearLayout) v.findViewById(R.id.analyzer);
        wscale = (LinearLayout) v.findViewById(R.id.wscale);
        readmessage();
        lblprinter = (TextView) v.findViewById(R.id.lblprinter);
        connect = (Button) v.findViewById(R.id.connect);
        print = (Button) v.findViewById(R.id.print);
        disconnect = (Button) v.findViewById(R.id.disconnect);
        String[] arraySpinner = new String[]{
                "None", "Messages", "Whatsapp"
        };
        Spinner s = (Spinner) v.findViewById(R.id.spinnermessage);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        if(readmessage()==0){
            int spinnerPosition = adapter.getPosition("None");
            s.setSelection(spinnerPosition);
        }
        else if(readmessage()==1)
        {
            int spinnerPosition = adapter.getPosition("Messages");
            s.setSelection(spinnerPosition);
        }
        else if(readmessage()==2)
        {
            int spinnerPosition = adapter.getPosition("Whatsapp");
            s.setSelection(spinnerPosition);
        }
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savemessage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent bluetoothPicker = new Intent("android.bluetooth.devicepicker.action.LAUNCH");
                    startActivity(bluetoothPicker);
                    /*Intent intentOpenBluetoothSettings = new Intent();
                    intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intentOpenBluetoothSettings);*/
                    //FindBluetoothDevice();
                    //openBluetoothPrinter();
                   /* db = msettingsHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select value from settings where name='sms'", null);
                    if (cursor == null) {
                        String bcode = "1";
                        String name = "sms";
                        String value = s.getSelectedItem().toString();
                        String active = "1";
                        Boolean addsettings = msettingsHelper.addsettings(bcode, name, value, active);
                    }
                    else {
                        String slno = settingsdata
                        String bcode = "1";
                        String name = "sms";
                        String value = s.getSelectedItem().toString();
                        String active = "1";
                        Boolean updatesettings = msettingsHelper.updatesettings(slno,bcode, name, value, active);*/
                    //}
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        analyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Connect Analyzer Bluetooth");
                dialog.setTitle("Connect");
                dialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent bluetoothPicker = new Intent("android.bluetooth.devicepicker.action.LAUNCH");
                                startActivity(bluetoothPicker);
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

            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    disconnectBT();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /*db = msettingsHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select value from settings where name='sms'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                if (cursor.getString(0).equals("None")) {
                    s.setSelection(0);
                } else if (cursor.getString(0).equals("Messages")) {
                    s.setSelection(1);
                } else {
                    s.setSelection(2);
                }
            }
        }*/
        return v;
    }
    void savemessage(int message) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("fad", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("message", message);
        editor.apply();
    }

    int readmessage() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("fad", Context.MODE_PRIVATE);
        return sharedPref.getInt("message",0);
    }
    void FindBluetoothDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                lblprinter.setText("No Bluetooth Adapter found");
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
                        lblprinter.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
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
                                                lblprinter.setText(data);
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
    void printData() throws  IOException{
        try{
            String msg = "***Test Print Success***";
            msg+="\n";
            msg+="pinting in next line";
            msg+="\n";
            outputStream.write(msg.getBytes());
            lblprinter.setText("Printing Test...");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblprinter.setText("Printer Disconnected.");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
