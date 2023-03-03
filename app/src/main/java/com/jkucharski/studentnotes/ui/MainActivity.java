package com.jkucharski.studentnotes.ui;

import static com.jkucharski.studentnotes.utils.Const.CAMERA_PRM_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.jkucharski.studentnotes.R;
import com.jkucharski.studentnotes.ui.auth.LoginFragment;
import com.jkucharski.studentnotes.ui.subject.SubjectListFragment;

public class MainActivity extends AppCompatActivity {

    LoginFragment loginFragment;
    SubjectListFragment subjectListFragment;
    AccountSettingsFragment accountSettingsFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int diskPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(diskPermission != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    1);
        }

        fm = getSupportFragmentManager();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            subjectListFragment = new SubjectListFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, subjectListFragment);
        }else{
            loginFragment = new LoginFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, loginFragment);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                accountSettingsFragment = new AccountSettingsFragment(fm);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, accountSettingsFragment).addToBackStack(null);
                ft.commit();
                return true;
    }
}