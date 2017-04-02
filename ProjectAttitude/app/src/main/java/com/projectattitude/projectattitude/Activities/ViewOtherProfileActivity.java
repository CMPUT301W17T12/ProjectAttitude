package com.projectattitude.projectattitude.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;

/**
 * This class handles the viewing of other users profiles
 */
public class ViewOtherProfileActivity extends AppCompatActivity {

    protected ArrayList<Mood> recentMoodList = new ArrayList<Mood>();
    private MoodMainAdapter recentMoodAdapter;
    private ArrayAdapter<String> followUserAdapter;
    private ArrayAdapter<String> followedUserAdapter;

    private TextView nameView;
    private ListView recentMoodView;    // refers to user's most recent mood
    private ListView followUserList;
    private ListView followedUserList;
    private ImageView image;

    private User user;
    /**
     * Initial set up on creation including setting up references, adapters, and readying the search
     * button.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        image = (ImageView) findViewById(R.id.profileImage);
        nameView = (TextView) findViewById(R.id.profileUname);

        recentMoodView = (ListView) findViewById(R.id.latestMood);
        //TODO: Uncomment this later when viewOtherProfile is complete
        followUserList = (ListView) findViewById(R.id.followList);
        followedUserList = (ListView) findViewById(R.id.followedList);

        recentMoodAdapter = new MoodMainAdapter(this, recentMoodList);
        recentMoodView.setAdapter(recentMoodAdapter);

        user = (User) getIntent().getExtras().getSerializable("user");

        followUserAdapter = new ArrayAdapter<String>(this, R.layout.list_item, user.getFollowList());
        followedUserAdapter = new ArrayAdapter<String>(this, R.layout.list_item, user.getFollowedList());
        followUserList.setAdapter(followUserAdapter);
        followedUserList.setAdapter(followedUserAdapter);

        //If image exists in user, set image
        if (user.getPhoto() != null && user.getPhoto().length() > 0) {
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
                Intent intentView = new Intent(ViewOtherProfileActivity.this, ViewMoodActivity.class);
                intentView.putExtra("mood", recentMoodList.get(position));
                startActivityForResult(intentView, 1);
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
        nameView.setText(user.getUserName()); //getting the name of the user

        Mood userMood = user.getFirstMood();
        if(userMood != null && recentMoodList.size() == 0){
            recentMoodList.add(userMood);
            recentMoodAdapter.notifyDataSetChanged();
        }

        //adding recent moods for each followed person

    }

    private boolean isNetworkAvailable() {  // checks if network available for searching database
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
