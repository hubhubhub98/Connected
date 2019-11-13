package com.changsdev.whoaressuproject.Testfunction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Makechatroom {

    public Makechatroom(String opposite){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Chatroom chatroom = new Chatroom(opposite);

        DatabaseReference postsRef = mDatabase.child("users").child("124124");
        postsRef.push().setValue(chatroom);
    }
}
