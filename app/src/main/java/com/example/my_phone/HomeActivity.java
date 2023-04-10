package com.example.my_helper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView nav_bar;
    String firstName="";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nav_bar = findViewById(R.id.nav_bar);
        Intent i = getIntent();
        firstName = i.getStringExtra("first_name");


        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
        nav_bar.setSelectedItemId(R.id.home);

        // Taking Permissions for different activities

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS))
            {

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.RECEIVE_SMS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION},0);
                //Toast.makeText(this, "it is working", Toast.LENGTH_SHORT).show();
            }
        }

//        Toast.makeText(this, firstName, Toast.LENGTH_SHORT).show();
        nav_bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId())
                {
                    case R.id.home:
//                        Toast.makeText(HomeActivity.this, "home", Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        // startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        break;
                    case R.id.help:
//                        Toast.makeText(HomeActivity.this, "home", Toast.LENGTH_SHORT).show();
                        fragment = new HelpFragment();
                        // startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        break;
                    case R.id.profile:
//                        Toast.makeText(HomeActivity.this, "profile", Toast.LENGTH_SHORT).show();
                        fragment = new ProfileFragment();
                        //startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                        break;
                    case R.id.settings:
//                        Toast.makeText(HomeActivity.this, "h", Toast.LENGTH_SHORT).show();
                        fragment = new SettingFragment();
//                        startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
                return true;
            }
        });

    }


}