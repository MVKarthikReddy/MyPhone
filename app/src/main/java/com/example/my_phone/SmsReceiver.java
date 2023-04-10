package com.example.my_helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SmsReceiver extends BroadcastReceiver {

    // private Context context;
    SmsMessage[] msgs = null;
    String msg_From,msgBody,messages[],code = "";

    // For location purpose
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    // For ringing phone
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    // For retrieving data from database
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView t;

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "message", Toast.LENGTH_SHORT).show();
//        Bundle b = intent.getExtras();
//        Object[] sms = (Object[]) b.get("pdus");
//        for(Object obj : sms)
//        {
//            SmsMessage mess = SmsMessage.createFromPdu((byte[])obj);
//            String mob = mess.getDisplayOriginatingAddress();
//            Log.d("mobile No : ",mob);
//        }
//    }

    //
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.PREF_NAME,0);
        boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);
        Log.d("LOGGED IN : ",hasLoggedIn+"");
            mediaPlayer = MediaPlayer.create(context,R.raw.perfect);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Users");
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Digi_Code_From_DB : ",code);
                    code = snapshot.child("digi_code").getValue(String.class);
                    Log.d("Digi_Code_From_DB  ",code);

                    // To Receive SMS
                    if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
                    {
                        Toast.makeText(context, "message", Toast.LENGTH_SHORT).show();
                        Bundle b = intent.getExtras();
                        Bundle bundle = intent.getExtras();

                        if(b!=null)
                        {
                            try {
//                    Toast.makeText(context, "message", Toast.LENGTH_SHORT).show();
                                Object[] pdus = (Object[]) bundle.get("pdus");
                                msgs = new SmsMessage[pdus.length];
                                for (int i = 0; i < msgs.length; i++) {
                                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                    msg_From = msgs[i].getOriginatingAddress();
                                    msgBody = msgs[i].getMessageBody();
                                    Log.d("Msg Details","Phone no: "+msg_From+"\nmsg : "+msgBody);

                                    // To send sms
//                        SmsManager sms = SmsManager.getDefault();
//                        sms.sendTextMessage(msg_From,null,"hai",null,null);
                                    //Toast.makeText(context, "sms sent successfully", Toast.LENGTH_SHORT).show();

                                }

                                messages = msgBody.split(" ",4);
                                int msgLength = messages.length;

//                    Toast.makeText(context, "messages : "+messages[0], Toast.LENGTH_SHORT).show();
                                if(msgLength==1 && messages[0].equalsIgnoreCase("Myphone")) {


                                    String text="For contacts : " +
                                            "Myphone <4 digit code> getContact <contactName> \n \n" +
                                            "For Location : " +
                                            "Myphone <4 digit code> getLocation \n \n" +
                                            "For Ring Mobile : " +
                                            "Myphone <4 digit code> RingPhone \n \n" +
                                            "For Lock Mobile : " +
                                            "Myphone <4 digit code> lockPhone\n\n"+
                                            "To change 4 digit code : " +
                                            "Myphone <4 digit code> changeCode\n";
                                    sendMultiMessage(msg_From, text);

//                        SmsManager sms = SmsManager.getDefault();
//                        sms.sendTextMessage(msg_From,null,text,null,null);
//                        Toast.makeText(context, "messages : "+messages[0], Toast.LENGTH_SHORT).show();

                                }
                                else if(msgLength>2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("getcontact"))
                                {
                                    Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE},"DISPLAY_NAME = '" + messages[3] + "'",null,null);
                                    cursor.moveToFirst();
                                    String text = cursor.getString(0);
                                    sendMessage( msg_From,messages[3]+" : "+text);

                                }
                                else if(msgLength>=2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("getlocation"))
                                {
//                                    Toast.makeText(context, "In location method", Toast.LENGTH_SHORT).show();



//                            Toast.makeText(context, "In location method", Toast.LENGTH_SHORT).show();

                                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                        return;
                                    }
                                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {

                                            String longitude="",latitude="";
//                                            Toast.makeText(context, "In location method", Toast.LENGTH_SHORT).show();

                                            if(location != null)
                                            {
                                                latitude = Double.toString(location.getLatitude());
                                                longitude = Double.toString(location.getLongitude());
                                            }
//                                    set_location.setText("longitude  : "+longitude+"\n"+"latitude : "+latitude);
                                            Log.d("Location","longitude  : "+longitude+"\n"+"latitude : "+latitude);
                                            String text = "https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude ;
                                            sendMessage(msg_From,text);


                                        }
                                    });


//                        Log.d("Location","longitude  : "+longitude+"\n"+"latitude : "+latitude);



                                }
                                else if(msgLength>=2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("ringphone"))
                                {
//                                    Toast.makeText(context, "ringing", Toast.LENGTH_SHORT).show();
                                    audioManager = (AudioManager) context.getSystemService(context.getApplicationContext().AUDIO_SERVICE);
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    Toast.makeText(context, "after ringing", Toast.LENGTH_SHORT).show();
                                    mediaPlayer.start();
                                    sendMessage(msg_From,"ringing");

                                }
                                else if(msgLength>=2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("stopringing"))
                                {
                                    mediaPlayer.stop();
                                }
                                else if(msgLength>=2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("lockScreen"))
                                {
                                    DevicePolicyManager d = (DevicePolicyManager)context.getSystemService(context.DEVICE_POLICY_SERVICE);
                                    d.lockNow();
                                    sendMessage(msg_From,"successfully locked the screen");
                                }
                                else if(msgLength>=2 && messages[0].equalsIgnoreCase("myphone") && messages[1].equals(code) && messages[2].equalsIgnoreCase("changecode"))
                                {
                                    String digi_code = generate_Code(4);
                                    String instruction = "Dear user,\nYour 4 digit code is : "+digi_code+" \nAlways remember this code";
                                    String Phone_num = snapshot.child("phone number").getValue(String.class);
//                        Log.d("Phone_Num : ",Phone_num);

                                    //adding generated code to hashmap

                                    HashMap<String,Object> hashMap = new HashMap<String,Object>();
                                    hashMap.put("digi_code",digi_code);

                                    // updating 4 digit code
                                    databaseReference.child(user.getUid()).updateChildren(hashMap);

                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(Phone_num,null,instruction,null,null);
                                    smsManager.sendTextMessage(msg_From,null,"your 4 digit code is sent to your registered mobile number",null,null);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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


    public void sendMultiMessage(String number,String text) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts=smsManager.divideMessage(text);
            smsManager.sendMultipartTextMessage(number, null, parts, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg_From,String text)
    {
//        Toast.makeText(context, "messages : "+messages[0], Toast.LENGTH_SHORT).show();

        try{
            Log.d("Msg",msg_From);
//            Toast.makeText(context, "in sms sender", Toast.LENGTH_SHORT).show();
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(msg_From,null,text,null,null);
//            Toast.makeText(context, "messages : "+messages[0], Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}