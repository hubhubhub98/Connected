package com.changsdev.whoaressuproject.function;

import com.changsdev.whoaressuproject.model.Chatroom;
import com.changsdev.whoaressuproject.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Makechatroom {

    public Makechatroom(String myusername,String oppositeUID, String oppositeusername){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();


        Chatroom chatroom = new Chatroom(oppositeUID,oppositeusername);
        Chatroom chatroom1 = new Chatroom(userUID,myusername);

        String key = mDatabase.child("RoomInfo/"+userUID).push().getKey();
        mDatabase.child("RoomInfo/"+userUID+"/"+key+"/").setValue(chatroom);
        mDatabase.child("RoomInfo/"+oppositeUID+"/"+key).setValue(chatroom1);
    }
}
