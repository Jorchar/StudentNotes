package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkucharski.studentnotes.databinding.FragmentSubjectListBinding;

import java.util.ArrayList;
import java.util.List;

public class SubjectListFragment extends Fragment {

    FragmentSubjectListBinding binding;
    List<SubjectDC> subjectDCList = new ArrayList<>();
    RoomDB database;
    SubjectAdapter subjectAdapter;
    RecyclerView subjectRecyclerView;
    CreateSubjectFragment createSubjectFragment;
    FragmentManager fm;
    FragmentTransaction ft;

    SubjectListFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSubjectListBinding.inflate(inflater, container, false);

        database = RoomDB.getInstance(getContext());
        subjectDCList = database.subjectDao().getAll();

        subjectAdapter = new SubjectAdapter(fm, subjectDCList, getActivity());
        subjectRecyclerView = binding.recyclerViewSubject;
        subjectRecyclerView.setAdapter(subjectAdapter);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.createSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSubjectFragment = new CreateSubjectFragment(fm, database, subjectAdapter, subjectDCList);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, createSubjectFragment).addToBackStack(null);
                ft.commit();
            }
        });

        return binding.getRoot();
    }

}