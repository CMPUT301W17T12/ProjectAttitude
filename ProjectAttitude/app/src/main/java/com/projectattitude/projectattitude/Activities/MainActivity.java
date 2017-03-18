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
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.NetWorkChangeReceiver;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.Date;

import io.fabric.sdk.android.Fabric;

/**
 * The MainActivity is where the primary information for the user can be found. This is achieved by
 * syncing to the database individually for each username. A list of moods
 * is displayed filled with moods created by the user. Long clicking on a mood will provide
 * additional options such as to view the mood, edit the mood or delete the mood. Filtering
 * is available in the top right corner, as well as search functionality. For filtering, after
 * applying a filter, you must click on filter, then "All Moods" to refresh the list to filter.
 * Searching only works for searching through reasons (triggers) of moods
 */
public class MainActivity extends AppCompatActivity {

    protected ArrayList<Mood> moodList = new ArrayList<Mood>();
    private MoodMainAdapter moodAdapter;
    private ListView moodListView;
    private MainController controller;
    private boolean viewingMyList;
    private Integer itemPosition;

    private UserController userController = UserController.getInstance();

    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetWorkChangeReceiver receiver;
    private boolean isConnected = false;

    private  int listItem; //This is the index of the item pressed in the list

    // https://trinitytuts.com/pass-data-from-broadcast-receiver-to-activity-without-reopening-activity/
    // Network listener to sync when connecting to network
    NetWorkChangeReceiver netWorkChangeReceiver = new NetWorkChangeReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
        if(isNetworkAvailable()){
            if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
                ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
                addUserTask.execute(UserController.getInstance().getActiveUser());
            }
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new MainController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get passed user from LoginActivity
        User user = (User) getIntent().getSerializableExtra("PassUserToMain");
        userController.setActiveUser(user);

        moodListView = (ListView) findViewById(R.id.moodListView);

        //adapter is fed from moodList inside user
        moodAdapter = new MoodMainAdapter(this, moodList);
        //moodAdapter = new MoodMainAdapter(this, userController.getActiveUser().getMoodList());
        moodListView.setAdapter(moodAdapter);
        viewingMyList = false;

        //Load user and mood, and update current displayed list
        userController.loadFromFile();
        Log.d("userController load", userController.getActiveUser().getMoodList().toString());
        refreshMoodList();

        registerForContextMenu(moodListView);

        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentView = new Intent(MainActivity.this, ViewMoodActivity.class);
                intentView.putExtra("mood", moodList.get(position));
                startActivityForResult(intentView, 1);
            }
        });

        registerReceiver(netWorkChangeReceiver, new IntentFilter("networkConnectBroadcast"));

        // twitter init
        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        // sync on start
        if(isNetworkAvailable()) {
            if (ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())) {
                ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
                addUserTask.execute(UserController.getInstance().getActiveUser());
            }
        }

        try{
            ArrayList<Mood> tempList = userController.getActiveUser().getMoodList();
            Log.d("moodlist1", tempList.toString());
            refreshMoodList();
            Log.d("moodList2", moodList.toString());
        }
        catch(Exception e){
            Log.d("Error", "Failed to get the moods from the async object");
        }

        // Floating action menu
        final FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
        FloatingActionButton fabAddMood = (FloatingActionButton) findViewById(R.id.fabAddMood);
        FloatingActionButton fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        FloatingActionButton fabProfile = (FloatingActionButton) findViewById(R.id.fabProfile);
        FloatingActionButton fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);

        // on click listener for adding moods
        fabAddMood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                createMood();
            }
        });

        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.close(true);
                Intent viewMapIntent = new Intent(MainActivity.this, MapActivity.class);
                startActivityForResult(viewMapIntent, 0);
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.close(true);
                Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                intent.putExtra("moodCount", moodList.size());
                startActivity(intent);
            }
        });

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.close(true);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    //-------POPUP MENU FUNCTIONS-------
    /**
     * OpenSFMenu - Open Sort/Filter Menu
     * Is used when the sort/filter button is pressed to display a menu
     * @param view - the sort/filter button
     * @see #openSortMenu(MenuItem)
     * @see #openFilterMenu(MenuItem)
     */
    public void openSFMenu(View view){
        //TODO: Test all this popupmenu crap
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_filter_menu, popup.getMenu());
        popup.show();
    }

    /**
     * openSortMenu
     * Is used when the sort option in sort/filter menu is pressed to display a menu
     * @param item - the sort option in sort_filter_menu
     * @see #sortMood(MenuItem)
     */
    public void openSortMenu(MenuItem item){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_menu, popup.getMenu());
        popup.show();
    }

    /**
     * openFilterMenu
     * Is used when the filter option in sort/filter menu is pressed to display a menu
     * @param item - the filter option in sort_filter_menu
     * @see #filterMood(MenuItem)
     */
    public void openFilterMenu(MenuItem item){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_menu, popup.getMenu());
        popup.show();
    }

    /**
     * This method will take the user to the Create Mood view
     */
    private void createMood(){
        Intent createMoodIntent = new Intent(MainActivity.this, CreateMoodActivity.class);
        startActivityForResult(createMoodIntent, 0);
    }

    /**
     * This method takes a mood the user made and brings them to the edit mood view
     */
    private void editMood(Mood returnedMood){
        Mood moodCheck = userController.getActiveUser().getMoodList().get(itemPosition);
        Log.d("moodCheckEdit", moodCheck.getEmotionState() + " " + moodCheck.getMoodDate() + " " + moodCheck.getTrigger() + " " + moodCheck.getSocialSituation());
        userController.getActiveUser().getMoodList().set(itemPosition, returnedMood);
        userController.saveInFile();
        refreshMoodList();
        moodAdapter.notifyDataSetChanged();

        Log.d("editing", userController.getActiveUser().getMoodList().get(itemPosition).toString());

        //updating db
        if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(UserController.getInstance().getActiveUser());
        }
    }

    /**
     * This deletes a selected mood.
     * @param i, the integer of the moodList to be removed
     */
    private void deleteMood(Integer i){
        //Log.d("deleting", moodList.get(i).toString());
        //Mood delMood = moodList.get(i);
        Log.d("deleting", userController.getActiveUser().getMoodList().get(i).toString());

       //Mood moodCheck = userController.getActiveUser().getMoodList().get(itemPosition);
        //Log.d("moodCheckDelete", moodCheck.getEmotionState() + " " + moodCheck.getMoodDate() + " " + moodCheck.getTrigger() + " " + moodCheck.getSocialSituation());

        ArrayList<Mood> tmpList = userController.getActiveUser().getMoodList();
        for (int j = 0; j < tmpList.size(); j++) {
            if (tmpList.get(j).equals(moodList.get(i))) {
                Mood delMood = userController.getActiveUser().getMoodList().get(j);
                Mood moodCheck = userController.getActiveUser().getMoodList().get(j);
                Log.d("moodCheckDelete", moodCheck.getEmotionState() + " " + moodCheck.getMoodDate() + " " + moodCheck.getTrigger() + " " + moodCheck.getSocialSituation());
                //moodList = controller.getMyMoodList().getMoodList();
                //moodList.remove(delMood);
                userController.getActiveUser().getMoodList().remove(delMood);
                //controller.setMyMoodList(new MoodList(moodList));
                //Log.d("deleting", moodList.get(i).toString());
                userController.saveInFile();
                Log.d("userController deleted", userController.getActiveUser().getMoodList().toString());

                refreshMoodList();
                moodAdapter.notifyDataSetChanged();
                break;
            }
        }
        //updating db
        if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(UserController.getInstance().getActiveUser());
        }
    }

    /**
     * Handles sorting the list, called when an item in the sortMenu is pressed
     * @param item - one of the sort options from the sort menu
     */
    public void sortMood(MenuItem item){
        if(item == null){
            controller.sortList(moodList, "Sort"); //True = sorting by date
            return;
        }
        switch (item.getItemId()) {
            case R.id.dateOption:
                controller.sortList(moodList, "Sort"); //True = sorting by date
                break;
            case R.id.reverseDateOption:
                controller.sortList(moodList, "Reverse Sort"); //False = sorting by reverse date
                break;
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * Handles filtering the list
     * @param item - one of the options from the filter menu
     * @see #filterMoodsByEmotion(MenuItem)
     * @see #filterMoodsByTime(MenuItem)
     */
    public void filterMood(MenuItem item){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
        MenuInflater inflater = popup.getMenuInflater();
        switch (item.getItemId()) {
            case R.id.timeOption:
                inflater.inflate(R.menu.time_menu, popup.getMenu());
                popup.show();
                break;

            case R.id.followingOption:
                //TODO: Following
                viewingMyList = !viewingMyList;
                break;

            case R.id.emotionOption:
                inflater.inflate(R.menu.mood_menu, popup.getMenu());
                popup.show();
                break;

            case R.id.allOption:
                //TODO: Add following to allOption
                userController.loadFromFile();
                refreshMoodList();
                 moodAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * This is the method that handles finding moods with a given keyword
     * Called by pressing the searchButton on main_layout
     */
    public void filterMoodByTrigger(View view){
        //Get text from search bar and then call controller function
        controller.filterListByTrigger(moodList, ((EditText)findViewById(R.id.searchBar)).getText().toString());
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * Handles filtering the list, but specifically for the time menu
     * @param item
     */
    public void filterMoodsByTime(MenuItem item){
        //TODO: Make sure moods are up to date?
        switch (item.getItemId()) {
            case R.id.dayOption:
                controller.filterListByTime(moodList, (long)8.64e+7); //1 day's worth of milliseconds
                break;

            case R.id.weekOption:
                controller.filterListByTime(moodList, (long)6.048e+8); //1 week's worth of milliseconds
                break;

            case R.id.monthOption:
                controller.filterListByTime(moodList, (long)2.628e+9); //1 month's worth of milliseconds approximately
                break;

            case R.id.yearOption:
                controller.filterListByTime(moodList, (long)3.154e+10); //1 year's worth of milliseconds approximately
                break;
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * Handles filtering the list, but specifically for the mood menu
     * @param item - option from the filter emotion menu
     */
    public void filterMoodsByEmotion(MenuItem item){
        Long milliseconds = new Date().getTime();
        switch (item.getItemId()) {
            case R.id.angerOption:
                controller.filterListByEmotion(moodList, "Anger"); //1 day's worth of milliseconds
                break;

            case R.id.confusionOption:
                controller.filterListByEmotion(moodList, "Confusion"); //1 day's worth of milliseconds
                break;

            case R.id.disgustOption:
                controller.filterListByEmotion(moodList, "Disgust"); //1 day's worth of milliseconds
                break;

            case R.id.fearOption:
                controller.filterListByEmotion(moodList, "Fear"); //1 day's worth of milliseconds
                break;

            case R.id.happinessOption:
                controller.filterListByEmotion(moodList, "Happiness"); //1 day's worth of milliseconds
                break;

            case R.id.sadnessOption:
                controller.filterListByEmotion(moodList, "Sadness"); //1 day's worth of milliseconds
                break;

            case R.id.shameOption:
                controller.filterListByEmotion(moodList, "Shame"); //1 day's worth of milliseconds
                break;

            case R.id.surpriseOption:
                controller.filterListByEmotion(moodList, "Surprise"); //1 day's worth of milliseconds
                break;
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * refreshMood - Used to refresh the mood list with the most current moods.
     * Currently works by using the global variable moodList
     */
    public void refreshMoodList(){
        //TODO: Add following and perhaps make algorithm more elegant
        ArrayList<Mood> newList = userController.getActiveUser().getMoodList();
        moodList.clear();
        moodList.addAll(newList);
        sortMood(null);
    }

    //accept returned information from activities
    @Override
    // requestCode 0 = Add mood
    // requestCode 1 = View mood -- resultCode 2 = delete, 3 = Edit Mood
    // requestCode 2 = Edit Mood
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Mood returnedMood;

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                returnedMood = (Mood) data.getSerializableExtra("addMoodIntent");

                userController.getActiveUser().getMoodList().add(returnedMood);

                Mood moodCheck = returnedMood;
                Log.d("moodCheckAdd", moodCheck.getEmotionState() + " " + moodCheck.getMoodDate() + " " + moodCheck.getTrigger() + " " + moodCheck.getSocialSituation());

                userController.saveInFile();

                refreshMoodList();
                moodAdapter.notifyDataSetChanged();

                //TODO: Only update moodList if displaying myMoodList, not following list, otherwise moodList = followingList
                //This to-do applies to the viewMoodActivity and EditMoodActivity result too

                Log.d("userController Added", userController.getActiveUser().getMoodList().toString());

                //update the user
                if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
                    ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
                    addUserTask.execute(UserController.getInstance().getActiveUser());
                }
            }

        }

        //ViewMoodActivity results
        if (requestCode == 1){
            //ViewMoodActivity says delete the mood
            if (resultCode == 2){
                deleteMood(itemPosition);
            }
            //ViewMoodActivity says edit
            if (resultCode == 3){
                returnedMood = (Mood) data.getSerializableExtra("newMood");
                editMood(returnedMood);
            }
        }

        //EditMoodActivity results
        if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                returnedMood = (Mood) data.getSerializableExtra("mood");
                editMood(returnedMood);
            }
        }
    }


    /**
     * Taken from
     * http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
     * on 2/5/2017
     * @param menu Takes in the menu list
     * @param v the main view
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.moodListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }
    /**
     * This method handles long presses on an item.
     * When a long press is recorded a menu pops up with the option to edit or delete a record
     * Taken from http://stackoverflow.com/questions/17207366/creating-a-menu-after-a-long-click-event-on-a-list-view
     * on 5/5/2017
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        itemPosition = info.position;
        Log.d("Adapter click position", itemPosition+"");
        boolean edit = true; //For some reason view as also bringing up the edit window
        //This bool fixes that
        switch(item.getItemId()) {
            case R.id.edit: //When edit is pressed
                if (edit) {
                    Intent intentEdit = new Intent(MainActivity.this, EditMoodActivity.class);
//                    intentEdit.putExtra("mood", moodList.get(itemPosition));
                    intentEdit.putExtra("mood", moodList.get(itemPosition));
                    startActivityForResult(intentEdit, 2); //Handled in the results section
                    //calculate itemPosition in user list
                    ArrayList<Mood> tmpList = userController.getActiveUser().getMoodList();
                    for (int i = 0; i < tmpList.size(); i++) {
                        if (tmpList.get(i).equals(moodList.get(itemPosition))) {
                            itemPosition = i;
                            break;
                        }
                    }
                    listItem = itemPosition;
                }
                return true;

            case R.id.delete: //When delete is pressed the item is removed, and everything is updated
                deleteMood(itemPosition);
                return true;
            // When tweet is pressed TODO build a proper string
            case R.id.tweet:
                TweetComposer.Builder builder = new TweetComposer.Builder(this)
                        .text("Today I'm feeling " + moodList.get(itemPosition).toString());
                builder.show();
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Taken from http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
     * @return a bool if the device is connected to the internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}

