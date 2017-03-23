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

package com.projectattitude.projectattitude.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * This is a temporary profile, just shows name and number of moods
 */
public class ViewProfileActivity extends AppCompatActivity {
    protected ArrayList<Mood> recentMoodList = new ArrayList<Mood>();
    protected ArrayList<Mood> followingMoodList = new ArrayList<Mood>();
    private UserController userController = UserController.getInstance();
    private MoodMainAdapter recentMoodAdapter;
    private MoodMainAdapter followingMoodAdapter;

    private Button searchButton;
    private EditText searchBar;
    private TextView nameView;
    private TextView countView;
    private ListView recentMoodView;    // refers to user's most recent mood
    private ListView followingMoodView; // refers to moods user is following
    private ArrayList<String> usersFollowed;
    private ArrayList<Mood> usersFollowedMoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        searchBar = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        nameView = (TextView) findViewById(R.id.profileUname);

        recentMoodView = (ListView) findViewById(R.id.latestMood);
        followingMoodView = (ListView) findViewById(R.id.followListView);

        recentMoodAdapter = new MoodMainAdapter(this, recentMoodList);
        recentMoodView.setAdapter(recentMoodAdapter);

        followingMoodAdapter = new MoodMainAdapter(this, followingMoodList);
        followingMoodView.setAdapter(followingMoodAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Profile setup
        nameView.setText(userController.getActiveUser().getUserName()); //getting the name of the user

        int moodCount = (int) getIntent().getSerializableExtra("moodCount");

        Mood userMood;
        if (moodCount > 0) {
            //Adding the mood to the user's most recent mood
            userMood = (Mood) getIntent().getSerializableExtra("mood");
            recentMoodList.add(userMood);
            recentMoodAdapter.notifyDataSetChanged();

            //adding recent moods for each follower
            User user = (User) getIntent().getSerializableExtra("user");
            usersFollowed = user.getFollowList();
            if(usersFollowed != null){
                for(int i = 0; i < usersFollowed.size(); i++){
                    String stringFollowedUser = usersFollowed.get(i);
                    ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
                    try {
                        User followedUser = getUserTask.execute(stringFollowedUser).get();
                        Mood userFollowedMood = followedUser.getFirstMood();
                        usersFollowedMoods.add(userFollowedMood);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            followingMoodList.add(userMood);    //TODO Temporary place holder, remove

            followingMoodAdapter.notifyDataSetChanged();

        }
    }

}
