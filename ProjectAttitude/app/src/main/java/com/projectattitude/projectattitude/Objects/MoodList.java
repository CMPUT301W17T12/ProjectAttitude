package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 */

public class MoodList {

    //fix for submission
//    private ArrayList<Mood> records;

    private ArrayList<Mood> moods;

    public MoodList(){
        moods = new ArrayList<Mood>();
    }

    public void addMood(Mood mood) {
        moods.add(mood);
    }

    public boolean hasMood(Mood mood) {
        return false;
    }

    public int getCount() {
        return moods.size();
    }

    public void deleteMood(Mood mood) {
    }

    public Mood getMood(int i) {
       return moods.get(i);
    }

    //fix for submission
//    public ArrayList<Mood> getMoodList() {
//        return records;
//    }

    public ArrayList<Mood> getMoodList() {
        return moods;
    }
}
