package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by henrywei on 3/23/17.
 * @see FilterDecorator
 */

public class FilterTimeDecorator extends FilterDecorator {

    public FilterTimeDecorator( String filterParameter){
        super(filterParameter);
        this.setFilterType("Time");
    }

    public FilterTimeDecorator(String filterParameter, FilterDecorator enclosedDecorator){
        super(filterParameter, enclosedDecorator);
    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' date
     * Removes moods from moodList that don't have times less than what's specified in the timeParameter
     * @param moodList - moods to be filtered
     */
    public void specificFilter(ArrayList<Mood> moodList){
        long timeParameter;
        switch(this.getFilterParameter()){
            case "Day":
                timeParameter = (long)8.64e+7; //1 day's worth of milliseconds
                break;

            case "Week":
                timeParameter = (long)6.048e+8; //1 week's worth of milliseconds
                break;

            case "Month":
                timeParameter = (long)2.628e+9; //1 month's worth of milliseconds approximately
                break;

            case "Year":
                timeParameter = (long)3.154e+10; //1 year's worth of milliseconds approximately
                break;

            default: //Default filter by day
                timeParameter = (long)8.64e+7;
                break;

        }

        long currentTime = new Date().getTime();

        for(int i = moodList.size() - 1; i >= 0; --i) { //Go backwards on list, to work around moodList.remove()
            //If time is greater than timeParameter, remove it from moodList
            long moodTime = ((Date)moodList.get(i).getMoodDate()).getTime();
            if(Math.abs(currentTime - moodTime) > timeParameter){
                moodList.remove(i);
            }
        }
    }
}
