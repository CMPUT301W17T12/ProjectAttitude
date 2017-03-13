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
 * This class holds a list of mood objects, manipulating the objects within the list as requested.
 */
public class MoodList implements Serializable, Cloneable {


    private ArrayList<Mood> moods;  // actual list object

    /**
     * Instantiates a new Mood list.
     */
    public MoodList(){
        moods = new ArrayList<Mood>();
    }

    /**
     * Instantiates a new Mood list.
     *
     * @param tempList the temp list
     */
    public MoodList(ArrayList<Mood> tempList){
        moods = tempList;
    }

    /**
     * Add mood.
     *
     * @param mood the mood
     */
    public void addMood(Mood mood) {
        moods.add(mood);
    }

    /**
     * Has mood boolean.
     *
     * @param mood the mood
     * @return boolean
     */
    public boolean hasMood(Mood mood) {
        return false;
    }


    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return moods.size();
    }

    /**
     * Delete mood.
     *
     * @param mood the mood
     */
    public void deleteMood(Mood mood) {
        moods.remove(mood);
    }

    /**
     * Delete mood.
     *
     * @param i i-th mood
     */
    public void deleteMood(Integer i) {
        moods.remove(i);
    }

    /**
     * Gets mood.
     *
     * @param i the
     * @return the mood
     */
    public Mood getMood(int i) {
       return moods.get(i);
    }


    /**
     * Gets mood list.
     *
     * @return the mood list
     */
    public ArrayList<Mood> getMoodList() {
        return moods;
    }

    /**
     * Sets mood list.
     *
     * @param ml the ml
     */
    public void setMoodList(MoodList ml) {
        moods = ml.getMoodList();
    }

    /**
     * Creates a copy of the list.
     * @return moodList object
     */
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
