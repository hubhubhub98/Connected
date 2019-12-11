package com.changsdev.whoaressuproject.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.Adapter.MyAdapter;
import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.model.Datamodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    String email;
    MyAdapter myAdapter;
    ArrayList<ChatInfo> chatInfoArrayList;
    RecyclerView mRecyclerView;
    EditText SearchWard;



    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        SearchWard=v.findViewById(R.id.listsearch);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users/"+ userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email=dataSnapshot.child("userEmail").getValue(String.class);
                myAdapter = new MyAdapter(chatInfoArrayList,email);
                mRecyclerView.setAdapter(myAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference mDatabase;
        chatInfoArrayList = new ArrayList<>();
        final ArrayList<String> indexes = new ArrayList<>();

        mDatabase=FirebaseDatabase.getInstance().getReference().child("RoomInfo/"+userUID);
        mDatabase.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Datamodel message = dataSnapshot.getValue(Datamodel.class);
                String uid = dataSnapshot.getKey();
                chatInfoArrayList.add(new ChatInfo(message.Sender,message.message,uid,message.oppositeusername,message.oppositeUID));
                indexes.add(uid);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Datamodel message = dataSnapshot.getValue(Datamodel.class);
                String uid = dataSnapshot.getKey();
                int index = indexes.indexOf(uid);
                chatInfoArrayList.add(indexes.size(),new ChatInfo(message.Sender,message.message,uid,message.oppositeusername,message.oppositeUID));
                indexes.add(indexes.size(),uid);
                chatInfoArrayList.remove(index);
                indexes.remove(index);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getKey();
                int index = indexes.indexOf(uid);
                chatInfoArrayList.remove(index);
                indexes.remove(index);
                myAdapter.notifyDataSetChanged();
        }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
