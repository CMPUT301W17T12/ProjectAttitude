package com.projectattitude.projectattitude;

import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

/**
 * Created by Boris on 25/02/2017.
 * These tests check if the mood object behaves as it should.
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
        mood.setGeoLocation(new GeoPoint(0f, 0f));
        assertEquals(new GeoPoint(0f, 0f), mood.getGeoLocation());

    }

    public void testSetGeoLocation(){
        Mood mood = new Mood();

        mood.setGeoLocation(new GeoPoint(0f, 0f));
        assertEquals(new GeoPoint(0f, 0f), mood.getGeoLocation());
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

    public void testGetImage(){
        Mood mood = new Mood();

        Image image = null;
        mood.setImage(image);
        assertEquals(image, mood.getImage());
    }

    public void testSetImage(){
        Mood mood = new Mood();

        Image image = null;
        mood.setImage(image);
        assertEquals(image, mood.getImage());
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