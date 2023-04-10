package com.example.my_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PermissionsActivity extends AppCompatActivity {

    Button permission_for_sms,permission_for_contacts,permission_for_location,permission_for_lock;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        permission_for_sms = findViewById(R.id.permissions_for_sms);
        permission_for_contacts = findViewById(R.id.perissions_for_contacts);
        permission_for_location = findViewById(R.id.permission_for_location);
        permission_for_lock = findViewById(R.id.permisson_for_lock);
        permission_for_sms.setBackgroundColor(getResources().getColor(R.color.red));
        permission_for_location.setBackgroundColor(getResources().getColor(R.color.red));
        permission_for_lock.setBackgroundColor(getResources().getColor(R.color.red));
        permission_for_contacts.setBackgroundColor(getResources().getColor(R.color.red));

//        permission_for_sms.setBackgroundColor(0x000000);

        // For Device Admin Permissions
        devicePolicyManager = (DevicePolicyManager)getSystemService(this.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, ScreenLock.class);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
        {
            permission_for_sms.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }
        else
        {
            permission_for_sms.setBackgroundColor(getResources().getColor(R.color.white));

        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED)
        {
            permission_for_contacts.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }
        else
        {
            permission_for_contacts.setBackgroundColor(getResources().getColor(R.color.white));

        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            permission_for_location.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }
        else
        {
            permission_for_location.setBackgroundColor(getResources().getColor(R.color.white));

        }

        permission_for_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_sms_permission();
//                 Toast.makeText(PermissionsActivity.this, "wait while sreuesting... ", Toast.LENGTH_SHORT).show();
            }
        });

        permission_for_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request_contacts_permission();
            }
        });

        permission_for_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request_location_permission();
            }
        });

        permission_for_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request_admin_permission();

            }
        });
    }

    public void request_sms_permission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_SMS))
            {

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS},1);

            }
        }
        else
        {
            permission_for_sms.setBackgroundColor(getResources().getColor(R.color.light_blue));
        }
    }

    public void request_contacts_permission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED )
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,(Manifest.permission.READ_CONTACTS)))
            {

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},2);
                //Toast.makeText(this, "it is working", Toast.LENGTH_SHORT).show();


            }
        }
        else
        {
            permission_for_contacts.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }
    }
    public void request_location_permission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3);
                //Toast.makeText(this, "it is working", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            permission_for_location.setBackgroundColor(getResources().getColor(R.color.light_blue));

        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        boolean isActive = devicePolicyManager.isAdminActive(compName);
//        permission_for_lock.setVisibility(isActive ? View.GONE : View.VISIBLE);
//    }

    public void request_admin_permission()
    {

        if (!devicePolicyManager.isAdminActive(compName)) {
            // try to become active
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
            startActivityForResult(intent, 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(0 == requestCode)
        {
            if(requestCode == Activity.RESULT_OK)
            {
                Toast.makeText(this, "admin permission is given", Toast.LENGTH_SHORT).show();
                permission_for_lock.setBackgroundColor(getResources().getColor(R.color.light_blue));

                // done with activate to Device Admin
            }
            else
            {
                // cancle it.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "sms permissions granted!", Toast.LENGTH_SHORT).show();
                    permission_for_sms.setBackgroundColor(getResources().getColor(R.color.light_blue));

//            Log.d(grantResults);
                    //startActivity(new Intent(MainActivity.this,get_Location.class));
                } else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();

                }
                break;

            case 2:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "contact's permissions granted!", Toast.LENGTH_SHORT).show();
                    permission_for_contacts.setBackgroundColor(getResources().getColor(R.color.light_blue));

//            Log.d(grantResults);
                    //startActivity(new Intent(MainActivity.this,get_Location.class));
                } else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "location permissions granted!", Toast.LENGTH_SHORT).show();
                    permission_for_location.setBackgroundColor(getResources().getColor(R.color.light_blue));

//            Log.d(grantResults);
                    //startActivity(new Intent(MainActivity.this,get_Location.class));
                } else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}