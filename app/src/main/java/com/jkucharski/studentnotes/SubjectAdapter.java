package com.jkucharski.studentnotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectVH> {

    static List<SubjectDC> subjectNames = new ArrayList<>();

    @NonNull
    @Override
    public SubjectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater subjectLI = LayoutInflater.from(parent.getContext());
        View view = subjectLI.inflate(R.layout.subject_cardview, parent, false);
        return new SubjectVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectVH holder, int position) {
        SubjectDC subjectName = subjectNames.get(position);
        holder.subjectNameTV.setText(subjectName.name);
    }

    @Override
    public int getItemCount() {
        if(subjectNames == null)
            return 0;
        return subjectNames.size();
    }

    public class SubjectVH extends RecyclerView.ViewHolder{
        String subjectName;
        TextView subjectNameTV;

        public SubjectVH(@NonNull View itemView) {
            super(itemView);
            subjectNameTV = itemView.findViewById(R.id.subjectNameTV);

        }
    }
    public void addSubject(String name, String description){
        subjectNames.add(new SubjectDC(name, description));
        notifyItemInserted(subjectNames.size()-1);
    }
}
