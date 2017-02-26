package com.projectattitude.projectattitude.Activities;

import android.location.Location;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.barcode.Barcode;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.util.Date;


public class ViewMoodActivity extends AppCompatActivity {
    private String emotionState;
    private Location geoLocation; //Unsure if this is proper type
    private Image photo; //Unsure here as well
    private String trigger;
    private Date moodDate;
    private String socialSituation;
    private String explanation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);
    }

    /**
     * Takes the user to the edit mood view
     * Functions the same as in main activity
     * Suggest we make an abstract class or a controller to do both?
     * @param mood the mood to be changed
     */
    private void editMood(Mood mood){

    }

    /**
     * Deletes a given mood
     * Functions the same as in main activity
     * Suggest we make an abstract class or a controller to do both?
     * @param mood the mood to be deleted
     */
    private void deleteMood(Mood mood){

    }

}
