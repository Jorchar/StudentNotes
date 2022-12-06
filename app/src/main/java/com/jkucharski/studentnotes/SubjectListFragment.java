package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jkucharski.studentnotes.databinding.FragmentSubjectListBinding;

import java.util.ArrayList;
import java.util.List;

public class SubjectListFragment extends Fragment {

    FragmentSubjectListBinding binding;
    SubjectAdapter subjectAdapter;
    RecyclerView subjectRecyclerView;
    CreateSubjectFragment createSubjectFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    String firebaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    SubjectListFragment(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentSubjectListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseReference = "Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Subjects";
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                .getReference(firebaseReference).get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<SubjectDC> subjectDCList = new ArrayList<>();
                    for(DataSnapshot item_snapshot:dataSnapshot.getChildren()){
                        if((Boolean)item_snapshot.child("active").getValue()){
                            SubjectDC subjectDC = item_snapshot.getValue(SubjectDC.class);
                            subjectDCList.add(subjectDC);
                        }
                    }
                    subjectAdapter.setSubjects(subjectDCList);
                });

        subjectAdapter = new SubjectAdapter(fm, firebaseReference);
        subjectRecyclerView = binding.recyclerViewSubject;
        subjectRecyclerView.setAdapter(subjectAdapter);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.createSubjectButton.setOnClickListener(v -> {
            createSubjectFragment = new CreateSubjectFragment(fm);
            ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, createSubjectFragment).addToBackStack(null);
            ft.commit();
        });



    }

}