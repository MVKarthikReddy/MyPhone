package com.example.my_helper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    Button log_out,close_account;
    LinearLayout change_pass,grant_perm,support_agent,about;
    ImageView go_to_home;

    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_setting, container, false);

        log_out = v.findViewById(R.id.log_out);
        close_account = v.findViewById(R.id.close_account);

        change_pass = v.findViewById(R.id.change_password);
        grant_perm = v.findViewById(R.id.grant_permisssions);
        about = v.findViewById(R.id.about);
        support_agent = v.findViewById(R.id.support_agent);
//        go_to_home = v.findViewById(R.id.go_to_home);

        firebaseAuth = FirebaseAuth.getInstance();

        //to change password
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ForgotPasswordActivity.class));
            }
        });

        //to grant permissoins
        grant_perm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),PermissionsActivity.class));
            }
        });

        //go back to home
//        go_to_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
//
//            }
//        });

        //to generate new code
//        gen_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(),GenerateCode.class));
//            }
//        });

        //to logout
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREF_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn",false);
                editor.commit();

                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });

        close_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Close Account");
                progressDialog.setMessage("Wait while we are closing your account...");
                progressDialog.show();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREF_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn",false);
                editor.commit();
                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Hello buddy", Toast.LENGTH_SHORT).show();

                                FirebaseAuth.getInstance().getCurrentUser().delete();

                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                progressDialog.dismiss();

//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful())
//                                                {
//                                                    Toast.makeText(getActivity(), "data removed", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(getActivity(),LoginActivity.class));
//                                                    progressDialog.dismiss();
//                                                }
//                                            }
//                                        });
                            }
                        });
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Log.d("Current User : ",firebaseUser.getEmail());
        }
        else
        {
            Log.d("Current User : ","null");
        }
    }
}