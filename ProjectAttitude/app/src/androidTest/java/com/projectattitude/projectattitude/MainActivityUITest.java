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
import android.widget.TextView;

import com.projectattitude.projectattitude.Activities.EditMoodActivity;
import com.projectattitude.projectattitude.Activities.LoginActivity;
import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Activities.ViewMoodActivity;
import com.robotium.solo.Solo;
import com.projectattitude.projectattitude.Activities.CreateMoodActivity;

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
        createHappy(solo);
        assertTrue(solo.searchText("Happiness"));   //checking if mood exist

        deleteFirstMood();

        assertFalse(solo.searchText("Happiness"));
    }

    //Logs in, creates a happy mood then changes it to angry, deletes after
    public void testEditMood(){
        logIn(solo);
        createHappy(solo);

        assertTrue(solo.searchText("Happiness"));   //checking if mood exist

        solo.clickLongInList(0);
        assertTrue(solo.searchText("Edit"));
        solo.clickOnText("Edit");

        solo.assertCurrentActivity("Wrong activity", EditMoodActivity.class);

        assertTrue(solo.searchText("Happiness"));   // editing happy mood to angry mood
        solo.searchText("Happiness");
        solo.clickOnText("Happiness");
        assertTrue(solo.searchText("Anger"));
        solo.clickOnText("Anger");

        assertTrue(solo.searchButton("Save"));
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);   // checking if mood changed
        assertTrue(solo.searchText("Anger"));
        assertFalse(solo.searchButton("Happiness"));
        deleteFirstMood();
    }

    public void testViewMood(){ //Making sure view mood works
        logIn(solo);
        createHappy(solo);


        solo.clickLongInList(0);
        assertTrue(solo.searchText("View"));
        solo.clickOnText("View");
        solo.assertCurrentActivity("Wrong activity", ViewMoodActivity.class);
        assertTrue(solo.searchText("Happiness"));
        solo.clickOnText("Delete");
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        assertFalse(solo.searchText("Happiness"));


    }

    public void deleteFirstMood(){ //deletes the first mood
        solo.clickLongInList(0);
        assertTrue(solo.searchText("Delete"));
        solo.clickOnText("Delete");
    }

    public void logIn(Solo solo){ //Logs in with tester account
        solo.enterText((EditText)solo.getView(R.id.usernameField), "tester");
        solo.clickOnView(solo.getView(R.id.signInButton));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    public void createHappy(Solo solo){ //Creates a happy mood and returns to main
        solo.clickOnView(solo.getView(R.id.addMoodButton));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        assertTrue(solo.searchText("Select an emotional state"));   //creating mood
        solo.clickOnText("Select an emotional state");
        assertTrue(solo.searchText("Happiness"));
        solo.clickOnText("Happiness");

        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}