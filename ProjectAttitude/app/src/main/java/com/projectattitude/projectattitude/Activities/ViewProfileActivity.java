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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        final User user = (User) getIntent().getSerializableExtra("user");

        searchButton.setOnClickListener(new View.OnClickListener() {    // adding a new user to following list
            @Override
            public void onClick(View v) {

                String followingName = searchBar.getText().toString();

                User followedUser = new User();
                followedUser.setUserName(followingName);
                Boolean cancel = false;

                if(followingName.equals("")){   // no username entered to search for
                    cancel = true;
                }

                if (cancel) {   // search has been canceled
                    searchBar.requestFocus();
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
                                    //if(user.getFollowedList().contains(followedUser.getUserName()) == false){   // user not already in list
                                    user.addFollowed(followedUser.getUserName());    // followed people stored as string
                                    if(followedUser.getFirstMood() != null){
                                        followingMoodList.add(followedUser.getFirstMood());

                                        followingMoodAdapter.notifyDataSetChanged();
                                    }

                                    userController.getActiveUser().addFollowed(followedUser.getUserName());
                                    Log.d("Error", "Followed list = " + userController.getActiveUser().getFollowedList().toString());
                                    userController.saveInFile();

                                    ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
                                    updateUserTask.execute(UserController.getInstance().getActiveUser());
                                    //}
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
                    if(followedUser.getFirstMood() != null){
                        followingMoodList.add(followedUser.getFirstMood());
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
