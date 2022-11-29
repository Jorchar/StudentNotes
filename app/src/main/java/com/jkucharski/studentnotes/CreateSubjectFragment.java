package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkucharski.studentnotes.databinding.FragmentCreateSubjectBinding;

public class CreateSubjectFragment extends Fragment {

    FragmentCreateSubjectBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    SubjectListFragment subjectListFragment;
    String firebaseReference;

    CreateSubjectFragment(FragmentManager fm){
        this.fm = fm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = FragmentCreateSubjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    binding.confirmNewSubjectButton.setOnClickListener(view1 -> {
        String subjectName = binding.subjectNameInput.getText().toString();
        String subjectDesc = binding.subjectDescriptionInput.getText().toString();

        SubjectDC subjectDC = new SubjectDC();
        subjectDC.setName(subjectName);
        subjectDC.setDescription(subjectDesc);
        firebaseReference = "Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                .child("Subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();
                subjectDC.setId(size);
                FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference+"/Subjects")
                        .child(Integer.toString(subjectDC.getId())).setValue(subjectDC);
                subjectListFragment = new SubjectListFragment(fm);
                ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, subjectListFragment);
                ft.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    });
    }
}