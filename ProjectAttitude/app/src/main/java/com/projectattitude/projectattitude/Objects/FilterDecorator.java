package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;

/**
 * Created by henrywei on 3/23/17.
 */

public abstract class FilterDecorator {
    private String filterType;
    private String filterParameter;
    private FilterDecorator enclosedDecorator;

    public FilterDecorator(String filterParameter){
        this.filterParameter = filterParameter;
    }

    public FilterDecorator(String filterParameter, FilterDecorator enclosedDecorator){
        this(filterParameter);
        this.enclosedDecorator = enclosedDecorator;
    }

    public String getFilterParameter() {
        return filterParameter;
    }

    public void setFilterParameter(String filterParameter) {
        this.filterParameter = filterParameter;
    }

    public void setFilterType(String filterType) {
        //Can only set filterType once
        if(this.filterType == null){
            this.filterType = filterType;
        }

    }

    public String getFilterType() {
        return filterType;
    }

    public void filter(ArrayList<Mood> moodList){
        if(enclosedDecorator != null){
            enclosedDecorator.filter(moodList);
        }
        specificFilter(moodList);
    }

    public FilterDecorator getEnclosedDecorator() {
        return enclosedDecorator;
    }

    public void setEnclosedDecorator(FilterDecorator enclosedDecorator) {
        this.enclosedDecorator = enclosedDecorator;
    }

    //specificFilter must filter moodList depending on its filterType and filterParameter
    public abstract void specificFilter(ArrayList<Mood> moodList);

}
