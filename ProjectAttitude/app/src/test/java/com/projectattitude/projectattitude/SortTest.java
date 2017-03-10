package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Objects.Mood;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by henry on 3/8/2017.
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
