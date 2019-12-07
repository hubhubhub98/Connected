package com.changsdev.whoaressuproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.activity_chatroom;
import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.function.Sendmessage;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.Message;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatitem1;
        TextView chatitem2;
        String sender;

        ChatViewHolder(View view) {
            super(view);
            chatitem1 = view.findViewById(R.id.message1);
            chatitem2=view.findViewById(R.id.message2);
        }
    }

    private ArrayList<Message> messages;
    private String user;

    public ChatAdapter(ArrayList<Message> messages, String username) {
        this.messages = messages;
        this.user = username;
    }

    @Override // oncreateViewHolder는 아이템레이아웃을 뷰홀더에 고정시키는 역할을 함.
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Layout layout;
        View v=null;

        switch (viewType) {
            case 0:
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, parent, false);
            v.findViewById(R.id.message1).setBackgroundResource(R.drawable.outbox2);
            v.findViewById(R.id.message2).setVisibility(View.GONE);
            System.out.println("내꺼");
            break;

            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, parent, false);
                v.findViewById(R.id.message2).setBackgroundResource(R.drawable.inbox2);
                v.findViewById(R.id.message1).setVisibility(View.GONE);
                System.out.println("남꺼");
                break;
        }
        return new ChatViewHolder(v);

    }

    @Override // onBindViewHolder는 뷰홀더에 데이터를 입력시킴.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
        chatViewHolder.chatitem1.setText(messages.get(position).message);
        chatViewHolder.chatitem2.setText(messages.get(position).message);
        chatViewHolder.sender = messages.get(position).Sender;

    }


    @Override // 사이즈 확인
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(final int position) {
        if (user.equals(messages.get(position).Sender)) {
            System.out.println(user);
            System.out.println(messages.get(position).Sender);
            System.out.println("00");
            return 0;
        } else {
            System.out.println(user);
            System.out.println(messages.get(position).Sender);
            System.out.println("11");
            return 1;
        }
    }
}
