package com.projectattitude.projectattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projectattitude.projectattitude.R;

/**
 * This activity will handle the viewing and handling of notifications
 * such as follow requests
 */
public class ViewNotificationsActivity extends AppCompatActivity {

    //I created a notification_item.xml to handle notifications
    //On second thought we could use toast pop ups to handle instead of buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
    }
}
