package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by henrywei on 3/23/17.
 * @see FilterDecorator
 */

public class FilterEmotionDecorator extends FilterDecorator {

    public FilterEmotionDecorator(String filterParameter){
        super(filterParameter);
        filterType = "Emotion";
    }

    public FilterEmotionDecorator(String filterParameter, FilterDecorator enclosedDecorator){
        super(filterParameter, enclosedDecorator);
    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' emotional state
     * Removes moods from moodList that don't have the correct emotional state
     * @param moodList - moods to be filtered
     */
    public void specificFilter(ArrayList<Mood> moodList){
        for(int i = moodList.size() - 1; i >= 0; --i){
            if(!(moodList.get(i).getEmotionState().equals(filterParameter))){ //If mood's emotion is not equal to emotion Parameter
                moodList.remove(i);
            }
        }
    }
}
