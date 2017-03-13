package com.projectattitude.projectattitude;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.CreateMoodActivity;
import com.projectattitude.projectattitude.Activities.EditMoodActivity;
import com.projectattitude.projectattitude.Activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by Boris on 12/03/2017.
 *
 * This class starts in the login screen, logs in and attempts to make a mood.
 */

public class MainActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityUITest() {
        super(com.projectattitude.projectattitude.Activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    private void testCreateMood(){
        LoginActivityUITest loginActivityUITest = new com.projectattitude.projectattitude.LoginActivityUITest();    //login
        loginActivityUITest.testLogin();
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.addMoodButton));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        assertTrue(solo.searchText("Select an emotional state"));   //creating mood
        solo.clickOnText("Select an emotional state");
        assertTrue(solo.searchText("Happy"));
        solo.clickOnText("Happy");

        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        assertTrue(solo.searchText("Happy"));   //checking if mood exist

        deleteFirstMood();

        assertFalse(solo.searchText("Happy"));
    }

    private void testEditMood(){
        LoginActivityUITest loginActivityUITest = new com.projectattitude.projectattitude.LoginActivityUITest();    //login
        loginActivityUITest.testLogin();
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.addMoodButton));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        assertTrue(solo.searchText("Select an emotional state"));   //creating mood
        solo.clickOnText("Select an emotional state");
        assertTrue(solo.searchText("Happy"));
        solo.clickOnText("Happy");

        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        assertTrue(solo.searchText("Happy"));   //checking if mood exist

        solo.clickLongInList(0);
        assertTrue(solo.searchText("Edit"));
        solo.clickOnButton("Edit");

        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);

        assertTrue(solo.searchText("Happy"));   // editing happy mood to angry mood
        solo.searchText("Happy");
        solo.clickOnText("Happy");
        assertTrue(solo.searchText("Anger"));
        solo.clickOnText("Anger");

        assertTrue(solo.searchButton("Save"));
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);   // checking if mood changed
        assertTrue(solo.searchText("Anger"));
        assertFalse(solo.searchButton("Happy"));
    }

    private void deleteFirstMood(){ //deleting mood
        solo.clickLongInList(0);
        assertTrue(solo.searchText("Delete"));
        solo.clickOnButton("Delete");
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}