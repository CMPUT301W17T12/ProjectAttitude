package com.projectattitude.projectattitude.Controllers;

import android.content.Intent;

import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.Objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Chris on 2/24/2017.
 */

public class MainController {
    private User user;
    private ArrayList<User> followList; //The people the user follows?
    private ArrayList<User> followedList; //The people that follow the user?
    private MoodList myMoodList;
    private MoodList followedMoodList;
    private boolean displayingMyMoodList = true; //Which list is being displayed currently. 1 = myMoodList, 0 = followedMoodList

    /**
     * Gets the username of a given user
     * @param user the user you want the name of
     * @return a string of the users name
     * This function actually kinda works if we override toString!
     */
    public String getUsername(User user){
        return user.toString();
    }

    /**
     * I Guess this is where the bulk of the sorting gets done?
     * @param intent
     */
    public void sortList(Intent intent){
        //TODO: Test this function
        //TODO: Notify the list that it has changed
        //Taken from http://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
        //Date: 3/6/2017

        class dateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                return (int)(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
            }
        }

        class reverseDateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                return (int)-(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
            }
        }

        if(true){//Sort
            if(displayingMyMoodList) {//myMoodList being displayed
                Collections.sort(myMoodList.getMoodList(), new dateComparator());
            }else {//followedMoodList being displayed
                Collections.sort(followedMoodList.getMoodList(), new dateComparator());
            }
        }else{//Reverse Sort
            if(displayingMyMoodList) {//myMoodList being displayed
                Collections.sort(myMoodList.getMoodList(), new reverseDateComparator());
            }else {//followedMoodList being displayed
                Collections.sort(followedMoodList.getMoodList(), new reverseDateComparator());
            }
        }
    }

    /**
     * I Guess this is where the bulk of the filtering gets done?
     * @param intent
     */
    public void filterList(Intent intent){
        //need to filter
    }

    /**
     * This does sorting maybe?
     * @param array whats to be sorted?
     * @return a sorted array
     * Could just edit the list instead of returning a new one
     */
    private ArrayList returnSortedList(ArrayList array){
        return array;
    }
}
