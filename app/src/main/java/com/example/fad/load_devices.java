package com.example.fad;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class load_devices extends AppCompatActivity {
    LinearLayout layoutprinter,layoutanalyzer,layoutwscale;
    TextView printer,analyzer,wscale;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String prntr;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_devices);
        layoutprinter=(LinearLayout) findViewById(R.id.layoutprinter);
        layoutanalyzer=(LinearLayout) findViewById(R.id.layoutanalyzer);
        layoutwscale=(LinearLayout) findViewById(R.id.layoutwscale);
        printer=(TextView) findViewById(R.id.textprinter);
        analyzer=(TextView) findViewById(R.id.textanalyzer);
        wscale=(TextView) findViewById(R.id.textwscale);
        layoutanalyzer.setVisibility(View.INVISIBLE);
        layoutwscale.setVisibility(View.INVISIBLE);
        layoutprinter.setVisibility(View.INVISIBLE);
        next=(Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(load_devices.this, Home.class);
                startActivity(intent);
            }
        });
        try {
            FindBluetoothDevice();
            openBluetoothPrinter();
            FindBluetoothDevice();
            openBluetoothPrinter();
            FindBluetoothDevice();
            openBluetoothPrinter();
        }
        catch (Exception ex)
        {
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
    void FindBluetoothDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(load_devices.this,"No Bluetooth Adapter found",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(load_devices.this,"Bluetooth Printer Attached: "+pairedDev.getName(),Toast.LENGTH_LONG).show();
                        layoutprinter.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
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
                                                Toast.makeText(load_devices.this,data,Toast.LENGTH_LONG).show();
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
}
