package com.jkucharski.studentnotes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkucharski.studentnotes.databinding.EditorNavigationBarBinding;
import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    TextRecognitionFragment textRecognitionFragment;
    EditorNavigationBarBinding navigationBar;
    private RichEditor mEditor;
    String filename;

    NoteEditorFragment(FragmentManager fm, String filename){
        this.fm = fm;
        this.filename = filename;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteEditorBinding.inflate(inflater, container, false);

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

        navigationBar = binding.navigationBar;

        navigationBar.actionUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.undo();
            }
        });

        navigationBar.actionRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.redo();
            }
        });

        navigationBar.actionBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        navigationBar.actionItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        navigationBar.actionSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        navigationBar.actionSuperscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        navigationBar.actionStrikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        navigationBar.actionUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        navigationBar.actionHead1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        navigationBar.actionHead2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        navigationBar.actionHead3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        navigationBar.actionTxtColor.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        navigationBar.actionBgColor.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        navigationBar.actionAlignLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        navigationBar.actionAlignCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        navigationBar.actionAlignRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        navigationBar.actionBlockquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        navigationBar.actionInsertBullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        navigationBar.actionInsertNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        navigationBar.actionInsertAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3");
            }
        });

        navigationBar.actionInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                        "dachshund", 320);
            }
        });

        navigationBar.actionInsertVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
            }
        });

        navigationBar.actionInsertCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        navigationBar.actionOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textRecognitionFragment = new TextRecognitionFragment(fm);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, textRecognitionFragment);
                ft.commit();
            }
        });

        navigationBar.actionSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String test;
                test = mEditor.getHtml();
                //TODO Create saving file mechanics
                mEditor.setHtml(test);
            }
        });

        return binding.getRoot();
    }
}