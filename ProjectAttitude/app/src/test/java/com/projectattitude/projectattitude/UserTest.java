package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.User;

import java.util.ArrayList;

/**
 * Created by Boris on 25/02/2017.
 * These tests check if the user object works properly.
 */

public class UserTest extends ActivityInstrumentationTestCase2{

    public UserTest() {
        super(MainActivity.class);
    }

    public void testGetUserName(){
        User user = new User("UserName");
        assertEquals("UserName", user.getUserName());
    }

    public void testSetUserName(){
        User user = new User("UserName");
        user.setUserName("newUserName");
        assertEquals("newUserName", user.getUserName());
    }

    public void testGetFollowList(){
        User user = new User("UserName");
        ArrayList<String> testArray = new ArrayList<String>();

        assertEquals(null, user.getFollowList());
        user.addFollow("followName");
        testArray.add("followName");
        assertEquals(user.getFollowList(), testArray);
    }

    public void testGetFollowedList(){
        User user = new User("UserName");
        ArrayList<String> testArray = new ArrayList<String>();

        assertEquals(null, user.getFollowedList());
        user.addFollowed("followedName");
        testArray.add("followedName");
        assertEquals(user.getFollowedList(), testArray);
    }

    public void testAddFollow(){
        User user = new User("UserName");

        user.addFollow("followName");
        assertTrue(user.getFollowList() != null);
    }

    public void testAddFollowed(){
        User user = new User("UserName");

        user.addFollowed("followedName");
        assertTrue(user.getFollowedList() != null);
    }

    public void testSetID(){
        User user = new User("UserName");

        user.setId("1234");
        assertEquals(user.getId(), "1234");
    }

    public void testGetID(){
        User user = new User("UserName");

        user.setId("1234");
        assertEquals(user.getId(), "1234");
    }

}