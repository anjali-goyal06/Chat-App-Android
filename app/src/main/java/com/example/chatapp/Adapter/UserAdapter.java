package com.example.chatapp.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activity.ChatRoom;
import com.example.chatapp.Activity.UserList;
import com.example.chatapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    UserList userList;
    ArrayList<String> users;


    public UserAdapter(UserList userList, ArrayList<String> users) {
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
        String user = users.get(position);
        holder.userName.setText(user);
        holder.Name.setText(user);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] userId = new String[2];
                Intent intent = new Intent(userList, ChatRoom.class);

                intent.putExtra("userName",user);

                //userList.startActivity(intent);
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username",user);

                userId[0] = null;
                userId[1] =ParseUser.getCurrentUser().getObjectId();

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            Log.i("infoo , ", " recieever id = " + objects.size());
                            if (objects.size() > 0) {
                                userId[0] = objects.get(0).getObjectId();
                                intent.putExtra("recieverId",userId[0]);
                            }

                            userList.startActivity(intent);
                        } else {
                            Log.e("infoo", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


//


    @Override
    public int getItemCount()
    {
        Log.i("insideeee= " , " s = " + users);
       if(users!=null)
        return users.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView user_profile;
        TextView userName;
        TextView Name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.username);
            Name = itemView.findViewById(R.id.Name);
        }
    }
}
