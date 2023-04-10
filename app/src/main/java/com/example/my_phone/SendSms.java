package com.example.my_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendSms extends AppCompatActivity {

    String phoneNum;
    String msg;
    TextView e1,e2;
//    SmsReceiver receiver = new SmsReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            super.onReceive(context, intent);
////            e1.setText(msg_From);
////            e2.setText(msgBody);
//
//             Intent i = new Intent(SendSms.this,HomeActivity.class);
//             startActivity(i);
//
//
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(receiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiver);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        e1 = (TextView) findViewById(R.id.phone);
        e2 = (TextView) findViewById(R.id.msg);

//        e1.setText(phoneNum);
//        e2.setText(msg);
    }
}