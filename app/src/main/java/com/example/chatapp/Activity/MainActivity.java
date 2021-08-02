package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Model.Users;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText PersonName;
    EditText Password;
    EditText email;
    Button SignUpButton;
    TextView switchText;

    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    public void nextPage(){
        Intent intent = new Intent(getApplicationContext(),UserList.class);
        startActivity(intent);
    }

    public void switchOption(View v){
        Intent intent = new Intent(getApplicationContext(),LogIn_activity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Log.i("kkkkkkk", "51  kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

            PersonName = findViewById(R.id.editTextPersonName);
            Password = findViewById(R.id.editTextTextPassword);
            switchText = findViewById(R.id.textView);
            email = findViewById(R.id.emailId);
            SignUpButton = findViewById(R.id.buttonLog);

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Wait for a while");

            SignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), Password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Users user = new Users(PersonName.getText().toString(), email.getText().toString(), Password.getText().toString());
                                        Toast.makeText(MainActivity.this, "User signedUp successfully", Toast.LENGTH_SHORT).show();

                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);
                                        nextPage();
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            if (auth.getCurrentUser() != null) {
                Log.i("kkkkkkk", auth.getCurrentUser().getEmail());
                nextPage();
            }

    }
}