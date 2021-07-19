package com.example.chatapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Messages;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class  MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messageArrayList;
    String recieverId;
    int SEND_ITEM=1;
    int RECIEVE_ITEM=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messageArrayList){
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    public MessagesAdapter(Context context, ArrayList<Messages> messageArrayList, String recieverId) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.recieverId = recieverId;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Delete Message ?? ")
                        .setPositiveButton("Delete for Me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom  = FirebaseAuth.getInstance().getUid() + recieverId;
                                database.getReference().child("chats").child(senderRoom).
                                        child(messages.getMessageId())
                                        .setValue(null);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textMessage.setText(messages.getMessage());
//            viewHolder.date.setText((int) messages.getTimeStamp());
        }else{
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.textMessage.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
     //   Log.i("infoo","adapter = "+ messageArrayList.size());
        return messageArrayList.size();
    }

    public int getItemViewType(int position){
        Messages messages = messageArrayList.get(position);
        if(messages.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return SEND_ITEM;
        }else{
            return RECIEVE_ITEM;
        }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView date;
         public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtMessages);
            date = itemView.findViewById(R.id.timeStampSender);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView textMessage;
        TextView date;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.txtMessages);
            date = itemView.findViewById(R.id.timeStampReciever);
        }
    }
}
