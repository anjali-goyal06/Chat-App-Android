package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn_activity extends AppCompatActivity {

    EditText Password;
    EditText email;
    Button SignInButton;
    Boolean SignUpOnFlag = true;
    TextView switchText;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    public void nextPage(){
        Intent intent = new Intent(getApplicationContext(),UserList.class);
        startActivity(intent);
    }

    public void switchPage(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_activity);

        Password      = findViewById(R.id.signIn_Password);
        switchText    = findViewById(R.id.switchPage);
        email        = findViewById(R.id.signInEmail);
        SignInButton  = findViewById(R.id.sign_in_button);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(LogIn_activity.this);
        progressDialog.setTitle("Login Accound");
        progressDialog.setMessage("Wait for a while");

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().isEmpty()){
                    email.setError("Please Enter emailId");
                    return;
                }
                if(Password.getText().toString().isEmpty()){
                    Password.setError("Please Enter Password");
                    return;
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(email.getText().toString(),Password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    nextPage();
                                }else{
                                    Toast.makeText(LogIn_activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}