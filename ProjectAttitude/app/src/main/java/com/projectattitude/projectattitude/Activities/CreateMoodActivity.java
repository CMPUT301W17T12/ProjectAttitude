package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projectattitude.projectattitude.Abstracts.DatePickerEditText;
import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

public class CreateMoodActivity extends MoodActivity {

    private Mood newMood;
    private EditText emotionalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);

        Button completeButton = (Button) findViewById(R.id.saveButton);
        final DatePickerEditText date = new DatePickerEditText(this, R.id.dateField);
        emotionalState = (EditText) findViewById(R.id.emotionalStateField);

        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMood = new Mood();
                newMood.setEmotionState(emotionalState.getText().toString());
                newMood.setMoodDate(date.getDate());
                Intent returnCreateMoodIntent = new Intent();
                returnCreateMoodIntent.putExtra("addMoodIntent", newMood);
                setResult(RESULT_OK, returnCreateMoodIntent);
                finish();
            }
        });
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
