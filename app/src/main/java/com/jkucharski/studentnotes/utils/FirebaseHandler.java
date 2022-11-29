package com.jkucharski.studentnotes.utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance("https://studentnotes-222a3-default-rtdb.europe-west1.firebasedatabase.app/")
                .setPersistenceEnabled(true);
    }
}
