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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchRequestController;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.FollowRequest;
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
    private ArrayList<Mood> usersFollowedMoods = new ArrayList<Mood>();

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

        final User user = userController.getActiveUser();

        searchButton.setOnClickListener(new View.OnClickListener() {    // adding a new user to following list
            @Override
            public void onClick(View v) {

                String followingName = searchBar.getText().toString();

                User followedUser = new User();
                followedUser.setUserName(followingName);

                if (followingName.equals("")) {   // no username entered to search for
                    searchBar.requestFocus(); // search has been canceled
                }

                else {
                    if(isNetworkAvailable()){
                        if (ElasticSearchUserController.getInstance().verifyUser(followedUser)){
                            Log.d("Error", "User did not exist");

                        } else {
                            Log.d("Error", "User did exist");
                            //grab user from db and add to following list
                            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
                            try {
                                followedUser = getUserTask.execute(followingName).get();
                                if(followedUser != null){   // user exists
                                    if(followedUser.getUserName().equals(user.getUserName())){
                                        Toast.makeText(ViewProfileActivity.this, "You cannot be friends with yourself. Ever", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(user.getFollowedList().contains(followedUser.getUserName())){
                                            Toast.makeText(ViewProfileActivity.this, "You're already following that user.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{// user not already in list
                                            //check if request between users already exists in database
                                            ElasticSearchRequestController.CheckRequestTask checkRequestTask = new ElasticSearchRequestController.CheckRequestTask();
                                            checkRequestTask.execute(user.getUserName(),followedUser.getUserName());
                                            if(checkRequestTask.get() == null){// request does not already exist
                                                ElasticSearchRequestController.AddRequestTask addRequestTask = new ElasticSearchRequestController.AddRequestTask();
                                                addRequestTask.execute(new FollowRequest(user.getUserName(),followedUser.getUserName()));

                                                Toast.makeText(ViewProfileActivity.this, "Request sent!", Toast.LENGTH_SHORT).show();
                                            }else{ // request exists
                                                Toast.makeText(ViewProfileActivity.this, "Request already exists.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        Toast.makeText(ViewProfileActivity.this, "Must be connected to internet to search for users!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /**
         * This handles when a user clicks on their most recent mood, taking them to the view mood screen
         */
        recentMoodView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentView = new Intent(ViewProfileActivity.this, ViewMoodActivity.class);
                intentView.putExtra("mood", recentMoodList.get(position));
                startActivityForResult(intentView, 1);
            }
        });

        /**
         * This handles when a user clicks on a followed mood, taking them to the view mood screen
         */
        followingMoodView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentView = new Intent(ViewProfileActivity.this, ViewMoodActivity.class);
                intentView.putExtra("mood", followingMoodList.get(position));
                startActivityForResult(intentView, 1);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        //Profile setup
        nameView.setText(userController.getActiveUser().getUserName()); //getting the name of the user

        int moodCount = (int) getIntent().getSerializableExtra("moodCount");
        User user = (User) getIntent().getSerializableExtra("user");

        Mood userMood = (Mood) getIntent().getSerializableExtra("mood");    // getting user mood
        if(userMood != null){
            recentMoodList.add(userMood);
            recentMoodAdapter.notifyDataSetChanged();
        }

        //adding recent moods for each followed person

        usersFollowed = userController.getActiveUser().getFollowedList();
        if(usersFollowed != null){
            for(int i = 0; i < usersFollowed.size(); i++){
                String stringFollowedUser = usersFollowed.get(i);
                ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
                try {
                    User followedUser = getUserTask.execute(stringFollowedUser).get();
                    if(followedUser != null){
                        if(followedUser.getFirstMood() != null){
                            followingMoodList.add(followedUser.getFirstMood());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        followingMoodAdapter.notifyDataSetChanged();

    }

    private boolean isNetworkAvailable() {  // checks if network available for searching database
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
