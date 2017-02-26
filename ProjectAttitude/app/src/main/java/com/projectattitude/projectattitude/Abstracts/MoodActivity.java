package com.projectattitude.projectattitude.Abstracts;

import android.location.Location;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Created by Chris on 2/25/2017.
 * This will be an abstract class that the other activities inherit from
 * I got tired of copy pasting variables, thats stupid so use inheritance
 */

public abstract class MoodActivity extends AppCompatActivity {
    private String emotionState;
    private Location geoLocation; //Unsure if this is proper type
    private Image photo; //Unsure here as well
    private String trigger;
    private Date moodDate;
    private String socialSituation;
    private String explanation;
}
