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
    FragmentManager fm;
    FragmentTransaction ft;

    public AccountSettingsFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }
}