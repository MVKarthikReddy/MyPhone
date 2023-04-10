package com.example.my_helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    ImageView go_to_home;
    TextView first_name,last_name,u_email,u_phoneNum,user_Name;
    Button edit_profile;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    //SharedPreferences
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONENUM   = "phone_num";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
//        go_to_home = v.findViewById(R.id.go_to_home);
        first_name = v.findViewById(R.id.user_firstName);
        last_name = v.findViewById(R.id.user_lastName);
        u_email = v.findViewById(R.id.user_Email);
        u_phoneNum = v.findViewById(R.id.user_PhoneNum);
        user_Name =  v.findViewById(R.id.User_Name);
        edit_profile = v.findViewById(R.id.edit_profile);

        //Retriving user profile data via shared preferences
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(KEY_FIRSTNAME,null);
        String lastName = sharedPreferences.getString(KEY_LASTNAME,null);
        String email = sharedPreferences.getString(KEY_EMAIL,null);
        String phone_Num = sharedPreferences.getString(KEY_PHONENUM,null);

        first_name.setText(firstName);
        last_name.setText(lastName);
        u_email.setText(email);
        u_phoneNum.setText(phone_Num);
        user_Name.setText(firstName+" "+lastName);


        //editing profile
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),EditProfileActivity.class));
            }
        });

        // Retrieving profile data via firebase

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("first name").getValue(String.class);
                String lastName = snapshot.child("last name").getValue(String.class);
                String phoneNum = snapshot.child("phone number").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);

//                first_name.setText(firstName);
//                last_name.setText(lastName);
//                u_email.setText(email);
//                u_phoneNum.setText(phoneNum);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_FIRSTNAME,firstName);
                editor.putString(KEY_LASTNAME,lastName);
                editor.putString(KEY_EMAIL,email);
                editor.putString(KEY_PHONENUM,phoneNum);
                editor.commit();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        go_to_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
//            }
//        });


        return v;
    }
}