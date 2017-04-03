/*
 * MIT License
 *
 * Copyright (c) 2017 CMPUT301W17T12
 * Authors rsauveho vuk bfleyshe henrywei cs3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.projectattitude.projectattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Objects.ColorMap;
import com.projectattitude.projectattitude.Objects.EmoticonMap;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
    private TextView creator;
    private ImageView imageView;
    private ImageView emotionStateIcon;
    private Button profileButton;

    ScrollView r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood);

        //Get all the fields
        profileButton = (Button) findViewById(R.id.profileButton);
        emotionState = (TextView) findViewById(R.id.EmotionStateView);
        date = (TextView) findViewById(R.id.DateView);
        trigger = (TextView) findViewById(R.id.TriggerView);
        socialSituation = (TextView) findViewById(R.id.SocialSituationView);
        r1 = (ScrollView) findViewById(R.id.activity_view_mood);
        imageView = (ImageView) findViewById(R.id.imageView3);
        creator = (TextView) findViewById(R.id.creatorText);

        //set all text fields
        emotionState.setText("");
        date.setText("");
        trigger.setText("");
        socialSituation.setText("");

        imageView = (ImageView) findViewById(R.id.imageView3);
        emotionStateIcon = (ImageView) findViewById(R.id.EmotionalStateImage);

        //TODO Set texts from the mood
        final Mood mood = (Mood) getIntent().getSerializableExtra("mood");
        emotionState.setText(mood.getEmotionState());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        date.setText(sdf.format(mood.getMoodDate()));
        trigger.setText(mood.getTrigger());
        socialSituation.setText(mood.getSocialSituation());
        creator.setText(mood.getMaker());

        profileButton.setText("View " + mood.getMaker() + "'s Profile");
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMoodActivity.this, ViewOtherProfileActivity.class);
                ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
                getUserTask.execute(mood.getMaker());
                try{
                    User user = getUserTask.get();
                    if(user != null){// is online and got user
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }else{ // is offline
                        Toast.makeText(ViewMoodActivity.this, "Must be connected to internet to view user!", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        // Show emoticons
        EmoticonMap<String, Integer> eMap = new EmoticonMap<>();
        int res = (int) eMap.get(mood.getEmotionState());
        emotionStateIcon.setImageResource(res);

        //decode base64 image stored in User
        byte[] imageBytes = Base64.decode(mood.getPhoto(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(decodedImage);
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
        Intent intentEdit = new Intent(ViewMoodActivity.this, EditMoodActivity.class);
        intentEdit.putExtra("mood", mood);
        startActivityForResult(intentEdit, 0); //Handled in the results section
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

    /**
     * This should no longer be used as we no longer delete or edit moods from viewing them
     * requestCode 0 = Add mood
     * requestCode 1 = View mood -- resultCode 2 = delete, 3 = Edit Mood
     * requestCode 2 = Edit Mood
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
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

    /**
     * Taken from http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
     * @return a bool if the device is connected to the internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
