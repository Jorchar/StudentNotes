package com.jkucharski.studentnotes;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileOutputStream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.noteNameTV.setText(noteDC.get(position).name);
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
    public void addNote(String name){
        noteDC.add(new NoteDC(name));

        notifyItemInserted(noteDC.size()-1);
    }

    public void createNoteDocument(String name){
        XWPFDocument document = new XWPFDocument();
        try{
            FileOutputStream outputStream = new FileOutputStream(name+".docx");
            document.write(outputStream);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
