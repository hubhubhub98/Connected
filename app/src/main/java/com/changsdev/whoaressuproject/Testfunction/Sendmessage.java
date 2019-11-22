package com.changsdev.whoaressuproject.Testfunction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sendmessage {

    Sendmessage(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        message message= new message("김영경", "dudrud981126@naver.com");
        mDatabase.child("users").child("userId").setValue(message);
    }
}
