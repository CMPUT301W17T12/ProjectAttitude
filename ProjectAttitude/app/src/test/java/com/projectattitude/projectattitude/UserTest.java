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

    /**
     * Creates the userTest.
     */
    public UserTest() {
        super(MainActivity.class);
    }

    /**
     * Tests getting the userName.
     */
    public void testGetUserName(){
        User user = new User();
        user.setUserName("UserName");
        assertEquals("UserName", user.getUserName());
    }

    /**
     * Tests setting the userName.
     */
    public void testSetUserName(){
        User user = new User();
        user.setUserName("newUserName");
        assertEquals("newUserName", user.getUserName());
    }

    /**
     * Tests getting the follow list.
     */
    public void testGetFollowList(){
        User user = new User();
        user.setUserName("UserName");
        ArrayList<String> testArray = new ArrayList<String>();

        assertEquals(null, user.getFollowList());
        user.addFollow("followName");
        testArray.add("followName");
        assertEquals(user.getFollowList(), testArray);
    }

    /**
     * Tests getting the followed list.
     */
    public void testGetFollowedList(){
        User user = new User();
        user.setUserName("UserName");
        ArrayList<String> testArray = new ArrayList<String>();

        assertEquals(null, user.getFollowedList());
        user.addFollowed("followedName");
        testArray.add("followedName");
        assertEquals(user.getFollowedList(), testArray);
    }

    /**
     * Tests adding a follow.
     */
    public void testAddFollow(){
        User user = new User();
        user.setUserName("UserName");

        user.addFollow("followName");
        assertTrue(user.getFollowList() != null);
    }

    /**
     * Tests adding a followed.
     */
    public void testAddFollowed(){
        User user = new User();
        user.setUserName("UserName");

        user.addFollowed("followedName");
        assertTrue(user.getFollowedList() != null);
    }

    /**
     * Tests setting an ID.
     */
    public void testSetID(){
        User user = new User();
        user.setUserName("UserName");

        user.setId("1234");
        assertEquals(user.getId(), "1234");
    }

    /**
     * Tests getting an ID.
     */
    public void testGetID(){
        User user = new User();
        user.setUserName("UserName");

        user.setId("1234");
        assertEquals(user.getId(), "1234");
    }

}