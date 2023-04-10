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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {




    public static String PREF_NAME = "Myprefsfile";

    TextView forgotPassword,createAccount;
    Button login;
    EditText user_password,user_email;
    ProgressDialog progressDialog;


    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String device_ID,device_ID_from_DB = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);
        login = findViewById(R.id.reset_password);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i1);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i2);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_app();
            }
        });
    }
    @SuppressLint("LongLogTag")
    private void login_app()
    {
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();

        String validateEmail = "([A-Za-z0-9]+[.-_])*[A-Za-z0-9]+@[A-Za-z0-9-]+(\\.[A-Z|a-z]{2,})+";

        Log.d("User Details","email : "+email+"\npassword : "+password);

        if(!email.matches(validateEmail) || email.isEmpty())
        {
            user_email.setError("Enter proper email");
//            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty() || password.length()<6)
        {
            user_password.setError("Enter proper password");
        }
        else
        {
            Log.d("TAG","email : "+email+"\npassword : "+password);

            progressDialog.setTitle("Login");
            progressDialog.setMessage("please wait while logging in...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            // Retrieving device_ID from database

            device_ID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

            Log.d("Device_ID_from_device : ",device_ID);
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Users");
                        FirebaseUser user = auth.getCurrentUser();
                        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                device_ID_from_DB = snapshot.child("device_ID").getValue(String.class);
                                Log.d("Device_ID_in_mainActivity: ",device_ID);
                                Log.d("Device_ID","Device_ID_from_DB: "+device_ID_from_DB);

                                boolean b = (device_ID.equals(device_ID_from_DB));
                                Log.d("isEqual",b+"");


                                if(b)
                                {
                                    Log.d("TAG","email : "+email+"\npassword : "+password);

                                    if(user.isEmailVerified())
                                    {
                                        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREF_NAME,0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("hasLoggedIn",true);
                                        editor.commit();

                                        nextActivity();
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "please verify your mail", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "you are a fake user", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
//                                Toast.makeText(LoginActivity.this, "error in login activity", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Enter proper credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void nextActivity(){
        progressDialog.dismiss();
//        Toast.makeText(this, "in next activity", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);



    }

}