package com.changsdev.whoaressuproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.activity_chatroom;
import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamname;
        TextView chatcontent;
        String roomuid;
        String oppositename;

        MyViewHolder(View view){
            super(view);
            teamname = view.findViewById(R.id.team_name);
            chatcontent = view.findViewById(R.id.chat_content);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    Intent intent = new Intent(v.getContext(), activity_chatroom.class);
                    intent.putExtra("roomuid",roomuid);
                    intent.putExtra("name",oppositename);
                    context.startActivity(intent);
                }
            });
        }
    }

    private ArrayList<ChatInfo> chatInfoArrayList;
    public MyAdapter(ArrayList<ChatInfo> chatInfoArrayList){
        this.chatInfoArrayList = chatInfoArrayList;
    }

    @Override // oncreateViewHolder는 아이템레이아웃을 뷰홀더에 고정시키는 역할을 함.
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override // onBindViewHolder는 뷰홀더에 데이터를 입력시킴.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.teamname.setText(chatInfoArrayList.get(position).sender);
        myViewHolder.chatcontent.setText(chatInfoArrayList.get(position).text);
        myViewHolder.roomuid=chatInfoArrayList.get(position).roomuid;
        myViewHolder.oppositename=chatInfoArrayList.get(position).oppositename;

    }

    @Override // 사이즈 확인
    public int getItemCount() {
        return chatInfoArrayList.size();
    }
}
