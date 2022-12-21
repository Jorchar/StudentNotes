package com.jkucharski.studentnotes;

import static com.jkucharski.studentnotes.utils.Const.FIREBASE_DATABASE_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectVH> {

    private List<SubjectDC> subjectDCList = new ArrayList<>();
    String firebaseReference;
    FragmentManager fm;

    SubjectAdapter(FragmentManager fm, String firebaseReference){
        this.fm = fm;
        this.firebaseReference = firebaseReference;
    }

    public void setSubjects(List<SubjectDC> subjectDCList){
        this.subjectDCList = subjectDCList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater subjectLI = LayoutInflater.from(parent.getContext());
        View view = subjectLI.inflate(R.layout.subject_cardview, parent, false);
        return new SubjectVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectVH holder, int position) {
        SubjectDC subjectDC = subjectDCList.get(position);
        holder.subjectNameTV.setText(subjectDC.getName());
        holder.subjectDescTV.setText(subjectDC.getDescription());

        holder.subjectBackground.setOnClickListener(view -> {
            NotesListFragment notesListFragment = new NotesListFragment(fm, subjectDC.getId());
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, notesListFragment).addToBackStack(null);
            ft.commit();
        });

        holder.deleteButton.setOnClickListener(view -> {
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                    .getReference(firebaseReference + "/" + subjectDC.getId()).child("active").setValue(false);
            subjectDCList.remove(position);
            notifyItemRemoved(position);
        });

        holder.editButton.setOnClickListener(view -> {
            EditSubjectFragment editSubjectFragment = new EditSubjectFragment(fm, subjectDC, firebaseReference + "/" + subjectDC.getId());
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.MainLayout, editSubjectFragment).addToBackStack(null);
            ft.commit();
        });

        holder.expandButton.setOnClickListener(view -> {
            if (holder.hiddenSubjectView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.subjectCardView,
                        new AutoTransition());
                holder.hiddenSubjectView.setVisibility(View.GONE);
                holder.expandButton.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }

            else {

                TransitionManager.beginDelayedTransition(holder.subjectCardView,
                        new AutoTransition());
                holder.hiddenSubjectView.setVisibility(View.VISIBLE);
                holder.expandButton.setImageResource(R.drawable.ic_baseline_expand_less_24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectDCList.size();
    }

    public class SubjectVH extends RecyclerView.ViewHolder{
        TextView subjectNameTV;
        TextView subjectDescTV;
        ImageButton expandButton;
        ImageButton deleteButton;
        ImageButton editButton;
        LinearLayout hiddenSubjectView;
        CardView subjectCardView;
        ImageView subjectBackground;

        public SubjectVH(@NonNull View itemView) {
            super(itemView);
            subjectNameTV = itemView.findViewById(R.id.subjectNameTV);
            subjectDescTV = itemView.findViewById(R.id.subjectDescriptionTV);
            expandButton = itemView.findViewById(R.id.expandButton);
            hiddenSubjectView = itemView.findViewById(R.id.hiddenSubjectView);
            subjectCardView = itemView.findViewById(R.id.subjectCardView);
            subjectBackground = itemView.findViewById(R.id.subjectImageBG);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
