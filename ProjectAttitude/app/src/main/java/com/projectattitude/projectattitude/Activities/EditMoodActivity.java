package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.projectattitude.projectattitude.Objects.DatePickerEditText;
import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.util.Date;

public class EditMoodActivity extends MoodActivity {
    Button completeButton;
    Button cancelButton;

    Intent intent;

    DatePickerEditText date;
    Spinner emotionSpinner;
    EditText etTrigger;
    Spinner socialSituationSpinner;
    private Mood newMood;


    //https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    //Resourse for the spinner I made
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);
        completeButton = (Button) findViewById(R.id.saveButton);
        date = new DatePickerEditText(this, R.id.dateField);
        emotionSpinner = (Spinner) findViewById(R.id.emotionSpinner);
        etTrigger = (EditText) findViewById(R.id.triggerField);
        socialSituationSpinner = (Spinner) findViewById(R.id.spinner);



        Mood mood = (Mood) getIntent().getSerializableExtra("mood");
        //Changes the fields to the selected mood
        etTrigger.setText(mood.getTrigger());
        Date tempDate = (Date) mood.getMoodDate();
        date.setDate(tempDate.getYear()+1900, tempDate.getMonth(), tempDate.getDay());
        //disgusting single line way to set the spinners
        //Taken from http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
        emotionSpinner.setSelection(((ArrayAdapter<String>)emotionSpinner.getAdapter())
                .getPosition(mood.getEmotionState()));
        socialSituationSpinner.setSelection(((ArrayAdapter<String>)socialSituationSpinner
                .getAdapter()).getPosition(mood.getSocialSituation()));


        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Spinner class will return a textview when you use getSelectedView(), allows for easy setError
                TextView errorText = (TextView) emotionSpinner.getSelectedView();

                if(errorCheck(errorText, etTrigger)){
                    newMood = new Mood();
                    newMood.setEmotionState(emotionSpinner.getSelectedItem().toString());
                    newMood.setMoodDate(date.getDate());
                    newMood.setTrigger(etTrigger.getText().toString());
                    newMood.setSocialSituation(socialSituationSpinner.getSelectedItem().toString());
                    Intent returnCreateMoodIntent = new Intent();
                    returnCreateMoodIntent.putExtra("mood", newMood);
                    setResult(RESULT_OK, returnCreateMoodIntent);
                    finish();
                }
            }
        });


    }
    /*error checks Emotional State spinner to make sure an emotional state was chosen
also error checks trigger input field for character length*/
    public boolean errorCheck(TextView emotionStateText, EditText etTriggerText) {

        String etTriggerString = etTriggerText.getText().toString();

        //count whitespace of trigger string
        int spaces = etTriggerString.length() - etTriggerString.replace(" ", "").length();

        if(emotionStateText.getText().toString().equals("Select an emotional state")) {
            emotionStateText.setTextColor(Color.RED);
            emotionStateText.setText(R.string.emotion_state_error);

            return false;
        }

        //trigger is longer then 20 characters
        else if(etTriggerString.length() > 20){
            etTriggerText.setError("Reason must be no more than 20 characters");
            return false;
        }

        //trigger is more than 3 words
        else if(spaces > 2){
            etTriggerText.setError("Reason must be no more than 3 words");
            return false;
        }
        return true;
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
