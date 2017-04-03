/*
 * MIT License
 *
 * Copyright (c) 2017 CMPUT301W17T12
 * Authors rsauveho vuk bfleyshe henrywei cs3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.projectattitude.projectattitude.Objects;

import android.graphics.Bitmap;

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
    private ArrayList<String> followList = new ArrayList<String>();    //arrayList of people that are following this user
    private ArrayList<String> followedList = new ArrayList<String>();    //arrayList of people that this user is following
    private ArrayList<FollowRequest> requests = new ArrayList<FollowRequest>();
    private ArrayList<Mood> moods;

    private String id;
    private String photo;

    /**
     * Creates the user object as well as initiating an arrayList of mood objects.
     */
    public User(){
        this.moods = new ArrayList<Mood>();
    }

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
    public void addFollow(String followName) {
        followList.add(followName);
    }

    public void removeFollow(String followName){
        followList.remove(followName);
    }

    /**
     * Adds a followed to the user.
     * @param followedName followedName
     */
    public void addFollowed(String followedName) {
        followedList.add(followedName);
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
     * Returns the first mood
     * @return mood
     */
    public Mood getFirstMood(){
        if(moods.size() != 0){
            return moods.get(0);
        }
        else{
            return null;
        }
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

    /**
     * Gets the user photo
     * @return String photo
     */
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<FollowRequest> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<FollowRequest> requests) {
        this.requests = requests;
    }
}
