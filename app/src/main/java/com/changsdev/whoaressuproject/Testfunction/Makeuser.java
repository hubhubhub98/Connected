package com.changsdev.whoaressuproject.Testfunction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Makeuser{
    Makeuser() {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        User user = new User("김영경", "dudrud981126@naver.com");
        mDatabase.child("users").child("userId").setValue(user);
    }
}
