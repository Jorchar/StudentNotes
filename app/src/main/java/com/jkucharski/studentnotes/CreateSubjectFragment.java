package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jkucharski.studentnotes.databinding.FragmentCreateSubjectBinding;

public class CreateSubjectFragment extends Fragment {

    FragmentCreateSubjectBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    SubjectListFragment subjectListFragment;

    CreateSubjectFragment(FragmentManager fm){
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateSubjectBinding.inflate(inflater, container, false);

        binding.confirmNewSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText subjectNameET = binding.subjectNameInput;
                EditText subjectDescET = binding.subjectDescriptionInput;
                String subjectName = subjectNameET.getText().toString();
                String subjectDesc = subjectDescET.getText().toString();

                SubjectAdapter subjectAdapter = new SubjectAdapter();
                subjectAdapter.addSubject(subjectName, subjectDesc);

                subjectListFragment = new SubjectListFragment(fm);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, subjectListFragment);
                ft.commit();
            }
        });

        return binding.getRoot();
    }
}