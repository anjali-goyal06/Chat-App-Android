package com.example.chatapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activity.ChatRoom;
import com.example.chatapp.Activity.UserList;
import com.example.chatapp.Model.Users;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    UserList userList;
    ArrayList<Users> users;


    public UserAdapter(UserList userList, ArrayList<Users> users) {
        this.userList = userList;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(userList).inflate(R.layout.user_row_items,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = users.get(position);
    //    Log.i("kkkkkfullinfo" , user.getEmailId() + user.getUserId() + user.getUsername() + user.getProfilePic());
        holder.userName.setText(user.getUsername());
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile).into(holder.user_profile);
        holder.lastMessage.setText(user.getAbout());
    //    Log.i("gggggggggggggggg","abc = " +  user.getProfilePic());

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + user.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                 holder.lastMessage.setText(dataSnapshot.child("message")
                                 .getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(userList, ChatRoom.class);
                intent.putExtra("userId",user.getUserId());
                intent.putExtra("userName",user.getUsername());
                intent.putExtra("profilePic",user.getProfilePic());
                userList.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView user_profile;
        TextView userName;
        TextView lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.username);
            lastMessage = itemView.findViewById(R.id.Name);
        }
    }
}
