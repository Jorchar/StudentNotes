package com.jkucharski.studentnotes.ui.editor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jkucharski.studentnotes.R;

import java.util.List;

public class ColorSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> colorList;

    public ColorSpinnerAdapter(Context context, List<Integer> colorList){
        this.context = context;
        this.colorList = colorList;
    }

    @Override
    public int getCount() {
        if(colorList != null){
            return colorList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return colorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_font_color, parent, false);

        ImageView image = rootView.findViewById(R.id.fontColorImage);

        image.setBackgroundColor(colorList.get(position));

        return rootView;
    }
}
