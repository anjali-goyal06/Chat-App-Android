package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText PersonName;
    EditText Password;
    Button LogButton;
    Boolean SignUpOnFlag = true;
    TextView switchText;

    public void nextPage(){
        Intent intent = new Intent(getApplicationContext(),UserList.class);
        startActivity(intent);
    }

    public void SignUpButton(View view){

        if(PersonName.getText().toString().equals("") || Password.getText().toString().equals("")){
            Toast.makeText(this,"UserName and Password are required",Toast.LENGTH_SHORT).show();
        }else{
            ParseUser user = new ParseUser();
            if(SignUpOnFlag==true){
                user.setUsername(PersonName.getText().toString());
                user.setPassword(Password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this,"Sucessfully registered",Toast.LENGTH_SHORT).show();
                            nextPage();
                        }else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                user.logInInBackground(PersonName.getText().toString(), Password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this,"Sucessfully registered",Toast.LENGTH_SHORT).show();
                            nextPage();
                        }else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void switchOption(View view){
        PersonName.setText("");
        Password.setText("");
        Log.e("done", "switchOption: ");
        if(SignUpOnFlag){
            SignUpOnFlag = false;
            switchText.setText("Doesn't have account!!\nSignUp");
            LogButton.setText("Sign In");
        }
        else{
            SignUpOnFlag = true;
            switchText.setText("Already have account !!\nLog In");
            LogButton.setText("Sign Up");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PersonName = findViewById(R.id.editTextPersonName);
        Password = findViewById(R.id.editTextTextPassword);
        switchText = findViewById(R.id.textView);
        LogButton  = findViewById(R.id.buttonLog);


        if(ParseUser.getCurrentUser()!=null){
         //  ParseUser.logOut();
            //Log.i("username = ", ParseUser.getCurrentUser().getUsername());
          nextPage();
        }
    }
}