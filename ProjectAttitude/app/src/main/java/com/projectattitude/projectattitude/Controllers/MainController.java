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
        //TODO: Replace with checking if is sort or reverseSort
        if(true){//Sort
            if(displayingMyMoodList) {//myMoodList being displayed
                //Collections.sort(myMoodList, new Comparator<Mood>(){
                    //public int compare(Mood mood1, Mood mood2){
                    //    return mood1.getMoodDate().getTime() - mood2.getMoodDate().getTime(); //TODO: Use when mood class is finished
                    //}
                //});
            }else {//followedMoodList being displayed
                //Collections.sort(followedMoodList, new Comparator<Mood>(){
                //public int compare(Mood mood1, Mood mood2){
                //    return mood1.getMoodDate().getTime() - mood2.getMoodDate().getTime();
                //}
                //});
            }
        }else{//Reverse Sort
            if(displayingMyMoodList) {//myMoodList being displayed
                //Collections.sort(myMoodList, new Comparator<Mood>(){
                //public int compare(Mood mood1, Mood mood2){
                //    return -(mood1.getMoodDate().getTime() - mood2.getMoodDate().getTime());
                //}
                //}); //Should check which list to sort through!
            }else {//followedMoodList being displayed
                //Collections.sort(followedMoodList, new Comparator<Mood>(){
                //public int compare(Mood mood1, Mood mood2){
                //    return -(mood1.getMoodDate().getTime() - mood2.getMoodDate().getTime());
                //}
                //}); //Should check which list to sort through!
            }
        }
    }

    /**
     * I Guess this is where the bulk of the filtering gets done?
     * @param intent
     */
    public void filterList(Intent intent){

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
