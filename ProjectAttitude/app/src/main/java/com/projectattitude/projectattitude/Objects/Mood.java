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

package com.projectattitude.projectattitude.Objects;

import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by Chris on 2/24/2017.
 * This is the mood class that represents the heart of this application. The majority of functions
 * here are getters and setters for the various properties of the mood object such as date and
 * trigger.
 */

public class Mood implements Serializable {

    private Date moodDate;
    private String emotionState;
    private Location geoLocation;
    private String trigger;
    private String explanation;
    private Image image;
    private String socialSituation;

    @JestId
    private String id;
    private String photo;


    /**
     * Initiates the mood object.
     */
    public Mood() {
    }

    /**
     * Sets the moodDate to the given moodDate.
     * @param moodDate moodDate
     */
    public void setMoodDate(Date moodDate) {
        this.moodDate = moodDate;
    }

    /**
     * Returns the emotionState.
     * @return emotionState
     */
    public String getEmotionState() {
        return emotionState;
    }

    /**
     * Sets the emotionState to the given emotionState.
     * @param emotionState emotionState
     */
    public void setEmotionState(String emotionState) {
        this.emotionState = emotionState;
    }

    /**
     * Gets the geoLocation.
     * @return geoLocation
     */
    public Location getGeoLocation() {
        return geoLocation;
    }

    /**
     * Sets the geoLocation
     * @param geoLocation geoLocation
     */
    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * Gets the trigger.
     * @return trigger
     */
    public String getTrigger() {
        return trigger;
    }

    /**
     * Sets the trigger.
     * @param trigger trigger
     */
    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    /**
     * Gets the explanation.
     * @return explanation
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets the explanation.
     * @param explanation explanation
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Gets the image object.
     * @return image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the image.
     * @param image image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets the socialSituation.
     * @return socialSituation
     */
    public String getSocialSituation() {
        return socialSituation;
    }

    /**
     * Sets the socialSituation.
     * @param socialSituation socialSituation
     */
    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * Gets the moodDate.
     * @return moodDate
     */
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
        return(emotionState);
    }

    /**
     * Gets the userID
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the userID
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets photo variable as string of 64 bit encoded
     * @param photo photo
     */
    public void setPhoto(String photo){

        this.photo = photo;
    }

    /**
     * Returns photo as string
     * @return photo
     */
    public String getPhoto(){
        return photo;
    }
}
