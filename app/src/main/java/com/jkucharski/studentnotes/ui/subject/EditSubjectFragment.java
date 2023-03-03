package com.jkucharski.studentnotes.ui.subject;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.database.FirebaseDatabase;
import com.jkucharski.studentnotes.databinding.FragmentEditSubjectBinding;
import com.jkucharski.studentnotes.model.SubjectDC;
import com.jkucharski.studentnotes.ui.editor.ColorSpinnerAdapter;
import com.jkucharski.studentnotes.utils.Const;

public class EditSubjectFragment extends Fragment {

    FragmentEditSubjectBinding binding;
    FragmentManager fm;
    String firebaseReference;
    SubjectDC subjectDC;
    Integer subjectColor;

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
        subjectColor = subjectDC.getColor();

        Spinner subjectColorSpinner = binding.subjectColor;
        ColorSpinnerAdapter subjectColorAdapter = new ColorSpinnerAdapter(getContext(), Const.getCardColorList());
        subjectColorSpinner.setAdapter(subjectColorAdapter);
        subjectColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectColor = (Integer)subjectColorAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.confirmEditSubjectButton.setOnClickListener(v -> {
            String subjectName = binding.subjectNameInput.getText().toString();
            String subjectDesc = binding.subjectDescriptionInput.getText().toString();
            subjectDC.setName(subjectName);
            subjectDC.setDescription(subjectDesc);
            subjectDC.setColor(subjectColor);

            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                    .child("name")
                    .setValue(subjectName);
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                    .child("description")
                    .setValue(subjectDesc);
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                    .child("color")
                    .setValue(subjectColor).addOnSuccessListener(unused -> {
                        fm.popBackStack();
                    });
        });
    }
}