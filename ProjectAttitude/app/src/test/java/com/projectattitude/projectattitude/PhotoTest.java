package com.projectattitude.projectattitude;

import android.test.ActivityInstrumentationTestCase2;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Photo;

/**
 * Created by bfleyshe on 3/13/17.
 */

public class PhotoTest extends ActivityInstrumentationTestCase2 {

    public PhotoTest() {
        super(MainActivity.class);
    }

    public void testGetPhoto(){
        Photo photo = new Photo();

        photo.setPhoto("testPhoto");
        assertEquals("testPhoto", photo.getPhoto());
    }


    public void testSetPhoto(){
        Photo photo = new Photo();

        photo.setPhoto("testPhoto");
        assertEquals("testPhoto", photo.getPhoto());
    }
}
