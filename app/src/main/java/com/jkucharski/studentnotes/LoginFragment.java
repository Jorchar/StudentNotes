package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkucharski.studentnotes.databinding.FragmentAccountSettingsBinding;
import com.jkucharski.studentnotes.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    RegisterFragment registerFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    public LoginFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.register.setOnClickListener(view -> {
            registerFragment = new RegisterFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, registerFragment).addToBackStack(null);
            ft.commit();
        });

        return binding.getRoot();
    }
}