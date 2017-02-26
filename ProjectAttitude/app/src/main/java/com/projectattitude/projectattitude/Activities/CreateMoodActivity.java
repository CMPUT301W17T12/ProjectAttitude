package com.projectattitude.projectattitude.Activities;

import android.os.Bundle;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.R;

public class CreateMoodActivity extends MoodActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);
    }

    /**
     * Not sure what this does tbh
     * @return A location? Should probably be the Location Type
     */
    private String createLocation(){
        return "";
    }

    /**
     * I don't know what this does either
     * @return an Image most likely
     */
    private void createPicture(){
        return;
    }
}
