package com.example.chatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.MessagesAdapter;
import com.example.chatapp.Model.Messages;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoom extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Messages> messagesList = new ArrayList<Messages>();

    int currentPositionOfMessages = 0;


    CardView sentBtn;
    ImageView profilePicToolBar;
    RecyclerView messageAdapter;
    MessagesAdapter adapter;
    EditText editText;
    String TAG = "infoo";
    TextView userNameTitle;
    ProgressDialog  progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        getSupportActionBar().hide();

        profilePicToolBar = findViewById(R.id.profile_image_toolbar);
        userNameTitle = findViewById(R.id.name);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String senderId = auth.getCurrentUser().getUid();



        Intent intent = getIntent();
        String reciverId= intent.getStringExtra("userId");
        String recieverName = intent.getStringExtra("userName");
        String profilePic = intent.getStringExtra("profilePic");
        userNameTitle.setText(recieverName);

        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(profilePicToolBar);


        messageAdapter = findViewById(R.id.messageAdater);
        adapter = new MessagesAdapter(ChatRoom.this, messagesList,reciverId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        messageAdapter.setAdapter(adapter);

        final String senderRoom = senderId + reciverId;
        final String recieverRoom = reciverId + senderId;

        editText = findViewById(R.id.editMessage);
        sentBtn = findViewById(R.id.sendBtn);


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Messages model = dataSnapshot.getValue(Messages.class);
                            model.setMessageId(dataSnapshot.getKey());
                            messagesList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                Log.i("helloo " , msg);
                if(msg.equals("")){
                    return;
                }
                editText.setText("");
                final Messages message = new Messages(msg,senderId);
               Log.i("timmme " , String.valueOf(new Date().getHours() + " - " + new Date().getMinutes() + " "));
               long time =  new Date().getHours()*100 + new Date().getMinutes();
                message.setTimeStamp(time);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });

    }
}
