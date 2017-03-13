package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.ColorMap;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

/**
 * This activity allows the user to view a mood object in extended detail, such
 * as viewing the location and image if appropriate. An edit button and delete button
 * appear in the corners that allow for their respective features.
 */
public class ViewMoodActivity extends MoodActivity {

    private TextView emotionState;
    private TextView date;
    private TextView trigger;
    private TextView socialSituation;
    private Button editButton;
    private Button deleteButton;
    private ImageView imageView;

    RelativeLayout r1;

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
        imageView = (ImageView) findViewById(R.id.imageView3);

        //set all text fields
        emotionState.setText("");
        date.setText("");
        trigger.setText("");
        socialSituation.setText("");

        //TODO Set texts from the mood
        final Mood mood = (Mood) getIntent().getSerializableExtra("mood");
        emotionState.setText(mood.getEmotionState());
        date.setText(mood.getMoodDate().toString());
        trigger.setText(mood.getTrigger());
        socialSituation.setText(mood.getSocialSituation());

        //decode base64 image stored in User
        byte[] imageBytes = Base64.decode(mood.getPhoto(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(decodedImage);
        //Handle colors
        ColorMap<String, Integer> map = new ColorMap<>();
        r1.setBackgroundColor((Integer) map.get(mood.getEmotionState()));

        //on click listener editing moods
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editMood(mood);
            }
        });

        //on click listener for deleting moods
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteMood(mood);
            }
        });

    }

    /**
     * Takes the user to the edit mood view
     * Functions the same as in main activity
     * Suggest we make an abstract class or a controller to do both?
     * @param mood the mood to be changed
     */
    private void editMood(Mood mood){
        Intent intentEdit = new Intent(ViewMoodActivity.this, EditMoodActivity.class);
        intentEdit.putExtra("mood", mood);
        startActivityForResult(intentEdit, 0); //Handled in the results section
        //TODO make edit mood


    }


    /**
     * Deletes a given mood
     * Functions the same as in main activity
     * Suggest we make an abstract class or a controller to do both?
     * @param mood the mood to be deleted
     */
    private void deleteMood(Mood mood){
        Intent returnToMain = new Intent();
        setResult(2, returnToMain);
        finish();

    }
    @Override
    // requestCode 0 = Add mood
    // requestCode 1 = View mood -- resultCode 2 = delete, 3 = Edit Mood
    // requestCode 2 = Edit Mood
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Mood returnedMood;

        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                Mood newMood = (Mood) data.getSerializableExtra("mood");
                Intent returnCreateMoodIntent = new Intent();
                returnCreateMoodIntent.putExtra("newMood", newMood);
                setResult(3, returnCreateMoodIntent);
                finish();
            }

        }

    }

}
