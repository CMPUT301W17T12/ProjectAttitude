package com.projectattitude.projectattitude;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.projectattitude.projectattitude.Activities.LoginActivity;
import com.projectattitude.projectattitude.Activities.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by Boris on 12/03/2017.
 * Tests the login screen functionality. Assumes internet is functional.
 */


public class LoginActivityUITest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginActivityUITest() {
        super(com.projectattitude.projectattitude.Activities.LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testLogin(){
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
        solo.enterText((EditText)solo.getView(R.id.usernameField), "tester");

        //solo.enterText((EditText)solo.getView(R.id.passwordField), "hello"); password is currently disabled

        solo.clickOnView(solo.getView(R.id.signInButton));

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}