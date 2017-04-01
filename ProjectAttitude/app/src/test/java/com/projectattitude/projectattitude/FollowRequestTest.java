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
import com.projectattitude.projectattitude.Objects.FollowRequest;

/**
 * Created by Boris on 25/02/2017.
 * These tests check if the request object works properly.
 */

public class FollowRequestTest extends ActivityInstrumentationTestCase2 {

    public FollowRequestTest() {
        super(MainActivity.class);
    }

    /**
     * Tests if requester is obtained properly.
     */
    public void testGetRequester() {
        FollowRequest request = new FollowRequest("requester", "requestee");
        assertEquals("requester", request.getRequester());
    }

    /**
     * Tests if requestee is obtained properly.
     */
    public void testGetRequestee() {
        FollowRequest request = new FollowRequest("requester", "requestee");
        assertEquals("requestee", request.getRequestee());
    }

    /**
     * Tests if ID is set properly.
     */
    public void testSetID() {
        FollowRequest request = new FollowRequest("requester", "requestee");
        request.setID("1234");
        assertEquals("1234", request.getID());
    }

    /**
     * Tests if ID is obtained properly.
     */
    public void testGetID() {
        FollowRequest request = new FollowRequest("requester", "requestee");
        request.setID("1234");
        assertEquals("1234", request.getID());
    }
}