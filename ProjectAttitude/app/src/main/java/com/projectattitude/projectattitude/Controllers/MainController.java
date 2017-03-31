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

package com.projectattitude.projectattitude.Controllers;

import android.util.Log;

import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.Objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Chris on 2/24/2017.
 */

public class MainController {

    /**
     * Sorts an array list holding moods depending on its date.
     * @param moodList: Arraylist containing moods.
     * @param sortOrder: "Sort" if sorting in normal order, "Reverse Sort" if sorting in reverse order.
     *
     * Postcondition: moodList is sorted, but arrayAdapter needs to be notified.
     */
    public static void sortList(ArrayList<Mood> moodList, String sortOrder){
        //Taken from http://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
        //Date: 3/6/2017

        if(sortOrder.equals("Sort")){//Sort
            Collections.sort(moodList, new Comparator<Mood>(){
                @Override
                public int compare(Mood mood1, Mood mood2) {
                    long tempValue = -(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
                    if (tempValue < 0){ return -1; }
                    if (tempValue > 0){ return 1; }
                    return 0; //else, return 0
                }
            });
        }else if (sortOrder.equals("Reverse Sort")){//Reverse Sort
            Collections.sort(moodList, new Comparator<Mood>(){
                @Override
                public int compare(Mood mood1, Mood mood2) {
                    long tempValue = ((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime();
                    if (tempValue < 0){ return -1; }
                    if (tempValue > 0){ return 1; }
                    return 0; //else, return 0
                }
            });
        }
        //else, don't sort or anything because invalid input.

    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' date
     * Removes moods from moodList that don't have times less than what's specified in the timeParameter
     * @param moodList - moods to be filtered
     * @param timeParameter - time in milliseconds to filter by
     */
    public void filterListByTime(ArrayList<Mood> moodList, long timeParameter){
        long currentTime = new Date().getTime();
        Log.d("Error", "Time Parameter: "+Long.toString(timeParameter));
        Log.d("Error", "MoodList Size: "+Integer.toString(moodList.size()));

        for(int i = moodList.size() - 1; i >= 0; --i) { //Go backwards on list, to work around moodList.remove()
            Log.d("Error", moodList.get(i).getEmotionState());
            //If time is greater than timeParameter, remove it from moodList
            long moodTime = ((Date)moodList.get(i).getMoodDate()).getTime();
            Log.d("Error", "This Mood's Time: "+Long.toString(currentTime - moodTime));
            if(Math.abs(currentTime - moodTime) > timeParameter){
                moodList.remove(i);
            }
        }

    }

}
