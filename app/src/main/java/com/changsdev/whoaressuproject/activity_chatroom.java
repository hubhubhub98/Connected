package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.changsdev.whoaressuproject.Adapter.ChatAdapter;
import com.changsdev.whoaressuproject.Adapter.MyAdapter;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.model.Datamodel;
import com.changsdev.whoaressuproject.model.Message;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_chatroom extends AppCompatActivity {
    TextView name1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatroom);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        String name = getIntent().getStringExtra("name");
        String roomuid = getIntent().getStringExtra("roomuid");
        name1=findViewById(R.id.oppositename);
        name1.setText(name);
        final String[] username = new String[1];
        FirebaseDatabase.getInstance().getReference().child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username[0] =dataSnapshot.child("userName").getValue(String.class);
                Log.d("ㅇㅇ",username[0]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final RecyclerView mRecyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase;

        final ArrayList<Message> messages = new ArrayList<>();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("messages/"+roomuid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot aa : dataSnapshot.getChildren()){
                    Message message = aa.getValue(Message.class);
                    messages.add(new Message(message.Sender,message.message));
                }
                ChatAdapter chatAdapter = new ChatAdapter(messages, username[0]);
                mRecyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
