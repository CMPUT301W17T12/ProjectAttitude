package com.projectattitude.projectattitude.Objects;

import java.util.ArrayList;

/**
 * Created by henrywei on 3/23/17.
 */

/**
 * Decorator abstract class used for managing filtering.
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

    /**
     * Returns filter parameter.
     * @return filter parameter
     */
    public String getFilterParameter() {
        return filterParameter;
    }

    /**
     * Sets filter parameter.
     * @param filterParameter filter parameter
     */
    public void setFilterParameter(String filterParameter) {
        this.filterParameter = filterParameter;
    }

    /**
     * Sets filterType settings.
     * @param filterType filterType setting
     */
    public void setFilterType(String filterType) {
        //Can only set filterType once
        if(this.filterType == null){
            this.filterType = filterType;
        }

    }

    public String getFilterType() {
        return filterType;
    }

    /**
     * Filters the moodList based off the parameters.
     * @param moodList moodList object to be passed in
     */
    public void filter(ArrayList<Mood> moodList){
        if(enclosedDecorator != null){
            enclosedDecorator.filter(moodList);
        }
        specificFilter(moodList);
    }

    /**
     * Gets enclosed decorator.
     * @return enclosedDecorator
     */
    public FilterDecorator getEnclosedDecorator() {
        return enclosedDecorator;
    }

    /**
     * Sets enclosed decorater
     * @param enclosedDecorator returns enclosedDecorator
     */
    public void setEnclosedDecorator(FilterDecorator enclosedDecorator) {
        this.enclosedDecorator = enclosedDecorator;
    }

    //specificFilter must filter moodList depending on its filterType and filterParameter
    public abstract void specificFilter(ArrayList<Mood> moodList);

}
