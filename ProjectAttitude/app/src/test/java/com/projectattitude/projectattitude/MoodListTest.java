package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;

import java.util.ArrayList;

/**
 * Created by Boris on 25/02/2017.
 */

public class MoodListTest extends ActivityInstrumentationTestCase2{

    public MoodListTest() {
        super(MainActivity.class);
    }

    public void testAddMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);

    }

    public void testDeleteMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);
        moods.deleteMood(mood);
        assertEquals(moods.getCount(), 0);
    }

    public void testGetMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);
        assertEquals(mood, moods.getMood(0));
    }

    public void testGetMoodList(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();
        ArrayList<Mood> testMoodList = new ArrayList<Mood>();
        testMoodList.add(mood);

        moods.addMood(mood);
        assertEquals(moods.getMoodList(), testMoodList); //TODO not sure if this is the proper way to check this
    }

    public void testCloneMoodList(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();
        ArrayList<Mood> testMoodList;
    }
}