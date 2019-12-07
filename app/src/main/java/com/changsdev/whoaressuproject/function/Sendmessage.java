package com.changsdev.whoaressuproject.function;

import android.util.Log;

import com.changsdev.whoaressuproject.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Sendmessage {
    public Sendmessage(String sender,String oppositeuid, String text,String roomuid){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Message message = new Message(sender,text);
        String useruid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = mDatabase.child("messages/"+roomuid).push().getKey();
        mDatabase.child("messages/"+roomuid+"/"+key).setValue(message);
        Map<String,Object> updates = new HashMap<>();
        updates.put("RoomInfo/"+useruid+"/"+roomuid+"/Sender",message.Sender);
        updates.put("RoomInfo/"+useruid+"/"+roomuid+"/message",message.message);
        updates.put("RoomInfo/"+oppositeuid+"/"+roomuid+"/Sender",message.Sender);
        updates.put("RoomInfo/"+oppositeuid+"/"+roomuid+"/message",message.message);
        mDatabase.updateChildren(updates);
    }
}

