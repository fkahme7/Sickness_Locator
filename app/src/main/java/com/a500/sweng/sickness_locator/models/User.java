package com.a500.sweng.sickness_locator.models;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private DatabaseReference mDatabase;

    public String name;
    public String email;

    public User() {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    public void createUser(String name) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            String email = fUser.getEmail();
            String uid = fUser.getUid();

            this.email = email;
            this.name = name;

            mDatabase.child(uid).setValue(this);
        }
    }
}
