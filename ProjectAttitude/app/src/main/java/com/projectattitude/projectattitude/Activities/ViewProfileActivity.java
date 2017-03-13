package com.projectattitude.projectattitude.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;


/**
 * This is a temporary profile, just shows name and number of moods
 */
public class ViewProfileActivity extends AppCompatActivity {
    private UserController userController = UserController.getInstance();

    private TextView nameView;
    private TextView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        nameView = (TextView) findViewById(R.id.profileUname);
        countView = (TextView) findViewById(R.id.profileCount);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Integer count = getIntent().getIntExtra("moodCount", 0);
        nameView.setText(userController.getActiveUser().getUserName());
        countView.setText("Number of moods: " + Integer.toString(count));
    }

}
