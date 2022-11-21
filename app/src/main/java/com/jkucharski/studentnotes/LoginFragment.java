package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jkucharski.studentnotes.databinding.FragmentAccountSettingsBinding;
import com.jkucharski.studentnotes.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    RegisterFragment registerFragment;
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

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(view -> {
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

            mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user.isEmailVerified()){
                            //TODO change to logged user mode
                        }else{
                            Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        binding.register.setOnClickListener(view -> {
            registerFragment = new RegisterFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, registerFragment).addToBackStack(null);
            ft.commit();
        });

        return binding.getRoot();
    }
}