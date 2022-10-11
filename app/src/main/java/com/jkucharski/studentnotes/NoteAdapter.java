package com.jkucharski.studentnotes;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {

    static List<NoteDC> noteDC = new ArrayList<>();

    FragmentManager fm;

    NoteAdapter(FragmentManager fm){
        this.fm = fm;
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
        //holder.noteNameTV.setText(noteDC.get(position).name);
        holder.noteNameTV.setText(noteDC.get(position).name);
        holder.noteBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteEditorFragment noteEditorFragment = new NoteEditorFragment(fm, noteDC.get(holder.getAdapterPosition()).name);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, noteEditorFragment);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(noteDC == null)
            return 0;
        return noteDC.size();
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
        noteDC.add(new NoteDC(name));
        String test = context.getExternalFilesDir("Notes").toString();
        createNoteDocument(name, context);
        notifyItemInserted(noteDC.size()-1);
        //TODO compare noteDC list to existing files
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
