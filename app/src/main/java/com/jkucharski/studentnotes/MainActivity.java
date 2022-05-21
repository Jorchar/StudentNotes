package com.jkucharski.studentnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSubjectRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpSubjectRecyclerView();
    }

    private void setUpSubjectRecyclerView() {
        SubjectAdapter subjectAdapter = new SubjectAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSubject);
        recyclerView.setAdapter(subjectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void CreateNewSubject(View view) {
        Intent intent = new Intent(this, CreateSubjectActivity.class);
        startActivity(intent);
    }
}