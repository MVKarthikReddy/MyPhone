package com.example.my_helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ModelClass> itemList;
    Adapter adapter;

    Button logout;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        initData();
        initRecyclerView();

        firebaseAuth = FirebaseAuth.getInstance();

        //to logout
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                Toast.makeText(SettingsActivity.this, "successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {

        }
    }

    private void initData() {

        itemList = new ArrayList<>();

        itemList.add(new ModelClass(R.drawable.ic_baseline_share_24,"Share1"));

        itemList.add(new ModelClass(R.drawable.ic_baseline_share_24,"Share2"));

        itemList.add(new ModelClass(R.drawable.ic_baseline_share_24,"Share3"));

    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}