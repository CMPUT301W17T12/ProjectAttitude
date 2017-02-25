package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.User;

/**
 * Created by Boris on 25/02/2017.
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



}