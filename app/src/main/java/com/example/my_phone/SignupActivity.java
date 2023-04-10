package com.example.my_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

//    FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    TextView first_name;
    EditText email,password,firstName,lastName,confirmPassword,phone_No;
    Button signUp;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener authStateListener;

    // To get unique android id
    String device_ID;

    //sharedpreferences
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONENUM   = "phone_num";



    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        device_ID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user != null){

                }else
                {
                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                }
            }
        };


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone_No = findViewById(R.id.Phone_num);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signUp);
        first_name = findViewById(R.id.user_firstName);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Toast.makeText(SignupActivity.this, "in sign up", Toast.LENGTH_SHORT).show();
                performAuth();

                Log.d("device_Id",device_ID);
                // To store data in realtime database

            }
        });


    }
    private  void performAuth(){
        progressDialog = new ProgressDialog(this);
        String fn = firstName.getText().toString();
        String ln = lastName.getText().toString();
        String e = email.getText().toString();
//        String ph = phone_No.getText().toString();
        String p = password.getText().toString();
        String cp = confirmPassword.getText().toString();
        String validateEmail = "([A-Za-z0-9]+[.-_])*[A-Za-z0-9]+@[A-Za-z0-9-]+(\\.[A-Z|a-z]{2,})+";

        Log.d("details",fn+"\n"+e+"\n"+p+"\n"+cp);

        if(fn.isEmpty())
        {
            firstName.setError("Enter proper first name");
//            Toast.makeText(this, "Please enter valid first name", Toast.LENGTH_SHORT).show();
        }
        else if(!e.matches(validateEmail))
        {
            email.setError("Enter proper email");
//            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        else if(p.isEmpty() || p.length()<6)
        {
            password.setError("enter proper password");
//            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
        }
        else if(!p.equals(cp))
        {
            confirmPassword.setError("check password once again");
//            Toast.makeText(this, "check password once again", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.setTitle("Registration");
            progressDialog.setMessage("please wait while registration...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

            auth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user = auth.getCurrentUser();
                        progressDialog.dismiss();
//                        Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        finish();

                        // Storing user data in firebase realtime database

                        HashMap<String,String> usersData= new HashMap<String,String>();
                        usersData.put("device_ID",device_ID);
                        usersData.put("first name",firstName.getText().toString());
                        usersData.put("last name",lastName.getText().toString());
                        usersData.put("email",email.getText().toString());
                        usersData.put("phone number",phone_No.getText().toString());
                        usersData.put("password",password.getText().toString());

                        root.child(user.getUid()).setValue(usersData);

                        // storing details in sharedpreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_FIRSTNAME,firstName.getText().toString());
                        editor.putString(KEY_LASTNAME,lastName.getText().toString());
                        editor.putString(KEY_PHONENUM,phone_No.getText().toString());
                        editor.putString(KEY_EMAIL,email.getText().toString());
                        editor.commit();
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        // Re-enable button

                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Verificatioon mail sent to : "+user.getEmail(), Toast.LENGTH_SHORT).show();
                                        } else {
//                                            Log.e(TAG, "sendEmailVerification", task.getException());
                                            Toast.makeText(SignupActivity.this,
                                                    "Failed to send verification email.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                    else{
                        progressDialog.dismiss();

                        Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void nextActivity(){
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}