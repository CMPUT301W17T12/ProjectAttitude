package com.projectattitude.projectattitude.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 
 The User class represents a personal account held by an individual for the purpose
 of interacting with the application. This class is used for handling information
 about each user as well as storing information for the follower and followed lists.
 */

public class User implements Serializable {

    private String userName;    //name of user
    private ArrayList<String> followList;    //arrayList of people that are following this user
    private ArrayList<String> followedList;    //arrayList of people that this user is following
    private ArrayList<Mood> moods;

    private String id;

    //Initiates the user with a string
    public User(String s){
    }

    /**
     * Creates the user object as well as initiating an arrayList of mood objects.
     */
    public User(){
        this.moods = new ArrayList<Mood>();
    }

//    public void makeMoodList(){
//        this.moods = new ArrayList<Mood>();
//    }

    /**
     * Returns the moodList array.
     * @return moodList
     */
    public ArrayList<Mood> getMoodList(){
        return moods;
    }

    /**
     * Sets the moodList array.
     * @param moodList moodList
     */
    public void setMoodList(ArrayList<Mood> moodList){
        this.moods = moodList;
    }

    /**
     * Returns the userName.
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the userName.
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Adds a follow to the user.
     * @param followName followName
     */
    public void addFollow(String followName) {//TODO implement followers/following
    }

    /**
     * Adds a followed to the user.
     * @param followedName followedName
     */
    public void addFollowed(String followedName) {
    }

    /**
     * Returns the followList.
     * @return followList
     */
    public ArrayList<String> getFollowList() {
        return followList;
    }

    /**
     * Returns the followedList.
     * @return followedList
     */
    public ArrayList<String> getFollowedList() {
        return followedList;
    }

    /**
     * Returns the user ID
     * @return id
     */
    public Object getId() {
        return id;
    }

    /**
     * Sets the user ID
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }
}
