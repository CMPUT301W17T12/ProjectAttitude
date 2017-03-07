package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;


public class ViewMoodActivity extends MoodActivity {

    private TextView emotionState;
    private TextView date;
    private TextView trigger;
    private TextView explanation;
    private TextView socialSituation;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);

        //Get all the fields
        emotionState = (TextView) findViewById(R.id.EmotionStateView);
        date = (TextView) findViewById(R.id.DateView);
        trigger = (TextView) findViewById(R.id.TriggerView);
        explanation = (TextView) findViewById(R.id.ExplanationView);
        socialSituation = (TextView) findViewById(R.id.SocialSituationView);

        //TODO Set texts from the mood
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
