package com.projectattitude.projectattitude.Objects;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 */

public class MoodList implements Serializable, Cloneable {

    //fix for submission
//    private ArrayList<Mood> records;

    private ArrayList<Mood> moods;

    public MoodList(){
        moods = new ArrayList<Mood>();
    }

    public MoodList(ArrayList<Mood> tempList){
        moods = tempList;
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
        moods.remove(mood);
    }

    public void deleteMood(Integer i) {
        moods.remove(i);
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

    public void setMoodList(MoodList ml) {
        moods = ml.getMoodList();
    }

    @Override
    public MoodList clone(){
        //super.clone()
        //Taken from http://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object-in-java
        //March 9, 2017
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //Store moodList
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(moods);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            //Restore moodList
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            ArrayList<Mood> returnList = (ArrayList<Mood>) new ObjectInputStream(bais).readObject();
            return new MoodList(returnList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("Error", "Failed to clone moodList from given list.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "Failed to clone moodList from given list.");
            return null;
        }
    }
}
