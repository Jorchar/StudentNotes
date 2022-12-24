package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.jkucharski.studentnotes.databinding.FragmentCreateSubjectBinding;
import com.jkucharski.studentnotes.databinding.FragmentEditSubjectBinding;

public class EditSubjectFragment extends Fragment {

    FragmentEditSubjectBinding binding;
    FragmentManager fm;
    String firebaseReference;
    SubjectDC subjectDC;

    public EditSubjectFragment(FragmentManager fm, SubjectDC subjectDC, String firebaseReference) {
        this.fm = fm;
        this.subjectDC = subjectDC;
        this.firebaseReference = firebaseReference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditSubjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.subjectNameInput.setText(subjectDC.getName());
        binding.subjectDescriptionInput.setText(subjectDC.getDescription());

        binding.confirmEditSubjectButton.setOnClickListener(v -> {
            String subjectName = binding.subjectNameInput.getText().toString();
            String subjectDesc = binding.subjectDescriptionInput.getText().toString();
            subjectDC.setName(subjectName);
            subjectDC.setDescription(subjectDesc);

            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                    .child("name")
                    .setValue(subjectName);
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                    .child("description")
                    .setValue(subjectDesc).addOnSuccessListener(unused -> {
                        fm.popBackStack();
                    });
        });
    }
}