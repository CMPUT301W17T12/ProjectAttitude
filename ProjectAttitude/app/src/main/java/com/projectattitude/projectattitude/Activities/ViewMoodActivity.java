package com.projectattitude.projectattitude.Activities;

import android.os.Bundle;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;


public class ViewMoodActivity extends MoodActivity {



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
