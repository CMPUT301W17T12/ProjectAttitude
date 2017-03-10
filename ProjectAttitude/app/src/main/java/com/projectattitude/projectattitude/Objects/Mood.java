package com.projectattitude.projectattitude.Objects;

import android.media.Image;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Chris on 2/24/2017.
 */

public class Mood implements Serializable {

    private Date moodDate;
    private String emotionState;
    private GeoPoint geoLocation;
    private String trigger;
    private String explanation;
    private Image image;
    private String socialSituation;

    @JestId
    private String id;

//    public Mood(String s) {
//    }

    public Mood() {
    }

    public void setMoodDate(Date moodDate) {
        this.moodDate = moodDate;
    }

    public String getEmotionState() {
        return emotionState;
    }

    public void setEmotionState(String emotionState) {
        this.emotionState = emotionState;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }


    public Object getMoodDate() {
        return moodDate;
    }

    /**
     * This is what the list view shows, we can edit this later
     * @return a string currently containing the emotionState
     * @updated on 3/6/17
     */
    @Override
    public String toString(){
        //TODO Probably make a real ui here
        return(emotionState);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
