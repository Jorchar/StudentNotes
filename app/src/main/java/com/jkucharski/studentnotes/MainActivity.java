package com.jkucharski.studentnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SubjectListFragment subjectListFragment;
    AccountSettingsFragment accountSettingsFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    1);
        }

        fm = getSupportFragmentManager();
        subjectListFragment = new SubjectListFragment(fm);
        ft = fm.beginTransaction();
        ft.replace(R.id.MainLayout, subjectListFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.accountButton:
                accountSettingsFragment = new AccountSettingsFragment(fm);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, accountSettingsFragment).addToBackStack(null);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}