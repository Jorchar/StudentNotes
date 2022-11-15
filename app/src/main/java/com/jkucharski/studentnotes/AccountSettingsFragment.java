package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkucharski.studentnotes.databinding.FragmentAccountSettingsBinding;

public class AccountSettingsFragment extends Fragment {

    FragmentAccountSettingsBinding binding;
    RoomDB database;
    LoginFragment loginFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    public AccountSettingsFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);

        binding.loginInButton.setOnClickListener(view -> {
            loginFragment = new LoginFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, loginFragment).addToBackStack(null);
            ft.commit();
        });

        return binding.getRoot();
    }
}