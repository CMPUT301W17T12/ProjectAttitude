package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Objects.Mood;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by henry on 3/8/2017.
 * This test checks if the filtering process works properly in the mainActivity.
 * @see MainActivity
 */

public class FilterTest extends ActivityInstrumentationTestCase2 {

    public FilterTest() {
        super(MainActivity.class);
    }

    //Test to test if sort works + basic functionality
    public void testFilterByDay(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();
        Calendar calendar1 = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();

        Mood m1 = new Mood();
        m1.setMoodDate(calendar1.getTime());

        int tempDay = calendar2.DAY_OF_MONTH;
        if(calendar2.DAY_OF_MONTH == 1){
            tempDay = 28;
        }else{
            --tempDay;
        }

        calendar2.set(calendar2.YEAR,calendar2.MONTH,tempDay);

        Mood m2 = new Mood();
        m1.setMoodDate(calendar2.getTime());

        list.add(m2);
        list.add(m1);

        //Make sure moods are in the correct starting spot
        assertEquals(list.get(0), m2);
        assertEquals(list.get(1), m1);

        //Test filter by day
        controller.filterListByTime(list, (long)8.64e+7);

        assertEquals(list.get(0),m1);

    }

    //Test to test if reverseSort works
    public void testFilterByMonth(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();
        Calendar calendar1 = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();

        Mood m1 = new Mood();
        m1.setMoodDate(calendar1.getTime());

        int tempMonth = calendar2.MONTH;
        if(calendar2.MONTH == calendar2.JANUARY){
            tempMonth = calendar2.DECEMBER;
        }else{
            --tempMonth;
        }

        calendar2.set(calendar2.YEAR,tempMonth,calendar2.DAY_OF_MONTH);

        Mood m2 = new Mood();
        m1.setMoodDate(calendar2.getTime());

        list.add(m2);
        list.add(m1);

        //Make sure moods are in the correct starting spot
        assertEquals(list.get(0), m2);
        assertEquals(list.get(1), m1);

        //Test filter by month
        controller.filterListByTime(list, (long)2.628e+9);

        assertEquals(list.get(0),m1);
    }

    public void testFilterByYear(){
        ArrayList<Mood> list = new ArrayList<Mood>();
        MainController controller = new MainController();
        Calendar calendar1 = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();

        Mood m1 = new Mood();
        m1.setMoodDate(calendar1.getTime());

        int tempMonth = calendar2.MONTH;
        if(calendar2.MONTH == calendar2.JANUARY){
            tempMonth = calendar2.DECEMBER;
        }else{
            --tempMonth;
        }

        calendar2.set(calendar2.YEAR-1,tempMonth,calendar2.DAY_OF_MONTH);

        Mood m2 = new Mood();
        m1.setMoodDate(calendar2.getTime());

        list.add(m2);
        list.add(m1);

        //Make sure moods are in the correct starting spot
        assertEquals(list.get(0), m2);
        assertEquals(list.get(1), m1);

        //Test filter by year
        controller.filterListByTime(list, (long)3.154e+10);

        assertEquals(list.get(0),m1);
    }
}
