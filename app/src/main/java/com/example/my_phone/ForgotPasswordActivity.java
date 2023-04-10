package com.example.my_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText mail;
    Button reset;
    FirebaseAuth auth;
    ImageView go_to_settings;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        reset =  findViewById(R.id.reset_password);
//        go_to_settings = findViewById(R.id.go_to_settings);

        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

//        go_to_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.forgot_password,new SettingFragment()).commit();
//
////                startActivity(new Intent(ForgotPasswordActivity.this,SettingsActivity.class));
//            }
//        });
    }
    private void validate(){

        mail = findViewById(R.id.user_email);
        String email = mail.getText().toString();

        String validateEmail = "([A-Za-z0-9]+[.-_])*[A-Za-z0-9]+@[A-Za-z0-9-]+(\\.[A-Z|a-z]{2,})+";
        if(!email.matches(validateEmail))
        {
            mail.setError("Enter proper email");
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        else
        {
           forget_password();
        }


    }
    private void forget_password()
    {
//        mail = findViewById(R.id.reset_password_email);
        String email = mail.getText().toString();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your mail", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to reset your password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}