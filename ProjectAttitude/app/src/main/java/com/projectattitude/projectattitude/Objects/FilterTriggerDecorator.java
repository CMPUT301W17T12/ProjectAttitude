package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by henrywei on 3/23/17.
 * @see FilterDecorator
 */

public class FilterTriggerDecorator extends FilterDecorator {

    public FilterTriggerDecorator(String filterParameter){
        super(filterParameter);
        this.setFilterType("Trigger");
    }

    public FilterTriggerDecorator(String filterParameter, FilterDecorator enclosedDecorator){
        super(filterParameter, enclosedDecorator);
    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' trigger
     * Removes moods from moodList that don't have the word in its trigger reason
     * @param moodList - moods to be filtered
     */
    public void specificFilter(ArrayList<Mood> moodList){
        String filterParameter = this.getFilterParameter().toLowerCase(); //Not case-sensitive searching
        for(int i = moodList.size() - 1; i >= 0; --i){
            //Split mood's trigger sentence into individual words, then try to find the word specified by reason
            String currentTrigger = moodList.get(i).getTrigger().toLowerCase();
            String triggerWords[] = currentTrigger.split(" ");
            boolean foundWord = false; //Flag for if word is found or not
            for(int k = 0; k < triggerWords.length; ++k){ //See if any of the mood's individual word matches what's in the search bar
                if(filterParameter.equals(triggerWords[k])){
                    foundWord = true;
                    break;
                }
            }
            if (!foundWord){
                moodList.remove(i);
            }
        }
    }
}
