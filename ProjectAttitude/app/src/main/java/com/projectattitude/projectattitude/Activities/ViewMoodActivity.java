package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.ColorMap;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.util.HashMap;


public class ViewMoodActivity extends MoodActivity {

    private TextView emotionState;
    private TextView date;
    private TextView trigger;
    private TextView socialSituation;
    private Button editButton;
    private Button deleteButton;

    RelativeLayout r1;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);

        //Get all the fields
        emotionState = (TextView) findViewById(R.id.EmotionStateView);
        date = (TextView) findViewById(R.id.DateView);
        trigger = (TextView) findViewById(R.id.TriggerView);
        socialSituation = (TextView) findViewById(R.id.SocialSituationView);
        editButton = (Button) findViewById(R.id.EditButton);
        deleteButton = (Button) findViewById(R.id.DeleteButton);
        r1 = (RelativeLayout) findViewById(R.id.activity_view_mood);
        emotionState.setText("");
        date.setText("");
        trigger.setText("");
        socialSituation.setText("");

        //TODO Set texts from the mood
        Mood mood = (Mood) getIntent().getSerializableExtra("mood");
        emotionState.setText(mood.getEmotionState());
        date.setText(mood.getMoodDate().toString());
        trigger.setText(mood.getTrigger());
        socialSituation.setText(mood.getSocialSituation());
        //Handle colors
        ColorMap<String, Integer> map = new ColorMap<>();
        r1.setBackgroundColor((Integer) map.get(mood.getEmotionState()));

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
