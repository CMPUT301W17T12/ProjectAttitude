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
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Objects.Mood;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by henry on 3/8/2017.
 * These tests check if sorting works properly within the MainActivity.
 * @see MainActivity
 */

public class SortTest extends ActivityInstrumentationTestCase2 {

    public SortTest() {
        super(MainActivity.class);
    }

    public void testVoidSort(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();

        //Test list with no elements
        controller.sortList(list, "Sort"); //Sort an empty list
        assertNotNull(list);

        Mood m1 = new Mood();
        m1.setMoodDate(new Date(2000,0,0));

        //Test list with 1 element
        list.add(m1);
        ArrayList<Mood> newList = (ArrayList<Mood>)list.clone();
        assertNotSame(list, newList); //Make sure they are different objects
        controller.sortList(list, ""); //Sort without an order
        assertEquals(list, newList); //Make sure the list is still the same afterwards
    }

    //Test to test if sort works + basic functionality
    public void testSort(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();

        Mood m1 = new Mood();
        m1.setMoodDate(new Date(2000,0,0));
        Mood m2 = new Mood();
        m1.setMoodDate(new Date(2002,0,0));
        Mood m3 = new Mood();
        m1.setMoodDate(new Date(2001,0,0));

        list.add(m1);

        //Test sorting 1 element
        controller.sortList(list, "Sort");
        assertEquals(list.get(0), m1);

        //Test sort
        list.add(m2);
        list.add(m3);
        controller.sortList(list, "Sort");
        assertEquals(list.get(0), m1);
        assertEquals(list.get(1), m3);
        assertEquals(list.get(2), m2);

    }

    //Test to test if reverseSort works
    public void testReverseSort(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();

        Mood m1 = new Mood();
        m1.setMoodDate(new Date(2000,0,0));
        Mood m2 = new Mood();
        m1.setMoodDate(new Date(2002,0,0));
        Mood m3 = new Mood();
        m1.setMoodDate(new Date(2001,0,0));

        list.add(m1);
        list.add(m2);
        list.add(m3);

        //Test reverseSort
        controller.sortList(list, "Reverse Sort");
        assertEquals(list.get(0), m2);
        assertEquals(list.get(1), m3);
        assertEquals(list.get(2), m1);
    }
}
