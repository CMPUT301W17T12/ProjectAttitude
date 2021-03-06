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
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projectattitude.projectattitude.Abstracts.MoodActivity;
import com.projectattitude.projectattitude.Objects.DatePickerEditText;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.io.ByteArrayOutputStream;

/**
 * The CreateMood handles the creation of mood objects for the user. The only field that is
 * mandatory is the mood state field. Entering other data will display it upon creation but is
 * not needed. Moods will be updated in the database if connected to the internet. Otherwise, they
 * will remain stored on the application where they will be pushed to the database as soon as a
 * connection is reestablished.
 */
public class CreateMoodActivity extends MoodActivity{

    private Mood newMood;   // initializing the mood object
    private ImageView imageView;
    private byte[] byteArray;
    private String s;
    private double latitude;
    private double longitude;

    /**
     * Sets all the ui elements when the create mood activity is created
     * @param savedInstanceState
     */
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
        final CheckBox saveLocation = (CheckBox) findViewById(R.id.saveLocation); // geoPoint location saving
        s = "";
        longitude = 0;
        latitude = 0;


        imageView.setVisibility(View.GONE);
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
                    String username = getIntent().getStringExtra("username");
                    newMood.setMaker(username);
                    newMood.setEmotionState(emotionSpinner.getSelectedItem().toString());
                    newMood.setMoodDate(date.getDate());
                    newMood.setTrigger(etTrigger.getText().toString().trim());
                    newMood.setLongitude(longitude);
                    newMood.setLatitude(latitude);


                    if(socialSituationSpinner.getSelectedItem().toString().equals("Select a social situation")){
                        newMood.setSocialSituation("");
                    }
                    else{
                        newMood.setSocialSituation(socialSituationSpinner.getSelectedItem().toString());
                    }
                    newMood.setPhoto(s);


                    Intent returnCreateMoodIntent = new Intent();
                    returnCreateMoodIntent.putExtra("addMoodIntent", newMood);
                    //returnCreateMoodIntent.putExtra("addPhotoIntent", newPhoto);
                    setResult(RESULT_OK, returnCreateMoodIntent);
                    finish();
                }
            }
        });

        /**
         * This toggle saves your location when checked.
         */
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveLocation.isChecked()) {
                    GPSTracker gps = new GPSTracker(CreateMoodActivity.this);
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Log.d("UserLocation", "latitude:" + gps.getLatitude()
                                + ", longitude: " + gps.getLongitude());

                        //sometimes only round to 3 decimals, I think it has to do with the
                        //how the round function calculates
                        latitude = Math.round(gps.getLatitude() * 10000d) / 10000d;
                        longitude = Math.round(gps.getLongitude() * 10000d) / 10000d;

                        if(latitude == 0 & longitude == 0){
                            Toast.makeText(CreateMoodActivity.this, "Could not find your location, please try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    else{
                        Toast.makeText(CreateMoodActivity.this, "Please turn on GPS for locations!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //NaN breaks the app when you undo location selection and complete mood creation
//                    latitude = NaN;
//                    longitude = NaN;
                    latitude = 0.0;
                    longitude = 0.0;

                }
            }
        });

        /**
         * This handles clicking on the add photo button to add photos to a mood
         */
        addPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This handles the results of the add photo activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PhotoTake", "picking photo");
        //successful picking of photo
        Log.d("PhotoTakeTest", "RequestCode: " + requestCode + " ResultCode: " + " Data: " + data );

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
            Log.d("PhotoAbort", "aborted");
        }
        //cover case when image is aborted
        else{
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.GONE);
//            imageView.setImageBitmap(null);
        }
    }

    /**
     * error checks Emotional State spinner to make sure an emotional state was chosen
     * also error checks trigger input field for character length
     */
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

//    @Override
//    public void onLocationChanged(Location location) {
//        currentLattitude = location.getLatitude();
//        currentLongitude = location.getLongitude();
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("Latitude","disable");
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("Latitude","enable");
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("Latitude","status");
//    }
}
