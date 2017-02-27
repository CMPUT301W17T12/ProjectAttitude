package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 */

public class MoodList {
    private int count = 0;

    //fix for submission
//    private ArrayList<Mood> records;

    private ArrayList<Mood> moods;

    public MoodList(){
    }

    public void addMood(Mood mood) {
    }

    public boolean hasMood(Mood mood) {
        return false;
    }

    public int getCount() {
        return count;
    }

    public void deleteMood(Mood mood) {
    }

    public Mood getMood(int i) {
        return null;
    }

    //fix for submission
//    public ArrayList<Mood> getMoodList() {
//        return records;
//    }

    public ArrayList<Mood> getMoodList() {
        return moods;
    }
}
