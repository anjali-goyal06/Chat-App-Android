package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.MessagesAdapter;
import com.example.chatapp.Model.Messages;
import com.example.chatapp.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {

    ArrayList<Messages> messagesList = new ArrayList<Messages>();
    String recieverId, senderId, recieverName, senderName;

    int currentPositionOfMessages = 0;
    ParseObject currentObject;
    List listofMessagesInServer;
    String objectIdRoomSender;
    String objectIdRoomReciever;
    Handler handler;
    Runnable run;
    EditText editText;
    CardView sentBtn;
    ParseQuery<ParseObject> q;
    int flag1 = 1;
    int flag2 = 1;

    String senderRoom;
    String recieverRoom;

    RecyclerView messageAdapter;
    MessagesAdapter adapter;

    String TAG = "infoo";

    void findSenderRecieverId(String room) {
        q.whereEqualTo("room", room);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0)
                        findSenderRecieverId(room);
                    else {
                        if (room == senderRoom)
                            objectIdRoomSender = objects.get(0).getObjectId();
                        else {
                            objectIdRoomReciever = objects.get(0).getObjectId();
                        }
                        handler.post(run);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    void function(String room) {

        q.whereEqualTo("room", room);
        Log.i(TAG, "done: rrom " + room);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "done: " + objects.size());
                    if (objects.size() == 0) {
                        ParseObject obj = new ParseObject("Chats");
                        obj.put("room", room);
                        obj.saveInBackground();

                        findSenderRecieverId(room);

                        return;
                    }

                    if (room == senderRoom)
                        objectIdRoomSender = objects.get(0).getObjectId();
                    else {
                        objectIdRoomReciever = objects.get(0).getObjectId();
                        return;
                    }
                    currentObject = objects.get(0);
                    listofMessagesInServer = objects.get(0).getList("MessagesArray");
                    if (listofMessagesInServer == null || listofMessagesInServer.size() == 0)
                        return;
                    for (int i = 0; i < listofMessagesInServer.size(); i++) {
                        ParseObject obj = (ParseObject) listofMessagesInServer.get(i);
                        try {
                            ParseObject obj2 = obj.fetchIfNeeded();

                            String idOfPerson = obj2.getString("senderId");
                            Messages msg = new Messages(obj2.getString("message"), idOfPerson);
                            messagesList.add(msg);

                            adapter.notifyDataSetChanged();
                            currentPositionOfMessages++;
                        } catch (ParseException parseException) {
                            Log.i(TAG, "done: exception  =  " + parseException.getMessage());
                            parseException.printStackTrace();
                        }
                    }


                    Log.i(TAG, "onCreate: current position" + currentPositionOfMessages);
                    handler.post(run);

                } else {
                    Toast.makeText(ChatRoom.this, "oncreate error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }


    void upDateDatabase(String message, String objectIdRoom, ParseObject obj) {


        q.getInBackground(objectIdRoom, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "id = " + object.getObjectId());
                    object.add("MessagesArray", obj);
                    object.saveInBackground();
                } else {
                    Log.i(TAG, "e printstack = " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        senderName = ParseUser.getCurrentUser().getUsername();
        senderId = ParseUser.getCurrentUser().getObjectId().toString();

        Intent intent = getIntent();
        recieverName = intent.getStringExtra("userName");
        setTitle(recieverName);

        recieverId = intent.getStringExtra("recieverId");
        senderRoom = senderId + "" + recieverId;
        recieverRoom = recieverId + "" + senderId;

        q = new ParseQuery<ParseObject>("Chats");

        function(senderRoom);
        function(recieverRoom);


        messageAdapter = findViewById(R.id.messageAdater);
        adapter = new MessagesAdapter(ChatRoom.this, messagesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        messageAdapter.setAdapter(adapter);

        editText = findViewById(R.id.editMessage);
        sentBtn = findViewById(R.id.sendBtn);


        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: ");
                q.whereEqualTo("objectId", objectIdRoomSender);
                q.getInBackground(objectIdRoomSender, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null && object != null) {

                            listofMessagesInServer = object.getList("MessagesArray");
                            Log.i(TAG, "done: " + listofMessagesInServer + " " + currentPositionOfMessages);
                            if (listofMessagesInServer != null && listofMessagesInServer.size() > currentPositionOfMessages) {

                                for (; currentPositionOfMessages < listofMessagesInServer.size(); currentPositionOfMessages++) {
                                    //  if(currentPositionOfMessages>0) continue;
                                    ParseObject obj = (ParseObject) listofMessagesInServer.get(currentPositionOfMessages);

                                    ParseObject obj2;
                                    try {
                                        obj2 = obj.fetchIfNeeded();
                                        String idOfPerson = obj2.getString("senderId");
                                        Messages msg = new Messages(obj2.getString("message"), idOfPerson);
                                        Log.i(TAG, "done: msgg ====" + msg.getMessage());
                                        messagesList.add(msg);

                                        adapter.notifyDataSetChanged();
                                    } catch (ParseException parseException) {
                                        parseException.printStackTrace();
                                    }

                                }
                            }
                        } else {
                            handler.postDelayed(run, 3000);
                            e.printStackTrace();
                        }
                    }
                });

                handler.postDelayed(this::run, 3000);
            }
        };


        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatRoom.this, "Please enter the valid Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");

                ParseObject obj = new ParseObject("msg");
                obj.put("message", message);
                obj.put("senderId", senderId);
                upDateDatabase(message, objectIdRoomSender, obj);
                upDateDatabase(message, objectIdRoomReciever, obj);
            }
        });
    }
}
