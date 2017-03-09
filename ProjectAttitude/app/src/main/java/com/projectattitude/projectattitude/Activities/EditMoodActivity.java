package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

public class EditMoodActivity extends MoodActivity {
    Button editButton;
    Button deleteButton;
    TextView dateView;
    TextView emotionStateView;
    TextView triggerView;
    TextView socialSituationView;
    Intent intent;

    //https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    //Resourse for the spinner I made
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);
        editButton = (Button) findViewById(R.id.EditButton);
        deleteButton = (Button) findViewById(R.id.DeleteButton);

        dateView = (TextView) findViewById(R.id.DateView);
        emotionStateView = (TextView) findViewById(R.id.EmotionStateView);
        triggerView = (TextView) findViewById(R.id.TriggerView);
        socialSituationView = (TextView) findViewById(R.id.SocialSituationView);

        Mood mood = (Mood) getIntent().getSerializableExtra("mood");


    }

    /**
     * Not sure what this does tbh
     * @see CreateMoodActivity
     * @return A location? Should probably be the Location Type
     * once again we do the same function twice, should probably not repeat code if we can avoid it
     * Maybe add it to Mood Activity?
     */
    private String createLocation(){
        return "";
    }

    /**
     * I don't know what this does either
     * @see CreateMoodActivity
     * @return an Image most likely
     */
    private void createPicture(){
        return;
    }
}
