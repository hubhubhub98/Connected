package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.changsdev.whoaressuproject.Adapter.ChatAdapter;
import com.changsdev.whoaressuproject.Adapter.MyAdapter;
import com.changsdev.whoaressuproject.function.Sendmessage;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.model.Datamodel;
import com.changsdev.whoaressuproject.model.Message;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_chatroom extends AppCompatActivity {
    TextView name1;
    Button snd_button;
    EditText send_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatroom);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        String name = getIntent().getStringExtra("name");
        final String roomuid = getIntent().getStringExtra("roomuid");
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
        final RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase;

        final ArrayList<Message> messages = new ArrayList<>();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("messages/"+roomuid);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatAdapter chatAdapter = new ChatAdapter(messages, username[0]);
                mRecyclerView.setAdapter(chatAdapter);
                Message message = dataSnapshot.getValue(Message.class);
                messages.add(new Message(message.Sender,message.message));
                chatAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        snd_button=findViewById(R.id.send_btn);
        send_txt=findViewById(R.id.send_txt);
        snd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("RoomInfo/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+roomuid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String oppositeuid =dataSnapshot.child("oppositeUID").getValue(String.class);
                        new Sendmessage(username[0],oppositeuid,send_txt.getText().toString(),roomuid);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}
