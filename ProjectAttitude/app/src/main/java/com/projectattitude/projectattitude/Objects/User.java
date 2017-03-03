package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 
 The User class represents a personal account held by an individual for the purpose
 of interacting with the application. This class is used for handling information
 about each user as well as storing information for the follower and followed lists.
 */

public class User {

    private String userName;
    private ArrayList<String> followList;
    private ArrayList<String> followedList;

    public User(String s){
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
}
