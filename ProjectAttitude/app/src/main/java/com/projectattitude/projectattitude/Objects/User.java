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

//    public User(String s){
//    }

    public User(){
        this.moods = new ArrayList<Mood>();
    }

//    public void makeMoodList(){
//        this.moods = new ArrayList<Mood>();
//    }

    public ArrayList<Mood> getMoodList(){
        return moods;
    }

    public void setMoodList(ArrayList<Mood> moodList){
        this.moods = moodList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addFollow(String followName) {
    }

    public void addFollowed(String followName) {
    }

    public ArrayList<String> getFollowList() {
        return followList;
    }

    public ArrayList<String> getFollowedList() {
        return followedList;
    }

    public Object getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
