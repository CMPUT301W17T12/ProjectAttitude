package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.DatePickerEditText;
import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

public class CreateMoodActivity extends MoodActivity {

    private Mood newMood;
    private Spinner emotionSpinner;
    //private EditText emotionalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);

        Button completeButton = (Button) findViewById(R.id.saveButton);
        final DatePickerEditText date = new DatePickerEditText(this, R.id.dateField);
        final Spinner emotionSpinner = (Spinner) findViewById(R.id.emotionSpinner);

        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Spinner class will return a textview when you use getSelectedView(), allows for easy setError
                TextView errorText = (TextView) emotionSpinner.getSelectedView();

                if(errorCheck(errorText)){
                    newMood = new Mood();
                    newMood.setEmotionState(emotionSpinner.getSelectedItem().toString());
                    newMood.setMoodDate(date.getDate());
                    Intent returnCreateMoodIntent = new Intent();
                    returnCreateMoodIntent.putExtra("addMoodIntent", newMood);
                    setResult(RESULT_OK, returnCreateMoodIntent);
                    finish();
                }
            }
        });
    }

    //error checks Emotional State spinner to make sure an emotional state was chosen
    public boolean errorCheck(TextView emotionStateText) {
        Log.d("check on emotion state", emotionStateText.getText().toString());
        if(emotionStateText.getText().toString().equals("Select an emotional state")) {
            emotionStateText.setTextColor(Color.RED);
            emotionStateText.setText(R.string.emotion_state_error);

            return false;
        }
        return true;
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
