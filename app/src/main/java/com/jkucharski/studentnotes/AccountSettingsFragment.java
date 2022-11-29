package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.jkucharski.studentnotes.databinding.FragmentAccountSettingsBinding;

public class AccountSettingsFragment extends Fragment {

    FragmentAccountSettingsBinding binding;
    LoginFragment loginFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    public AccountSettingsFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.logOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            loginFragment = new LoginFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, loginFragment);
            ft.commit();
        });
    }
}