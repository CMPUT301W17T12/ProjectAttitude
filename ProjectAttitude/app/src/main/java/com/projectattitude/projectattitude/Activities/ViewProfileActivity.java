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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * This is the profile page of the user. It shows the name of the user, as well as the portrait,
 * followed by the user's most recent mood. From here, the user can add moods to follow
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
    private ImageView image;
    private Activity thisActivity = this;
    String s = "";

    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * Initial set up on creation including setting up references, adapters, and readying the search
     * button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        searchBar = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        image = (ImageView) findViewById(R.id.profileImage);
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
                        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
                        try{
                            if (getUserTask.execute(followedUser.getUserName()).get() == null){
                                Log.d("Error", "User did not exist");
                                Toast.makeText(ViewProfileActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Error", "User did exist");
                                //grab user from db and add to following list
                                getUserTask = new ElasticSearchUserController.GetUserTask();
                                try {
                                    followedUser = getUserTask.execute(followingName).get();
                                    if(followedUser != null){   // user exists
                                        if(followedUser.getUserName().equals(user.getUserName())){
                                            Toast.makeText(ViewProfileActivity.this, "You cannot be friends with yourself. Ever", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            if(user.getFollowList().contains(followedUser.getUserName())){
                                                Toast.makeText(ViewProfileActivity.this, "You're already following that user.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{// user not already in list
                                                //check if request between users already exists in database
                                                ElasticSearchRequestController.CheckRequestTask checkRequestTask = new ElasticSearchRequestController.CheckRequestTask();
                                                checkRequestTask.execute(user.getUserName(), followedUser.getUserName());
                                                if(checkRequestTask.get().size() == 0){ //request doesn't exists - not sure why .get always returns an filled array or empty array
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
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                    else{
                        Toast.makeText(ViewProfileActivity.this, "Must be connected to internet to search for users!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //If image exists in user, set image
        if(user.getPhoto().length() > 0){
            //decode base64 image stored in User
            byte[] imageBytes = Base64.decode(user.getPhoto(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(decodedImage);
        }

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

        //Adjusted from http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
        //on 3/29/17
        /**
         * This handles when the user clicks on their image
         */
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Check if user has permission to get picture from gallery
               if(ContextCompat.checkSelfPermission(thisActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(thisActivity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
               }else{ //user already has permission
                   Intent intent = new Intent();
                // Show only images, no videos or anything else
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                   startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
               }
           }
        });
    }

    /**
     * This method is called as soon as the activity starts,
     */
    @Override
    protected void onStart(){
        super.onStart();

        //Profile setup
        nameView.setText(userController.getActiveUser().getUserName()); //getting the name of the user

        int moodCount = (int) getIntent().getSerializableExtra("moodCount");
        User user = (User) getIntent().getSerializableExtra("user");

        Mood userMood = (Mood) getIntent().getSerializableExtra("mood");    // getting user mood
        if(userMood != null && recentMoodList.size() == 0){
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                //if greater then byte threshold, compress
                if (bitmap.getByteCount() > 65536) {
                    //4 or less times greater, scale by 2
                    if (bitmap.getByteCount() / 65536 <= 4) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() / 2), (bitmap.getHeight() / 2), false);
                    }
                    //9 or less times greater, scale by 3
                    else if (bitmap.getByteCount() / 65536 <= 9) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() / 3), (bitmap.getHeight() / 3), false);
                    }
                    else {
                        //anything greater then 9 times, scale by 4
                        bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() / 4), (bitmap.getHeight() / 4), false);
                    }
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                s = Base64.encodeToString(byteArray, Base64.DEFAULT);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //TODO: Update user with profile picture
            userController.getActiveUser().setPhoto(s);
            ElasticSearchUserController.UpdateUserPictureTask updateUserPictureTask = new ElasticSearchUserController.UpdateUserPictureTask();
            updateUserPictureTask.execute(UserController.getInstance().getActiveUser());

        }
    }

    //When return from requesting read external storage permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: //If requesting gallery permissions
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //If result cancelled, grantResults is empty
                    //If permissions granted, activate image button again
                    image.performClick();
                }
        }
    }


    }
