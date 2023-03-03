package com.jkucharski.studentnotes.utils;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                .setPersistenceEnabled(true);
    }
}
