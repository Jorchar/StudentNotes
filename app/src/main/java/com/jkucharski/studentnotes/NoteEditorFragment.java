package com.jkucharski.studentnotes;

import static android.app.Activity.RESULT_OK;
import static com.jkucharski.studentnotes.utils.Const.CAMERA_PRM_CODE;
import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;

public class NoteEditorFragment extends Fragment {

    FragmentNoteEditorBinding binding;
    FragmentManager fm;
    TextRecognitionFragment textRecognitionFragment;
    EditorNavigationBarBinding navigationBar;
    private RichEditor mEditor;
    String firebaseReference;
    DatabaseReference ref;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri imageUri;
    String defineImageInHTML;

    NoteEditorFragment(FragmentManager fm, String firebaseReference) {
        this.fm = fm;
        this.firebaseReference = firebaseReference;
        ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                .child("content");
    }

    private void dispatchTakePictureIntent() {

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PRM_CODE);
        }else{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, timeStamp);
            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            activityResultLauncher.launch(takePictureIntent);
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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                        byte[] imgBytes = byteArray.toByteArray();
                        defineImageInHTML = "data:image/jpeg;base64,";
                        defineImageInHTML += Base64.encodeBase64String(imgBytes);
                        mEditor.insertImage(defineImageInHTML, "", bitmap.getWidth()/3, bitmap.getHeight()/3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

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