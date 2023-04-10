package com.example.my_helper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileActivity extends AppCompatActivity {

    EditText edited_first_Name,edited_last_Name,edited_phNm;
    Button save,add_Profile;
    String fName,lName,pNum;
    ImageView profile_Photo;

    DatabaseReference databaseReference;
    FirebaseAuth auth;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_PHONENUM   = "phone_num";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edited_first_Name = findViewById(R.id.edited_first_Name);
        edited_last_Name = findViewById(R.id.edited_last_Name);
        edited_phNm  = findViewById(R.id.edited_phNm);
        save = findViewById(R.id.save_profile);
        add_Profile = findViewById(R.id.add_profile);
        profile_Photo = findViewById(R.id.profile_Photo);




        // updating profile


        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        // Use the uri to load the image
//                        Log.d("Image Uri : ",uri+"");
//                        profile_Photo.setImageURI(null);
//                        profile_Photo.setImageURI(Uri.parse(uri.toString()));
                        Toast.makeText(this, "selected an image", Toast.LENGTH_SHORT).show();
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                        Toast.makeText(this, "select an image to insert", Toast.LENGTH_SHORT).show();
                    }
                });


        add_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(EditProfileActivity.this)
                        .crop()
                        .maxResultSize(90,90,true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                launcher.launch(it);
                            }
                        }));
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                Log.d("Edited phoneNm : ",edited_phNm.getText().toString());
                hashMap.put("phone number",edited_phNm.getText().toString());
                hashMap.put("first name",edited_first_Name.getText().toString());
                hashMap.put("last name",edited_last_Name.getText().toString());

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_FIRSTNAME,edited_first_Name.getText().toString());
                editor.putString(KEY_LASTNAME,edited_last_Name.getText().toString());
                editor.putString(KEY_PHONENUM,edited_phNm.getText().toString());
                editor.commit();

                databaseReference.child(user.getUid()).updateChildren(hashMap);

                Toast.makeText(EditProfileActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
                            finish();

                    }
                },2000);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}