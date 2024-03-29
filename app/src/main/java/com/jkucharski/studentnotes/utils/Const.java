package com.jkucharski.studentnotes.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Const {
    public static final String FIREBASE_DATABASE_URL = "https://studentnotes-222a3-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final int CAMERA_PRM_CODE = 101;

    public static List<Integer> getColorList(){
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#FFFFFF"));
        colorList.add(Color.parseColor("#000000"));
        colorList.add(Color.parseColor("#FF0000"));
        colorList.add(Color.parseColor("#0000FF"));
        colorList.add(Color.parseColor("#00FF00"));

        return colorList;
    }

    public static List<Integer> getCardColorList(){
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#339933"));
        colorList.add(Color.parseColor("#D9CCD9"));
        colorList.add(Color.parseColor("#CCD9D9"));
        colorList.add(Color.parseColor("#D2D9CC"));
        colorList.add(Color.parseColor("#D9D2CC"));
        return colorList;
    }
}