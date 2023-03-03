package com.jkucharski.studentnotes.ui.subject;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkucharski.studentnotes.NoteAdapter;
import com.jkucharski.studentnotes.R;
import com.jkucharski.studentnotes.databinding.FragmentNotesListBinding;
import com.jkucharski.studentnotes.model.NoteDC;
import com.jkucharski.studentnotes.ui.editor.ColorSpinnerAdapter;
import com.jkucharski.studentnotes.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment {

    FragmentNotesListBinding binding;
    NoteAdapter noteAdapter;
    RecyclerView noteRecyclerView;
    FragmentManager fm;
    int subjectId;
    String noteName;
    String firebaseReference;
    List<NoteDC> noteDCList = new ArrayList<>();
    Integer noteColor = Color.parseColor("#339933");


    public NotesListFragment(FragmentManager fm, int subjectId) {
        this.fm = fm;
        this.subjectId = subjectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        binding = FragmentNotesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseReference = "Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Subjects/"+subjectId;
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                .getReference(firebaseReference+"/Notes").get()
                .addOnSuccessListener(dataSnapshot -> {
                    noteDCList.clear();
                    noteAdapter.setNote(noteDCList);
                    noteAdapter.notifyDataSetChanged();
                    for(DataSnapshot item_snapshot:dataSnapshot.getChildren()){
                        if((Boolean)item_snapshot.child("active").getValue()){
                            NoteDC noteDC = item_snapshot.getValue(NoteDC.class);
                            noteDCList.add(noteDC);
                        }
                    }
                    noteAdapter.setNote(noteDCList);
                });

        noteRecyclerView = binding.recyclerViewNotes;
        noteAdapter = new NoteAdapter(fm, firebaseReference+"/Notes", getContext());
        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.createNoteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Type in note's title");
            final View noteDialogLayout = getLayoutInflater().inflate(R.layout.create_note_dialog, null);
            EditText noteNameET = noteDialogLayout.findViewById(R.id.createNoteName);
            Spinner noteColorSpinner = noteDialogLayout.findViewById(R.id.chooseNoteColor);
            ColorSpinnerAdapter noteColorAdapter = new ColorSpinnerAdapter(getContext(), Const.getCardColorList());
            noteColorSpinner.setAdapter(noteColorAdapter);
            noteColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    noteColor = (Integer)noteColorAdapter.getItem(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            builder.setView(noteDialogLayout);
            builder.setPositiveButton("OK", (dialog, which) -> {
                noteName = noteNameET.getText().toString();
                NoteDC noteDC = new NoteDC();
                noteDC.setName(noteName);
                noteDC.setSubject(subjectId);
                noteDC.setColor(noteColor);
                FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                        .child("Notes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int size = (int) snapshot.getChildrenCount();
                        noteDC.setId(size);
                        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference+"/Notes")
                                .child(Integer.toString(noteDC.getId())).setValue(noteDC);
                        noteDCList.add(noteDC);
                        noteAdapter.setNote(noteDCList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

    }
}