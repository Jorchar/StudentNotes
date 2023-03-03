package com.jkucharski.studentnotes.ui.editor;

import static android.app.Activity.RESULT_OK;
import static com.jkucharski.studentnotes.utils.Const.CAMERA_PRM_CODE;
import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jkucharski.studentnotes.R;
import com.jkucharski.studentnotes.databinding.EditorNavigationBarBinding;
import com.jkucharski.studentnotes.databinding.FragmentNoteEditorBinding;
import com.jkucharski.studentnotes.utils.Const;

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
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference ref;
    ActivityResultLauncher<Intent> activityResultLauncher;
    File photoFile = null;
    Uri photoUri;

    public NoteEditorFragment(FragmentManager fm, String firebaseReference) {
        this.fm = fm;
        this.firebaseReference = firebaseReference;
        ref = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference(firebaseReference)
                .child("content");
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",    /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void takePictureIntent() throws IOException {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PRM_CODE);
        }else{
            photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(getActivity(), "com.jkucharski.studentnotes.provider", photoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(FirebaseAuth.getInstance().getUid() + "/images/");

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                storageReference.child(photoFile.getName()).putFile(photoUri)
                        .addOnSuccessListener(taskSnapshot -> storageReference.child(photoFile.getName()).getDownloadUrl()
                                .addOnSuccessListener(uri -> mEditor.insertImage(uri.toString(), "picture", 350, 400)));
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
        mEditor.setEditorFontColor(Color.BLACK);
        //TODO editable note background
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        navigationBar = binding.navigationBar;

        Spinner fontSizeSpinner = navigationBar.fontSizeNumber;
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(getContext(),R.array.font_size_array, android.R.layout.simple_spinner_item);
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(fontSizeAdapter);
        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String number = adapterView.getItemAtPosition(i).toString().substring(0, 2);
                mEditor.setEditorFontSize(Integer.parseInt(number));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
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
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Spinner fontColorSpinner = navigationBar.fontColor;
        ColorSpinnerAdapter fontColorAdapter = new ColorSpinnerAdapter(getContext(), Const.getColorList());
        fontColorSpinner.setAdapter(fontColorAdapter);
        fontColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEditor.setTextColor((Integer)fontColorAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner bgColorSpinner = navigationBar.bgColor;
        ColorSpinnerAdapter bgColorAdapter = new ColorSpinnerAdapter(getContext(), Const.getColorList());
        bgColorSpinner.setAdapter(bgColorAdapter);
        bgColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEditor.setBackgroundColor((Integer)bgColorAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        navigationBar.actionAlignLeft.setOnClickListener(v -> mEditor.setAlignLeft());

        navigationBar.actionAlignCenter.setOnClickListener(v -> mEditor.setAlignCenter());

        navigationBar.actionAlignRight.setOnClickListener(v -> mEditor.setAlignRight());

        navigationBar.actionBlockquote.setOnClickListener(v -> mEditor.setBlockquote());

        navigationBar.actionInsertBullets.setOnClickListener(v -> mEditor.setBullets());

        navigationBar.actionInsertNumbers.setOnClickListener(v -> mEditor.setNumbers());

        navigationBar.actionInsertAudio.setOnClickListener(v -> mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"));



        navigationBar.actionInsertImage.setOnClickListener(v -> {
            try {
                takePictureIntent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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