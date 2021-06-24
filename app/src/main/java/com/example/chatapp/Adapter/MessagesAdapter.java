package com.example.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Messages;
import com.example.chatapp.R;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messageArrayList;
    int SEND_ITEM=1;
    int RECIEVE_ITEM=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messageArrayList){
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==SEND_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            Log.i("infooo","send item = "+ messageArrayList.size() + " "+ messageArrayList.get(0).getMessage());

            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout_item,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messageArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textMessage.setText(messages.getMessage());

        }else{
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.textMessage.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        Log.i("infoo","adapter = "+ messageArrayList.size());
        return messageArrayList.size();
    }

    public int getItemViewType(int position){
        Messages messages = messageArrayList.get(position);
        if(ParseUser.getCurrentUser().getObjectId().toString().equals(messages.getSenderId())){
            return SEND_ITEM;
        }else{
            return RECIEVE_ITEM;
        }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
         public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtMessages);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView textMessage;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtMessages);
        }
    }
}
