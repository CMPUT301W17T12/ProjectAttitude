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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by henry on 3/8/2017.
 * This test checks if the filtering by date process works properly in the mainActivity.
 * @see MainActivity
 */

public class FilterTest extends ActivityInstrumentationTestCase2 {

    public FilterTest() {
        super(MainActivity.class);
    }

    //Tests if sort works + basic functionality
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
        //controller.filterListByTime(list, (long)8.64e+7);

        assertEquals(list.get(0),m1);

    }

    //Test if monthFilter works
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
        //controller.filterListByTime(list, (long)2.628e+9);

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
        //TODO: Redesign filter test
        //controller.filterListByTime(list, (long)3.154e+10);

        assertEquals(list.get(0),m1);
    }
}
