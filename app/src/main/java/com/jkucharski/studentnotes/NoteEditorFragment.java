package com.jkucharski.studentnotes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jkucharski.studentnotes.databinding.EditorNavigationBarBinding;
import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    FragmentTransaction ft;
    RoomDB database;
    TextRecognitionFragment textRecognitionFragment;
    EditorNavigationBarBinding navigationBar;
    private RichEditor mEditor;
    NoteDC noteDC;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    NoteEditorFragment(FragmentManager fm, NoteDC noteDC) {
        this.fm = fm;
        this.noteDC = noteDC;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //TODO get bitmap on RichEditBox
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = RoomDB.getInstance(getContext());

        binding = FragmentNoteEditorBinding.inflate(inflater, container, false);

        mEditor = (RichEditor) binding.textEditor;
        mEditor.setEditorFontSize(16);
        mEditor.setEditorFontColor(Color.BLACK);
        //TODO editable note background
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        mEditor.setHtml(noteDC.getContent());

        navigationBar = binding.navigationBar;

        Spinner fontSizeSpinner = navigationBar.fontSizeNumber;
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.font_size_array, android.R.layout.simple_spinner_item);
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(fontSizeAdapter);
        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String number = adapterView.getItemAtPosition(i).toString().substring(0, 2);
                mEditor.setEditorFontSize(Integer.parseInt(number));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner headingSpinner = navigationBar.headingNumber;
        ArrayAdapter<CharSequence> headingAdapter = ArrayAdapter.createFromResource(getContext(), R.array.heading_array, android.R.layout.simple_spinner_item);
        headingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        headingSpinner.setAdapter(headingAdapter);
        headingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String heading = adapterView.getItemAtPosition(i).toString().substring(8);
                    mEditor.setHeading(Integer.parseInt(heading));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                dispatchTakePictureIntent();
                //mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg", "dachshund", 320);
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
                ft.replace(R.id.MainLayout, textRecognitionFragment).addToBackStack(null);
                ft.commit();
            }
        });

        navigationBar.actionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.noteDao().updateContent(noteDC.getID(), mEditor.getHtml());
                noteDC.setContent(mEditor.getHtml());
            }
        });

        return binding.getRoot();
    }
}