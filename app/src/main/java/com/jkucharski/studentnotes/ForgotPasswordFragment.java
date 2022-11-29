package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jkucharski.studentnotes.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {

    FragmentForgotPasswordBinding binding;
    private FirebaseAuth mAuth;
    String emailAddress;

    public ForgotPasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        binding.resetPasswordButton.setOnClickListener(view -> {
            emailAddress = binding.resetEmail.getText().toString().trim();
            if(emailAddress.isEmpty()){
                binding.resetEmail.setError("Email is required!");
                binding.resetEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                binding.resetEmail.setError("Please enter a valid email!");
                binding.resetEmail.requestFocus();
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Something went wrong! Try again!", Toast.LENGTH_LONG).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            });
        });

        return binding.getRoot();
    }
}