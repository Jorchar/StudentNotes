package com.jkucharski.studentnotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    TextRecognitionFragment textRecognitionFragment;

    String filename;

    NoteEditorFragment(FragmentManager fm, String filename){
        this.fm = fm;
        this.filename = filename;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteEditorBinding.inflate(inflater, container, false);

        binding.ocrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textRecognitionFragment = new TextRecognitionFragment(fm);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, textRecognitionFragment);
                ft.commit();
            }
        });
        //TODO convert .docx file to html and show it in webview

        return binding.getRoot();
    }
}