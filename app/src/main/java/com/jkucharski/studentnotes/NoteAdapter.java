package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {

    static List<NoteDC> noteDCList = new ArrayList<>();
    FragmentManager fm;
    String firebaseReference;
    Context context;

    NoteAdapter(FragmentManager fm, String firebaseReference, Context context){
        this.fm = fm;
        this.firebaseReference = firebaseReference;
        this.context = context;
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

        holder.noteEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Type in note's title");
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                String noteName = input.getText().toString();
                FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                        .getReference(firebaseReference + "/" + noteDC.getId())
                        .child("name").setValue(noteName);

                NotesListFragment notesListFragment = new NotesListFragment(fm, noteDC.getSubject());
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainLayout, notesListFragment);
                ft.commit();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        holder.noteDelete.setOnClickListener(v -> {
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                    .getReference(firebaseReference + "/" + noteDC.getId()).child("active").setValue(false);
            noteDCList.remove(position);
            notifyItemRemoved(position);
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
        ImageButton noteEdit;
        ImageButton noteDelete;

        public NoteVH(@NonNull View itemView) {
            super(itemView);
            noteNameTV = itemView.findViewById(R.id.noteNameTV);
            noteCardView = itemView.findViewById(R.id.noteCardView);
            noteBackground = itemView.findViewById(R.id.noteImageBG);
            noteEdit = itemView.findViewById(R.id.editButton);
            noteDelete = itemView.findViewById(R.id.deleteButton);
        }
    }
}
