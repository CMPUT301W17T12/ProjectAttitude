package com.projectattitude.projectattitude;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
<<<<<<< HEAD
import android.text.Layout;
import android.util.Log;
import android.view.View;
=======
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e
import android.widget.EditText;
import android.widget.TextView;

import com.projectattitude.projectattitude.Activities.EditMoodActivity;
import com.projectattitude.projectattitude.Activities.LoginActivity;
import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Activities.ViewMoodActivity;
import com.robotium.solo.Solo;
import com.projectattitude.projectattitude.Activities.CreateMoodActivity;

import java.util.Date;
<<<<<<< HEAD
import java.util.zip.Inflater;
=======
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e

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
<<<<<<< HEAD
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
        solo.clickOnButton("Save");

        //Ensure save still works
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
        //Make sure the proper fields come up
        assertTrue(solo.searchText("Happiness"));
        assertTrue(solo.searchText("Alone"));
        solo.clickOnText("Delete");
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        assertFalse(solo.searchText("Happiness"));

=======
        createHappy(solo);
        assertTrue(solo.searchText("Happiness"));   //checking if mood exist
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e

    }

<<<<<<< HEAD
    public void deleteFirstMood(){ //deletes the first mood in the list
        solo.clickLongInList(0);
        assertTrue(solo.searchText("Delete"));
        solo.clickOnText("Delete");
=======
        assertFalse(solo.searchText("Happiness"));
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e
    }
    //TODO Maybe make a method to clean out the list fully?

<<<<<<< HEAD
    public void logIn(Solo solo){ //Logs in with tester account
        solo.enterText((EditText)solo.getView(R.id.usernameField), "tester");
        solo.clickOnView(solo.getView(R.id.signInButton));
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    public void checkMoods(){ //Makes sure the minimum fields exist within a mood
        solo.clickOnView(solo.getView(R.id.addMoodButton));
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
        solo.clickOnButton("Save");
        deleteFirstMood();

    }

    public void createHappy(Solo solo){ //Creates a happy mood and returns to main
        solo.clickOnView(solo.getView(R.id.addMoodButton));
        solo.assertCurrentActivity("Wrong activity", CreateMoodActivity.class);

        //Set mood to happy
        assertTrue(solo.searchText("Select an emotional state"));
        solo.clickOnText("Select an emotional state");
        solo.clickOnText("Happiness");
=======
    //Logs in, creates a happy mood then changes it to angry, deletes after
    public void testEditMood(){
        logIn(solo);
        createHappy(solo);

        assertTrue(solo.searchText("Happiness"));   //checking if mood exist

        solo.clickLongInList(0);
        assertTrue(solo.searchText("Edit"));
        solo.clickOnText("Edit");
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e

        //Set trigger to Test
        solo.enterText((EditText) solo.getCurrentActivity().findViewById(R.id.triggerField), "Test");
        assertTrue(solo.searchText("Test"));

<<<<<<< HEAD
        //Set situation to Alone
        solo.clickOnText("Select a social situation");
        solo.clickOnText("Alone");
=======
        assertTrue(solo.searchText("Happiness"));   // editing happy mood to angry mood
        solo.searchText("Happiness");
        solo.clickOnText("Happiness");
        assertTrue(solo.searchText("Anger"));
        solo.clickOnText("Anger");
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e

        assertTrue(solo.searchButton("Save"));  //saving mood
        solo.clickOnButton("Save");

<<<<<<< HEAD
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

=======
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
>>>>>>> 6c4e01898ec422620da504ea32e3f8ee382ba11e

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}