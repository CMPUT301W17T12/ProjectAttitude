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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.DatePickerEditText;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.Photo;
import com.projectattitude.projectattitude.R;

import java.io.ByteArrayOutputStream;

/**
 * The CreateMood handles the creation of mood objects for the user. The only field that is
 * mandatory is the mood state field. Entering other data will display it upon creation but is
 * not needed. Moods will be updated in the database if connected to the internet. Otherwise, they
 * will remain stored on the application where they will be pushed to the database as soon as a
 * connection is reestablished.
 */
public class CreateMoodActivity extends MoodActivity {

    private Mood newMood;   // initializing the mood object
    private Photo newPhoto;
    private ImageView imageView;
    private byte[] byteArray;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);  // setting the view

        Button completeButton = (Button) findViewById(R.id.saveButton); // initialization of the buttons
        Button addPhoto = (Button) findViewById(R.id.addPhoto);
        imageView = (ImageView) findViewById(R.id.imageView);
        final DatePickerEditText date = new DatePickerEditText(this, R.id.dateField);
        final Spinner emotionSpinner = (Spinner) findViewById(R.id.emotionSpinner);
        final EditText etTrigger = (EditText) findViewById(R.id.triggerField);
        final Spinner socialSituationSpinner = (Spinner) findViewById(R.id.spinner);
        //final CheckBox saveLocation = (CheckBox) findViewById(R.id.saveLocation); // geoPoint location saving
        s = "";


        imageView.setImageBitmap(null);

        /**
         * The complete button checks if there are any errors and then sets all the values of the
         * mood to the appropriate properties based off the data input.
         */
        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Spinner class will return a textview when you use getSelectedView(), allows for easy setError
                TextView errorText = (TextView) emotionSpinner.getSelectedView();

                if(errorCheck(errorText, etTrigger)){   //checking the trigger and emotional state selection
                    newMood = new Mood();
                    newMood.setEmotionState(emotionSpinner.getSelectedItem().toString());
                    newMood.setMoodDate(date.getDate());
                    newMood.setTrigger(etTrigger.getText().toString().trim());
                    newMood.setSocialSituation(socialSituationSpinner.getSelectedItem().toString());
                    newMood.setPhoto(s);

                    /*if(saveLocation.isChecked()){ //TODO check location
                        GeoPoint myLocation = LocationServices.FusedLocationApi.getLastLocation()
                        newMood.setGeoLocation(myLocation);
                    }*/


                    Intent returnCreateMoodIntent = new Intent();
                    returnCreateMoodIntent.putExtra("addMoodIntent", newMood);
                    //returnCreateMoodIntent.putExtra("addPhotoIntent", newPhoto);
                    setResult(RESULT_OK, returnCreateMoodIntent);
                    finish();
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //successful picking of photo
        if(requestCode == 3 && resultCode == RESULT_OK && null != data ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("PhotoBytes1", photo.getByteCount() + "");
            Log.d("PhotoHeight1", photo.getHeight() + "");
            Log.d("PhotoHeight1", photo.getWidth() + "");

            //if imageview is empty, keep it empty.
            if (imageView.getDrawable() == null) {
                Log.d("imageView", "empty");
                //nothing
            }
            else {
                //greater then byte threshold
                if (photo.getByteCount() > 65536) {
                    //4 or less times greater, scale by 2
                    if (photo.getByteCount() / 65536 <= 4) {
                        Bitmap photo1 = Bitmap.createScaledBitmap(photo, (photo.getWidth() / 2), (photo.getHeight() / 2), false);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Log.d("imageViewCompressed", photo1.getByteCount() + "");
                        byteArray = stream.toByteArray();
                        s = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        imageView.setImageBitmap(photo1);

                    }
                    //9 or less times greater, scale by 3
                    else if (photo.getByteCount() / 65536 <= 9) {
                        Bitmap photo1 = Bitmap.createScaledBitmap(photo, (photo.getWidth() / 3), (photo.getHeight() / 3), false);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Log.d("Compressed", photo1.getByteCount() + "");
                        byteArray = stream.toByteArray();
                        s = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        imageView.setImageBitmap(photo1);
                    }
                    else {
                        //anything greater then 9 times, scale by 4
                        Bitmap photo1 = Bitmap.createScaledBitmap(photo, (photo.getWidth() / 4), (photo.getHeight() / 4), false);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Log.d("Compressed", photo1.getByteCount() + "");
                        byteArray = stream.toByteArray();
                        s = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        imageView.setImageBitmap(photo1);
                    }
                }
                else {
                    //remains the same, no scaling
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    s = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    imageView.setImageBitmap(photo);
                }
            }
        }
        //cover case of taking picture, then aborting the taking of another
        else if (imageView.getDrawable() != null && !s.equals("")){
        }
        //no picture was picked
        else{
            s = "";
            Log.d("PhotoEmpty", s);
        }
    }

    /*error checks Emotional State spinner to make sure an emotional state was chosen
    also error checks trigger input field for character length*/
    public boolean errorCheck(TextView emotionStateText, EditText etTriggerText) {

        String etTriggerString = etTriggerText.getText().toString().trim();

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
