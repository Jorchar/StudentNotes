package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jkucharski.studentnotes.databinding.FragmentLoginBinding;
import com.jkucharski.studentnotes.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;

    private FirebaseAuth mAuth;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        binding.registerButton.setOnClickListener(view -> {
            String emailAddress = binding.registerEmail.getText().toString();
            String password = binding.registerPassword.getText().toString();

            if(emailAddress.isEmpty()){
                binding.registerEmail.setError("Email address is required!");
                binding.registerEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                binding.registerEmail.setError("Please provide valid email!");
                binding.registerEmail.requestFocus();
                return;
            }

            if(password.isEmpty()){
                binding.registerPassword.setError("Password is required!");
                binding.registerPassword.requestFocus();
                return;
            }

            if(password.length() < 8){
                binding.registerPassword.setError("Minimum password length should be 8 characters!");
                binding.registerPassword.requestFocus();
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    UserDC user = new UserDC(emailAddress);

                    FirebaseDatabase.getInstance("https://studentnotes-222a3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(), "Failed to registered!", Toast.LENGTH_LONG).show();
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "Failed to registered!", Toast.LENGTH_LONG).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            });
        });

        return binding.getRoot();
    }
}