package com.projectattitude.projectattitude.Abstracts;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

/**
 * Created by Chris on 2/25/2017.
 * This will be an abstract class that the other activities inherit from.
 */

public abstract class MoodActivity extends AppCompatActivity {
    private String emotionState;
    private GeoPoint geoLocation;
    private Image image;
    private String trigger;
    private Date moodDate;
    private String socialSituation;
    private String explanation;
}
