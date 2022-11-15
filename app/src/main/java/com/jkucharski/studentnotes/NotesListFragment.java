package com.jkucharski.studentnotes;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jkucharski.studentnotes.databinding.FragmentNotesListBinding;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment {

    FragmentNotesListBinding binding;
    List<NoteDC> noteDCList  = new ArrayList<>();
    RoomDB database;
    NoteAdapter noteAdapter;
    RecyclerView noteRecyclerView;
    FragmentManager fm;
    FragmentTransaction ft;
    int subjectId;
    String noteName;


    NotesListFragment(FragmentManager fm, int subjectId) {
        this.fm = fm;
        this.subjectId = subjectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        binding = FragmentNotesListBinding.inflate(inflater, container, false);

        database = RoomDB.getInstance(getContext());
        noteDCList = database.noteDao().getAll(subjectId);

        noteRecyclerView = binding.recyclerViewNotes;
        noteAdapter = new NoteAdapter(fm, noteDCList, getActivity());
        noteRecyclerView.setAdapter(noteAdapter);
        noteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.createNoteButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Type in note's title");
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noteName = input.getText().toString();

                    NoteDC noteDC = new NoteDC();
                    noteDC.setName(noteName);
                    noteDC.setSubject(subjectId);
                    noteDC.setActive(true);
                    database.noteDao().insert(noteDC);
                    noteDCList.clear();
                    noteDCList.addAll(database.noteDao().getAll(subjectId));
                    noteAdapter.notifyDataSetChanged();
                    noteAdapter.addNote(subjectId+"_"+noteName, getContext());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        });

        return binding.getRoot();
    }
}