package com.example.my_helper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView gen_text,gen_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        gen_code = v.findViewById(R.id.gen_code);
        gen_text = v.findViewById(R.id.gen_text);

        gen_text.setText("Dear user,\n\n 1. First you have to generate a 4 digit code \n\n 2. This 4 digit code will help you in the time of getting access \n\n 3. This is very confidential and don't share this to anyone.");

        gen_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new SettingFragment()).commit();
                gen_code.setBackgroundColor(getResources().getColor(R.color.white));

                String digi_code = generate_Code(4);
                String instruction = "Dear user,\nYour 4 digit code is : "+digi_code+" \nDon't share this to anyone.";
                Log.d("4_digi_code",digi_code);
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage("+919182305651",null,instruction,null,null);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                FirebaseAuth auth = FirebaseAuth.getInstance();
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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        return v;
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