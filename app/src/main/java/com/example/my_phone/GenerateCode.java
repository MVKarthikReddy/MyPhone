package com.example.my_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GenerateCode extends AppCompatActivity {

    Button code_Gen;
    String digi_code;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        code_Gen = findViewById(R.id.generate_code);

        code_Gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                digi_code = generate_Code(4);
                String instruction = "Dear user,\nYour 4 digit code is : "+digi_code+" \nDon't share this to anyone.";
                Log.d("4_digi_code",digi_code);
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage("+919182305651",null,instruction,null,null);

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Users");
                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                //adding generated code to hashmap

                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                hashMap.put("digi_code",digi_code);

                // updating 4 digit code
                databaseReference.child(user.getUid()).updateChildren(hashMap);

                databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Phone_num = snapshot.child("phone number").getValue(String.class);
                        Log.d("Phone_Num : ",Phone_num);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Phone_num,null,instruction,null,null);
                        // MOVING TO SETTING FRFAGMENT
                        startActivity(new Intent(GenerateCode.this,HomeActivity.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private String generate_Code(int len)
    {
        Random rm = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i=1;i<=len;i++){
            sb.append(rm.nextInt(10));
        }

        return sb.toString();
    }

}

