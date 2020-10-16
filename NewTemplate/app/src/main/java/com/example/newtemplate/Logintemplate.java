package com.example.newtemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logintemplate extends AppCompatActivity {

    EditText emailId,password;
    Button signIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logintemplate);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        signIn = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null) {
                    Toast.makeText(Logintemplate.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Logintemplate.this, homeActivity2.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(Logintemplate.this,"Please Login",Toast.LENGTH_SHORT).show();
                }

            }

        };
        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();


                if(email.isEmpty()){

                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();

                }



                else if(email.isEmpty() && pwd.isEmpty() ) {
                    Toast.makeText(Logintemplate.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();

                }
                else if(!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Logintemplate.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Logintemplate.this, "Login Error,Please Login Again", Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intToHome = new Intent(Logintemplate.this, homeActivity2.class);
                                startActivity(intToHome);
                            }
                        }


                    });
                }

                else{
                    Toast.makeText(Logintemplate.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }


        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent (Logintemplate.this,MainActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}