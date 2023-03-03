package com.jkucharski.studentnotes.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jkucharski.studentnotes.R;
import com.jkucharski.studentnotes.databinding.FragmentLoginBinding;
import com.jkucharski.studentnotes.ui.subject.SubjectListFragment;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    RegisterFragment registerFragment;
    ForgotPasswordFragment forgotPasswordFragment;
    SubjectListFragment subjectListFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    private FirebaseAuth mAuth;
    String emailAddress;
    String password;

    public LoginFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> {
            emailAddress = binding.loginEmail.getText().toString().trim();
            password = binding.loginPassword.getText().toString();

            if(emailAddress.isEmpty()){
                binding.loginEmail.setError("Email is required!");
                binding.loginEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                binding.loginEmail.setError("Please enter a valid email!");
                binding.loginEmail.requestFocus();
                return;
            }
            if(password.isEmpty()){
                binding.loginPassword.setError("Password is required!");
                binding.loginPassword.requestFocus();
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.isEmailVerified()){
                        Toast.makeText(getContext(), "Logged successfully!", Toast.LENGTH_LONG).show();
                        binding.progressBar.setVisibility(View.GONE);

                        subjectListFragment = new SubjectListFragment(fm);
                        ft = fm.beginTransaction();
                        ft.replace(R.id.MainLayout, subjectListFragment);
                        ft.commit();
                    }else{
                        Toast.makeText(getContext(), "Please verify your email!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.register.setOnClickListener(v -> {
            registerFragment = new RegisterFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, registerFragment).addToBackStack(null);
            ft.commit();
        });

        binding.forgotPassword.setOnClickListener(v -> {
            forgotPasswordFragment = new ForgotPasswordFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, forgotPasswordFragment).addToBackStack(null);
            ft.commit();
        });
    }
}