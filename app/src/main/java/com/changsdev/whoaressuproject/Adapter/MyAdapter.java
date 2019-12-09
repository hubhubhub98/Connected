package com.changsdev.whoaressuproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.activity_chatroom;
import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ChatInfo> chatInfoArrayList;
    private String email;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamname;
        TextView chatcontent;
        String roomuid;
        String oppositename;
        TextView count;

        MyViewHolder(View view){
            super(view);
            teamname = view.findViewById(R.id.team_name);
            chatcontent = view.findViewById(R.id.chat_content);
            count=view.findViewById(R.id.seencount);

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

    public MyAdapter(ArrayList<ChatInfo> chatInfoArrayList,String email){
        this.chatInfoArrayList = chatInfoArrayList;
        this.email=email;
    }

    @Override // oncreateViewHolder는 아이템레이아웃을 뷰홀더에 고정시키는 역할을 함.
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override // onBindViewHolder는 뷰홀더에 데이터를 입력시킴.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.teamname.setText(chatInfoArrayList.get(position).oppositename);
        myViewHolder.chatcontent.setText(chatInfoArrayList.get(position).text);
        myViewHolder.roomuid=chatInfoArrayList.get(position).roomuid;
        myViewHolder.oppositename=chatInfoArrayList.get(position).oppositename;


        final ArrayList<String> total=new ArrayList<>();
        final ArrayList<String> read=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("messages/"+chatInfoArrayList.get(position).roomuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total.clear();
                read.clear();
                for(DataSnapshot aa:dataSnapshot.getChildren()){
                    total.add("d");
                    System.out.println("토탈추가:"+total.size());
                    if(aa.child("readuser").toString().contains(email)) {
                        read.add("d");
                        System.out.println("개별추가:"+read.size());                    }
                }
                int count = total.size()-read.size();
                if(count!=0) {
                    myViewHolder.count.setVisibility(View.VISIBLE);
                    myViewHolder.count.setText(String.valueOf(count));
                }
                else if(count==0)
                    myViewHolder.count.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override // 사이즈 확인
    public int getItemCount() {
        return chatInfoArrayList.size();
    }
}
