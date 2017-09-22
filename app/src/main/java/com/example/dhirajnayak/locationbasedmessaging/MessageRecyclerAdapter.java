package com.example.dhirajnayak.locationbasedmessaging;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by dhirajnayak on 9/20/17.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerViewHolder> {
    ArrayList<Message> arrayListMessage=new ArrayList<>();
    Context mContext;
    private IMessageListener messageListener;

    public MessageRecyclerAdapter(Context mContext, ArrayList<Message> arrayListMessage, IMessageListener iMessageListener) {
        this.arrayListMessage = arrayListMessage;
        this.mContext = mContext;
        this.messageListener = iMessageListener;

    }



    @Override
    public MessageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view= layoutInflater.inflate(R.layout.inbox_msg_layout,parent,false);
        MessageRecyclerAdapter.MessageRecyclerViewHolder productRecyclerViewHolder=new MessageRecyclerAdapter.MessageRecyclerViewHolder(view);
        return productRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageRecyclerViewHolder holder, int position) {
        final Message message=arrayListMessage.get(position);
        if(message.getRead()==1){
            holder.imageViewBlue.setVisibility(View.INVISIBLE);
            holder.imageViewGrey.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewBlue.setVisibility(View.VISIBLE);
            holder.imageViewGrey.setVisibility(View.INVISIBLE);
        }
        if(message.getMessage().length()>45){
            holder.textViewMessage.setText(message.getMessage().substring(0,44));
        }else{
            holder.textViewMessage.setText(message.getMessage());
        }
        holder.textViewName.setText(message.getFirstName()+" "+message.getLastName());
        String changedDateString=new SimpleDateFormat("MM/dd/yy, hh:mm a").format(message.getDate());
        holder.textViewDate.setText(changedDateString);
        if(message.getLocked()==1){
            holder.imageViewLock.setVisibility(View.VISIBLE);
            holder.imageViewUnlock.setVisibility(View.INVISIBLE);
        }else{
            holder.imageViewLock.setVisibility(View.INVISIBLE);
            holder.imageViewUnlock.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageListener.messageDetail(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListMessage.size();
    }

    public static class MessageRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewBlue;
        ImageView imageViewGrey;
        ImageView imageViewLock;
        ImageView imageViewUnlock;
        TextView textViewName;
        TextView textViewDate;
        TextView textViewMessage;

        public MessageRecyclerViewHolder(View itemView) {
            super(itemView);
            imageViewBlue= (ImageView) itemView.findViewById(R.id.imageViewBlue);
            imageViewGrey= (ImageView) itemView.findViewById(R.id.imageViewGrey);
            imageViewLock= (ImageView) itemView.findViewById(R.id.imageViewLock);
            imageViewUnlock= (ImageView) itemView.findViewById(R.id.imageViewUnlock);
            textViewName= (TextView) itemView.findViewById(R.id.textViewInboxName);
            textViewDate= (TextView) itemView.findViewById(R.id.textViewInboxDate);
            textViewMessage= (TextView) itemView.findViewById(R.id.textViewInboxMessage);
        }
    }

    interface IMessageListener
    {
        void messageDetail(Message message);
    }

}

