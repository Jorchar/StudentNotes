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

import java.util.ArrayList;
import java.util.List;

public class CreateSubjectFragment extends Fragment {

    FragmentCreateSubjectBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    SubjectListFragment subjectListFragment;
    List<SubjectDC> subjectDCList = new ArrayList<>();
    RoomDB database;
    SubjectAdapter subjectAdapter;

    CreateSubjectFragment(FragmentManager fm, RoomDB database, SubjectAdapter subjectAdapter, List<SubjectDC> subjectDCList){
        this.fm = fm;
        this.subjectDCList = subjectDCList;
        this.database = database;
        this.subjectAdapter = subjectAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateSubjectBinding.inflate(inflater, container, false);

        binding.confirmNewSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = binding.subjectNameInput.getText().toString();
                String subjectDesc = binding.subjectDescriptionInput.getText().toString();

                SubjectDC subjectDC = new SubjectDC();
                subjectDC.setName(subjectName);
                subjectDC.setDescription(subjectDesc);
                database.subjectDao().insert(subjectDC);
                subjectDCList.clear();
                subjectDCList.addAll(database.subjectDao().getAll());

                subjectListFragment = new SubjectListFragment(fm);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, subjectListFragment);
                ft.commit();
            }
        });

        return binding.getRoot();
    }
}