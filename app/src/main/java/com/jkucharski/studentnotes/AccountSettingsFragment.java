package com.jkucharski.studentnotes;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.jkucharski.studentnotes.databinding.FragmentAccountSettingsBinding;

import java.util.Locale;

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

        Spinner langueageSpinner = binding.languageSpinner;
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(getContext(), R.array.language_array, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langueageSpinner.setAdapter(languageAdapter);
        langueageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setLocale("en");
                        break;
                    case 1:
                        setLocale("pl");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.logOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            loginFragment = new LoginFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, loginFragment);
            ft.commit();
        });
    }

    private void setLocale(String language) {

        Configuration overrideConfig = getActivity().getBaseContext().getResources().getConfiguration();
        overrideConfig.setLocale(new Locale(language));
        Context context = getActivity().createConfigurationContext(overrideConfig);
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        resources.updateConfiguration(overrideConfig, metrics);
        onConfigurationChanged(overrideConfig);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        binding.languageTextView.setText(R.string.language);
        binding.logOutButton.setText(R.string.logout);
    }
}