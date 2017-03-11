package com.projectattitude.projectattitude;

import android.provider.ContactsContract;
import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;

import java.util.Date;

/**
 * Created by Boris on 25/02/2017.
 */

public class MoodTest extends ActivityInstrumentationTestCase2{

    public MoodTest() {
        super(MainActivity.class);
    }

    public void testGetMoodDate(){
        Mood mood = new Mood();
        Date date = new Date();
        assertTrue(mood.getMoodDate() == date);
    }

    public void testSetMoodDate(){
        Mood mood = new Mood();
        Date date = new Date();

        mood.setMoodDate(date);
        assertEquals(mood.getMoodDate(), date);
    }

    public void testGetEmotionState(){
        Mood mood = new Mood();
        mood.setEmotionState("Happy");
        assertEquals("Happy", mood.getEmotionState());
    }

    public void testSetEmotionState(){
        Mood mood = new Mood();
        mood.setEmotionState("Happy");
        assertEquals("Happy", mood.getEmotionState());
    }

    public void testGetGeoLocation(){
        Mood mood = new Mood();
        //mood.setGeoLocation();
        assertEquals("testLocation", mood.getGeoLocation());
    }

    public void testSetGeoLocation(){
        Mood mood = new Mood();
        //mood.setGeoLocation();
        assertEquals("testLocation", mood.getGeoLocation());
    }

    public void testGetTrigger(){
        Mood mood = new Mood();
        mood.setTrigger("testTrigger");
        assertEquals(mood.getTrigger(), "testTrigger");
    }

    public void testSetTrigger(){
        Mood mood = new Mood();
        mood.setTrigger("testTrigger");
        assertEquals(mood.getTrigger(), "testTrigger");
    }

    public void testGetExplanation(){
        Mood mood = new Mood();
        mood.setExplanation("testExplanation");
        assertEquals(mood.getExplanation(), "testExplanation");
    }

    public void testSetExplanation(){
        Mood mood = new Mood();
        mood.setExplanation("testExplanation");
        assertEquals(mood.getExplanation(), "testExplanation");
    }

    public void testGetPhotograph(){
        Mood mood = new Mood();
        ContactsContract.Contacts.Photo photograph = null;
        //mood.setPhotograph(photograph);
        //assertEquals(photograph, mood.getPhotograph());
    }

    public void testSetPhotograph(){
        Mood mood = new Mood();
        ContactsContract.Contacts.Photo photograph = null;
        //mood.setPhotograph(null);
        //assertEquals(null, mood.getPhotograph());
    }

    public void testGetSocialSituation(){
        Mood mood = new Mood();
        mood.setSocialSituation("Alone");
        assertEquals("Alone", mood.getSocialSituation());
    }

    public void testSetSocialSituation(){
        Mood mood = new Mood();
        mood.setSocialSituation("Alone");
        assertEquals("Alone", mood.getSocialSituation());
    }
}