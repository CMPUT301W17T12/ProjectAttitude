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

package com.projectattitude.projectattitude;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.projectattitude.projectattitude.Activities.CreateMoodActivity;
import com.projectattitude.projectattitude.Activities.EditMoodActivity;
import com.projectattitude.projectattitude.Activities.LoginActivity;
import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Activities.ViewMoodActivity;
import com.robotium.solo.Solo;

import java.util.Date;

/**
 * Created by Boris on 12/03/2017.
 *
 * This class starts in the login screen, logs in and attempts to make a mood.
 */

public class MainActivityUITest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public MainActivityUITest() {
        super(com.projectattitude.projectattitude.Activities.LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Logs in, creates a happy mood then checks if it exists before deleting
     * Then checks to make sure delete works
     */
    public void testCreateMood(){
        logIn(solo);
        checkMoods();
        createHappy(solo);
        assertTrue(solo.searchText("Happiness"));   //checking if mood exist

        deleteFirstMood();

        assertFalse(solo.searchText("Happiness"));
    }

    /**
     * Logs in, creates a happy mood then changes it to angry, deletes after
     */
    public void testEditMood(){
        logIn(solo);
        createHappy(solo);

        assertTrue(solo.searchText("Happiness"));   //checking if mood exist

        solo.clickLongInList(0);
        assertTrue(solo.searchText("Edit")); //Make sure edit is there
        solo.clickOnText("Edit");

        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class); //Ensure activity changed

        // editing happy mood to angry mood
        assertTrue(solo.searchText("Happiness"));
        solo.clickOnText("Happiness");
        assertTrue(solo.searchText("Anger"));
        solo.clickOnText("Anger");

        //Change situation
        assertTrue(solo.searchText("Alone"));
        solo.clickOnText("Alone");//Edit the emotional state
        solo.clickOnText("With a crowd");
        assertTrue(solo.searchText("With a crowd"));


        assertTrue(solo.searchButton("Save"));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));

        //Ensure save still works
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);   // checking if mood changed
        assertTrue(solo.searchText("Anger"));
        assertFalse(solo.searchButton("Happiness"));
        deleteFirstMood();
    }

    public void testViewMood(){ //Making sure view mood works
        logIn(solo);
        createHappy(solo);

        solo.clickInList(0);

        solo.assertCurrentActivity("Wrong activity", ViewMoodActivity.class);
        //Make sure the proper fields come up
        assertTrue(solo.searchText("Happiness"));
        assertTrue(solo.searchText("Alone"));
        solo.goBack();
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        deleteFirstMood();
        assertFalse(solo.searchText("Happiness"));
    }

    public void testFilterByDay(){
        logIn(solo);
        createHappy(solo);

        solo.clickLongInList(0);
        assertTrue(solo.searchText("Edit"));
        solo.clickOnText("Edit");
        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);
        assertTrue(solo.searchText("Happiness"));

        assertTrue(solo.searchText("Save"));
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));

        createHappy(solo);  //creating second mood

        solo.clickLongInList(1);
        assertTrue(solo.searchText("Edit"));
        solo.clickOnText("Edit");
        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);
        assertTrue(solo.searchText("Happiness"));
        solo.clickOnText("Happiness");

        assertTrue(solo.searchText("Anger"));
        solo.clickOnText("Anger");  //changing to anger emotion to distinguish

        solo.clickOnText("2017");
        solo.setDatePicker(0, 2017, 3, 17);

        solo.clickOnText("OK");

        assertTrue(solo.searchText("Save"));
        solo.clickOnText("Save"); //Probably clicks on the save location
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));

        solo.clickOnImageButton(0);
        solo.clickOnText("Filter By Time");
        solo.clickOnText("Day");

        assertTrue(solo.searchText("Happiness"));
        assertFalse(solo.searchText("Anger"));

        // clean up
        deleteFirstMood();
        solo.clickOnImageButton(0);
        solo.clickOnText("Clear Filters");
        deleteFirstMood();
    }

    public void testFilterMood(){
        logIn(solo);
        createHappy(solo);

        createHappy(solo);  // creating a second mood as happy and editing it to be angry
        solo.clickLongInList(1);
        solo.clickOnText("Edit");
        solo.clickOnText("Happiness");
        solo.clickOnText("Anger");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));

        solo.clickOnImageButton(0);
        solo.clickOnText("Emotions");
        solo.clickOnScreen(721,396);

        assertTrue(solo.searchText("Anger"));   // only anger should be present
        assertFalse(solo.searchText("Happiness"));


        // clean up
        deleteFirstMood();
        solo.clickOnImageButton(0);
        solo.clickOnText("Clear Filters");
        deleteFirstMood();
    }

    public void deleteFirstMood(){ //deletes the first mood in the list
        solo.clickLongInList(0);
        assertTrue(solo.searchText("Delete"));
        solo.clickOnText("Delete");
    }
    //TODO Maybe make a method to clean out the list fully?

    public void logIn(Solo solo){ //Logs in with tester account
        solo.enterText((EditText)solo.getView(R.id.usernameField), "tester");
        solo.clickOnView(solo.getView(R.id.signInButton));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    public void checkMoods(){ //Makes sure the minimum fields exist within a mood
        solo.clickOnScreen(890, 1664); //The location of the fab button
        solo.clickOnView(solo.getView(R.id.fabAddMood));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        assertTrue(solo.searchText("Select an emotional state"));
        solo.clickOnText("Select an emotional state");

        //Tests to make sure the required moods exist
        assertTrue(solo.searchText("Anger"));
        assertTrue(solo.searchText("Confusion"));
        assertTrue(solo.searchText("Disgust"));
        assertTrue(solo.searchText("Fear"));
        assertTrue(solo.searchText("Happiness"));
        assertTrue(solo.searchText("Sadness"));
        assertTrue(solo.searchText("Shame"));
        assertTrue(solo.searchText("Surprise"));
        solo.clickOnText("Happiness");

        //Testing social situations
        solo.clickOnText("Select a social situation");
        assertTrue(solo.searchText("Alone"));
        assertTrue(solo.searchText("With one other person"));
        assertTrue(solo.searchText("With two to several people"));
        assertTrue(solo.searchText("With a crowd"));
        solo.clickOnText("Alone");


        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));
        deleteFirstMood();

    }

    /**
     * This test checks if a users name pops up in the profile screen
     * Also checks that the latest mood is there and correct
     */
    //TODO: Add following moods
    public void testProfile(){
        logIn(solo);
        createHappy(solo);
        solo.clickLongInList(0);
        solo.clickOnText("Edit");
        solo.clickOnText("Happiness");
        solo.clickOnText("Disgust");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnScreen(890, 1664); //The location of the fab button
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.fabProfile));
        assertTrue(solo.searchText("tester")); //Checks for the user name
        assertTrue(solo.searchText("Disgust")); //Checks most recent mood
        solo.goBack();
        deleteFirstMood();//Clean up
    }

    public void createHappy(Solo solo){ //Creates a happy mood and returns to main
        solo.clickOnScreen(890, 1664); //This is the location of the fab button
        solo.clickOnView(solo.getView(R.id.fabAddMood));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        //Set mood to happy
        assertTrue(solo.searchText("Select an emotional state"));
        solo.clickOnText("Select an emotional state");
        solo.clickOnText("Happiness");

        //Set trigger to Test
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.triggerField), "Test");
        assertTrue(solo.searchText("Test"));

        //Set situation to Alone
        solo.clickOnText("Select a social situation");
        solo.clickOnText("Alone");

        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.saveButton));

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }



    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
