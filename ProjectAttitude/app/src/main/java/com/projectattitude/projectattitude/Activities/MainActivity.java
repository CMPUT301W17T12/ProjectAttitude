package com.projectattitude.projectattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //TODO Build these functions
    /**
     * This method will take the user to the Create Mood view
     */
    private void createMood(){

    }

    /**
     * This method takes a mood the user made and brings them to the edit mood view
     */
    private void editMood(){

    }

    /**
     * This deletes a selected mood.
     * @param mood the mood the user wants to get rid of
     */
    private void deleteMood(Mood mood){

    }

    /**
     * This is the method that handles finding moods with a given keyword
     * Will probably return a mood list object in time, or set the current one.
     */
    private void searchMood(){

    }

    /**
     * Handles sorting the list, may need several functions for each type of sort.
     * @param moods the moodlist that is to be sorted
     */
    private void sortMood(MoodList moods){

    }

    /**
     * When the user clicks the map button this takes them to the map view
     */
    private void goToMap(){
    }

    /**
     * When the user clicks the profile button it will take them to the profile view
     * Later may take a profile as an argument to go to someone elses profile.
     */
    private void viewProfile(){
    }

    /**
     * Logs the current profile out of the application and returns the user to the log in view.
     */
    private void logOut(){

    }



}
