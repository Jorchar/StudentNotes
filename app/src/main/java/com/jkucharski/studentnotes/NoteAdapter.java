package com.jkucharski.studentnotes;

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

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {

    static List<NoteDC> noteDCList = new ArrayList<>();
    FragmentManager fm;
    String firebaseReference;

    NoteAdapter(FragmentManager fm, String firebaseReference){
        this.fm = fm;
        this.firebaseReference = firebaseReference;
    }

    public void setNote(List<NoteDC> noteDCList){
        this.noteDCList = noteDCList;
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
        holder.noteNameTV.setText(noteDC.getName());
        holder.noteBackground.setOnClickListener(view -> {
            NoteEditorFragment noteEditorFragment = new NoteEditorFragment(fm, firebaseReference+"/"+noteDC.getId());
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, noteEditorFragment).addToBackStack(null);
            ft.commit();
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
}
