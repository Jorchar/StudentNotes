package com.jkucharski.studentnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SubjectListFragment subjectListFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fm = getSupportFragmentManager();
        subjectListFragment = new SubjectListFragment(fm);
        ft = fm.beginTransaction();
        ft.replace(R.id.MainLayout, subjectListFragment);
        ft.commit();
    }
}