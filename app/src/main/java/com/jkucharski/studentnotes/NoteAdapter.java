package com.jkucharski.studentnotes;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {

    static List<NoteDC> noteDCList = new ArrayList<>();
    FragmentManager fm;
    private Activity context;
    private RoomDB database;

    NoteAdapter(FragmentManager fm, List<NoteDC> noteDCList, Activity context){
        this.fm = fm;
        this.noteDCList = noteDCList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapter.NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater noteLI = LayoutInflater.from(parent.getContext());
        View view = noteLI.inflate(R.layout.notes_cardview, parent, false);
        return new NoteVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        NoteDC noteDC = noteDCList.get(position);
        database = RoomDB.getInstance(context);
        holder.noteNameTV.setText(noteDC.getName());
        holder.noteBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteEditorFragment noteEditorFragment = new NoteEditorFragment(fm, noteDC);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, noteEditorFragment);
                ft.commit();
                //TODO polacz content z html edytora
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteDCList.size();
    }

    public class NoteVH extends RecyclerView.ViewHolder{
        TextView noteNameTV;
        CardView noteCardView;
        ImageView noteBackground;

        public NoteVH(@NonNull View itemView) {
            super(itemView);
            noteNameTV = itemView.findViewById(R.id.noteNameTV);
            noteCardView = itemView.findViewById(R.id.noteCardView);
            noteBackground = itemView.findViewById(R.id.noteImageBG);
        }
    }
    public void addNote(String name, Context context){
        String test = context.getExternalFilesDir("Notes").toString();
        createNoteDocument(name, context);
    }

    public void createNoteDocument(String name, Context context){
        File extStudentNoteFile = new File(context.getExternalFilesDir("Notes"), name +  ".html");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(extStudentNoteFile);
            fileOutputStream.write("lol".trim().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
