package com.projectattitude.projectattitude.Objects;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Chris on 2/24/2017.
 */

public class Mood implements Serializable {

    private Date moodDate;
    private String emotionState;
    private String geoLocation;
    private String trigger;
    private String explanation;
    private ContactsContract.Contacts.Photo photograph; //Maybe make this an Image?
    private String socialSituation;

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

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
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

    public ContactsContract.Contacts.Photo getPhotograph() {
        return photograph;
    }

    public void setPhotograph(ContactsContract.Contacts.Photo photograph) {
        this.photograph = photograph;
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

}
