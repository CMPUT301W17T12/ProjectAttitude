package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;

import java.util.ArrayList;

/**
 * Created by Boris on 25/02/2017.
 */
/**
 * These tests check if the list object for storing moods works properly.
 */
public class MoodListTest extends ActivityInstrumentationTestCase2 {

    /**
     * Instantiates a new Mood list test.
     */
    public MoodListTest() {
        super(MainActivity.class);
    }

    /**
     * Test create mood.
     */
    public void testCreateMood() {
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);

        ArrayList<Mood> arrayMoodList = new ArrayList<Mood>();
        arrayMoodList.add(mood);
        MoodList anotherMoodList = new MoodList(arrayMoodList);

        assertEquals(moods, anotherMoodList);
    }

    /**
     * Test add mood.
     */
    public void testAddMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);
    }

    /**
     * Test delete mood.
     */
    public void testDeleteMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);
        moods.deleteMood(mood);
        assertEquals(moods.getCount(), 0);

        moods.addMood(mood);
        Mood testMood = new Mood();
        moods.addMood(testMood);

        assertEquals(moods.getCount(), 2);
        moods.deleteMood(1);
        assertEquals(moods.getCount(), 1);

        assertEquals(moods.getMood(0), mood);

    }

    /**
     * Test get mood.
     */
    public void testGetMood(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertTrue(moods.hasMood(mood));
        assertEquals(moods.getCount(), 1);
        assertEquals(mood, moods.getMood(0));
    }

    /**
     * Test get count.
     */
    public void testGetCount(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();

        moods.addMood(mood);
        assertEquals(moods.getCount(), 1);
        Mood secondMood = new Mood();
        moods.addMood(secondMood);

        assertEquals(moods.getCount(), 2);
        moods.deleteMood(0);

        assertEquals(moods.getCount(), 1);
        assertEquals(moods.getMood(0), secondMood);
    }


    /**
     * Test get mood list.
     */
    public void testGetMoodList(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();
        ArrayList<Mood> testMoodList = new ArrayList<Mood>();
        testMoodList.add(mood);

        moods.addMood(mood);
        assertEquals(moods.getMoodList(), testMoodList);
    }

    /**
     * Test clone mood list.
     */
    public void testCloneMoodList(){
        MoodList moods = new MoodList();
        Mood mood = new Mood();
        ArrayList<Mood> testMoodList = new ArrayList<Mood>();

        testMoodList.add(mood);
        moods.addMood(mood);
        assertEquals(testMoodList, moods.getMoodList());
    }
}