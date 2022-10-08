package com.example.fad;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fad.users.userinfo;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signup extends AppCompatActivity {
    Button signup;
    EditText centername,conform,password,agentname,mobileno,houseno,village,mandal,dist,pincode,username;
    TextView passerror;
    CheckBox accept;
    private Firebase mRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    userinfo user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        accept =(CheckBox) findViewById(R.id.chkaccept);
        signup=(Button)findViewById(R.id.signup);
        agentname=(EditText) findViewById(R.id.agentname);
        houseno =(EditText) findViewById(R.id.address);
        village=(EditText) findViewById(R.id.village);
        mandal=(EditText) findViewById(R.id.mandal);
        dist=(EditText) findViewById(R.id.dist);
        pincode=(EditText) findViewById(R.id.pincode);
        username=(EditText) findViewById(R.id.Vusername);
        mobileno=(EditText) findViewById(R.id.Mobileno);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("milkcollection-733d1-default-rtdb");
        centername =(EditText) findViewById(R.id.centername);
        conform =(EditText) findViewById(R.id.conformpassword);
        password=(EditText) findViewById(R.id.password);
        passerror=(TextView) findViewById(R.id.passerror);
        signup.setEnabled(false);
        accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked&&password.getText().toString().equals(conform.getText().toString())){
                    signup.setEnabled(true);
                }
                else {
                    Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
                    //do nothing
                }
            }
        });
        conform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().equals(conform)){
                    passerror.setVisibility(View.INVISIBLE);
                    }
                else {
                    passerror.setText("Password Not Matched");
                    passerror.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinfo.setAgentname(agentname.getText().toString());
                userinfo.setHouseno(houseno.getText().toString());
                userinfo.setVillage(village.getText().toString());
                userinfo.setMandal(mandal.getText().toString());
                userinfo.setDist(dist.getText().toString());
                userinfo.setPincode(pincode.getText().toString());
                userinfo.setUsername(username.getText().toString());
                userinfo.setMobileno(mobileno.getText().toString());
                userinfo.setCentername(centername.getText().toString());
                userinfo.setPassword(password.getText().toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.setValue(user);
                        Toast.makeText(getBaseContext(), "data added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
