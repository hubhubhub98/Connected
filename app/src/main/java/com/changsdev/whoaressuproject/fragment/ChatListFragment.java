package com.changsdev.whoaressuproject.fragment;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.Adapter.MyAdapter;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.Datamodel;
import com.changsdev.whoaressuproject.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        final RecyclerView mRecyclerView = v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase;

        final ArrayList<ChatInfo> chatInfoArrayList = new ArrayList<>();
        final ArrayList<String> indexes = new ArrayList<>();
        final MyAdapter myAdapter = new MyAdapter(chatInfoArrayList);
        mRecyclerView.setAdapter(myAdapter);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("RoomInfo/"+userUID);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Datamodel message = dataSnapshot.getValue(Datamodel.class);
                String uid = dataSnapshot.getKey();
                chatInfoArrayList.add(new ChatInfo(message.oppositeusername,message.message,uid,message.oppositeusername));
                indexes.add(uid);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Datamodel message = dataSnapshot.getValue(Datamodel.class);
                String uid = dataSnapshot.getKey();
                int index = indexes.indexOf(uid);
                chatInfoArrayList.set(index,new ChatInfo(message.oppositeusername,message.message,uid,message.oppositeusername));
                myAdapter.notifyDataSetChanged();
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
        return v;


    }

}
