package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.Model.Users;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView mainRecyclerView ;
    UserAdapter userAdapter;
    ArrayList<Users> userlist = new ArrayList<>();
    ProgressDialog progressDialog;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logoutButton){
            auth.signOut();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else if(id==R.id.setting){
            Intent intent = new Intent(getApplicationContext(),Settings_activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        database  = FirebaseDatabase.getInstance();
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i("users.size = " , "s = " + userlist.size());
        userAdapter = new UserAdapter(UserList.this,userlist);
        mainRecyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(UserList.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Wait for a while");
        progressDialog.show();

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userlist.clear();
                 for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                     Users user = dataSnapshot.getValue(Users.class);
                     user.setUserId(dataSnapshot.getKey());

                     if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                     userlist.add(user);
                 }
                 userAdapter.notifyDataSetChanged();
                 progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


