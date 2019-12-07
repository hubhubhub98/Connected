package com.changsdev.whoaressuproject.function;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfo {

    public UserInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userUID = mAuth.getCurrentUser().getUid();
        boolean org;
        String uid;
        String email;
        String name;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users/" + userUID);

    }
}