package com.example.newtemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    public static final String TAG1 = "TAG";
    EditText emailId,password,name,phoneNo;
    Button signUp;
   TextView tvSignIn;
   FirebaseAuth mFirebaseAuth;
   ProgressBar progressBar;
   FirebaseFirestore fStore;
   String userId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        phoneNo= findViewById(R.id.editTextPhone);
        name = findViewById(R.id.editTextTextPersonName2);
        signUp = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);


        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email =emailId.getText().toString();
                String pwd =password.getText().toString();

                String pn=phoneNo.getText().toString();
                String nm =name.getText().toString();
                if(email.isEmpty()){

                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();

                }
                else if (pn.isEmpty()) {
                        phoneNo.setError("Please enter  correct phone number ");
                        phoneNo.requestFocus();


                }
                else if(nm.isEmpty()){
                    name.setError("Please enter your name ");
                    name.requestFocus();

                }
                else if(email.isEmpty() && pwd.isEmpty() && pn.isEmpty() && nm.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();

                }
                else if(!(email.isEmpty() && pwd.isEmpty() && pn.isEmpty() && nm.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"SignUP UnSuccessful,please try again",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"SignUP Successful!!!",Toast.LENGTH_SHORT).show();
                                userId = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userId);
                                Map<String, Object> user = new HashMap<>();
                                user.put("FullName",nm);
                                user.put("Email",emailId);
                                user.put("PhoneNo",pn);
                                documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                                    Log.d(TAG,"onSuccess: user profile is created for " + userId);
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());

                                    }
                                });
                                startActivity(new Intent(MainActivity.this,homeActivity2.class));

                            }
                        }
                    });
                }
               else{
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Logintemplate.class);
                startActivity(i);
            }
        });

    }
}