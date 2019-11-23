package com.changsdev.whoaressuproject.model;

import com.google.firebase.database.Exclude;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Message {
    public String Sender;
    public String message;

    public Message(){}

    public Message(String sender,String message){
        this.Sender=sender;
        this.message=message;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", Sender);
        result.put("text", message);
        return result;
    }
}
