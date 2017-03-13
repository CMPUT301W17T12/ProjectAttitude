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

    public void testGetId(){
        Mood mood = new Mood();
        mood.setId("1234");
        assertEquals("1234", mood.getId());
    }

    public void testSetId(){
        Mood mood = new Mood();
        mood.setId("1234");
        assertEquals("1234", mood.getId());
    }
}