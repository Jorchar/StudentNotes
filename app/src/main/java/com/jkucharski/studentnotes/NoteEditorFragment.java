package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jkucharski.studentnotes.databinding.EditorNavigationBarBinding;
import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    TextRecognitionFragment textRecognitionFragment;
    EditorNavigationBarBinding navigationBar;
    private RichEditor mEditor;
    String firebaseReference;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DatabaseReference ref;

    NoteEditorFragment(FragmentManager fm, String firebaseReference) {
        this.fm = fm;
        this.firebaseReference = firebaseReference;
        ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                .child("content");
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

            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] imgBytes = byteArray.toByteArray();
            String test = "data:image/jpeg;base64,";
            test += Base64.encodeBase64String(imgBytes);
            mEditor.insertImage(test, "", 350, 350);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteEditorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mEditor.setHtml(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


        navigationBar.actionUndo.setOnClickListener(v -> mEditor.undo());

        navigationBar.actionRedo.setOnClickListener(v -> mEditor.redo());

        navigationBar.actionBold.setOnClickListener(v -> mEditor.setBold());

        navigationBar.actionItalic.setOnClickListener(v -> mEditor.setItalic());

        navigationBar.actionSubscript.setOnClickListener(v -> mEditor.setSubscript());

        navigationBar.actionSuperscript.setOnClickListener(v -> mEditor.setSuperscript());

        navigationBar.actionStrikethrough.setOnClickListener(v -> mEditor.setStrikeThrough());

        navigationBar.actionUnderline.setOnClickListener(v -> mEditor.setUnderline());

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

        navigationBar.actionAlignLeft.setOnClickListener(v -> mEditor.setAlignLeft());

        navigationBar.actionAlignCenter.setOnClickListener(v -> mEditor.setAlignCenter());

        navigationBar.actionAlignRight.setOnClickListener(v -> mEditor.setAlignRight());

        navigationBar.actionBlockquote.setOnClickListener(v -> mEditor.setBlockquote());

        navigationBar.actionInsertBullets.setOnClickListener(v -> mEditor.setBullets());

        navigationBar.actionInsertNumbers.setOnClickListener(v -> mEditor.setNumbers());

        navigationBar.actionInsertAudio.setOnClickListener(v -> mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"));



        navigationBar.actionInsertImage.setOnClickListener(v -> dispatchTakePictureIntent());

        navigationBar.actionInsertVideo.setOnClickListener(v -> mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360));

        navigationBar.actionInsertCheckbox.setOnClickListener(v -> mEditor.insertTodo());

        navigationBar.actionOcr.setOnClickListener(view1 -> {
            textRecognitionFragment = new TextRecognitionFragment(fm);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, textRecognitionFragment).addToBackStack(null);
            ft.commit();
        });

        navigationBar.actionSave.setOnClickListener(v -> ref.setValue(mEditor.getHtml()));
    }
}