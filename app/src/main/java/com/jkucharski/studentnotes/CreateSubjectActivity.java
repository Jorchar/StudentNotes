package com.jkucharski.studentnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateSubjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
    }

    public void CreateSubject(View view) {
        EditText subjectNameET = findViewById(R.id.subjectNameInput);
        EditText subjectDescET = findViewById(R.id.subjectDescriptionInput);
        String subjectName = subjectNameET.getText().toString();
        String subjectDesc = subjectDescET.getText().toString();

        SubjectAdapter subjectAdapter = new SubjectAdapter();
        subjectAdapter.addSubject(subjectName, subjectDesc);

        super.onBackPressed();
    }
}