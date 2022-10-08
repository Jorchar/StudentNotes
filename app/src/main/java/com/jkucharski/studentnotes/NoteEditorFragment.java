package com.jkucharski.studentnotes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    TextRecognitionFragment textRecognitionFragment;
    private RichEditor mEditor;
    private TextView mPreview;

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
        //TODO implement textEditor
        mEditor = (RichEditor) binding.textEditor;
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //TODO editable note background
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        mPreview = binding.preview;
        binding.setBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        return binding.getRoot();
    }
}