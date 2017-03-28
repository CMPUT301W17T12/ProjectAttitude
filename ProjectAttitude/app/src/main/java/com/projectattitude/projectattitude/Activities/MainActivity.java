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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.FilterDecorator;
import com.projectattitude.projectattitude.Objects.FilterDecoratorHandler;
import com.projectattitude.projectattitude.Objects.FilterEmotionDecorator;
import com.projectattitude.projectattitude.Objects.FilterTimeDecorator;
import com.projectattitude.projectattitude.Objects.FilterTriggerDecorator;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.NetWorkChangeReceiver;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;

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
    private Integer itemPosition;
    private String sortingDate;

    private UserController userController = UserController.getInstance();
    private FilterDecorator filterDecorator = null;

    //Private variables for sorting/filtering


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
    //2017-03-21T17:03:03-0600 <----- stored
    //Tue Mar 21 17:16:14 MDT 2017 <----Henry print
    //2017-03-21T17:32:04-0600<------old db

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        controller = new MainController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get passed user from LoginActivity
        final User user = (User) getIntent().getSerializableExtra("PassUserToMain");
        userController.setActiveUser(user);

        moodListView = (ListView) findViewById(R.id.moodListView);

        //adapter is fed from moodList inside user
        moodAdapter = new MoodMainAdapter(this, moodList);
        //moodAdapter = new MoodMainAdapter(this, userController.getActiveUser().getMoodList());
        moodListView.setAdapter(moodAdapter);

        //Load user and mood, and update current displayed list
        userController.loadFromFile();
        Log.d("userController load", userController.getActiveUser().getMoodList().toString());
        sortingDate = "Sort";
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

        registerReceiver(netWorkChangeReceiver, new IntentFilter("networkConnectBroadcast"));   //TODO is crashing the app sometimes when returning from the profile page

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
        FloatingActionButton fabNotifications = (FloatingActionButton) findViewById(R.id.fabNotification);

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
                viewMapIntent.putExtra("user", user);
                startActivityForResult(viewMapIntent, 0);
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.close(true);
                Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                intent.putExtra("moodCount", moodList.size());
                if (moodList.size() > 0) {
                    //TODO This returns the first item in the list, but if its been sorted, that would be wrong
                    intent.putExtra("mood", moodList.get(0));
                }
                intent.putExtra("user", user);
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
         fabNotifications.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 fabMenu.close(true);
                 Intent intent = new Intent(MainActivity.this, ViewNotificationsActivity.class);
                 intent.putExtra("user", user);
                 startActivity(intent);
             }
         });
//     }

        //Sorting and filtering menu
        final Context activityContext = this;
        final ImageButton SFButton = (ImageButton) findViewById(R.id.filterButton);
        ImageButton SearchButton = (ImageButton) findViewById(R.id.searchButton);
        ImageButton ClearButton = (ImageButton) findViewById(R.id.clearButton);

        SFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activityContext, SFButton);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    //Listener for Sort/Filter Menu
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        PopupMenu popup = new PopupMenu(activityContext, SFButton);
                        MenuInflater inflater = popup.getMenuInflater();
                        FilterDecorator foundDecorator;
                        switch(item.getItemId()){
                            case R.id.dateOption:
                                item.setChecked(true);
                                sortMood("Sort");
                                break;
                            case R.id.reverseDateOption:
                                item.setChecked(true);
                                sortMood("Reverse Sort");
                                break;
                            case R.id.timeOption:
                                inflater.inflate(R.menu.time_menu, popup.getMenu());
                                foundDecorator = FilterDecoratorHandler.find(filterDecorator, "Time");
                                if(foundDecorator != null){
                                    findItemInMenu(popup.getMenu(), foundDecorator.getFilterParameter()).setChecked(true);
                                }
                                //On-click handler for time menu
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item){
                                        if(!item.isChecked()){ //If turning on filter, add/replace filter in decorator
                                            filterDecorator = FilterDecoratorHandler.findAndReplace(filterDecorator, new FilterTimeDecorator(item.getTitle().toString()));
                                        }else{ //If turning off filter, delete filter in decorator
                                            filterDecorator = FilterDecoratorHandler.findAndDelete(filterDecorator, "Time");
                                        }
                                        filterMood();
                                        item.setChecked(item.isChecked());
                                        return false;
                                    }
                                });
                                popup.show();
                                break;
                            case R.id.emotionOption:
                                inflater.inflate(R.menu.mood_menu, popup.getMenu());
                                foundDecorator = FilterDecoratorHandler.find(filterDecorator, "Emotion");
                                if(foundDecorator != null){
                                    findItemInMenu(popup.getMenu(), foundDecorator.getFilterParameter()).setChecked(true);
                                }
                                //On-click handler for emotion menu
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item){
                                        if(!item.isChecked()){ //If turning on filter, add/replace filter in decorator
                                            filterDecorator = FilterDecoratorHandler.findAndReplace(filterDecorator, new FilterEmotionDecorator(item.getTitle().toString()));
                                        }else{ //If turning off filter, delete filter in decorator
                                            filterDecorator = FilterDecoratorHandler.findAndDelete(filterDecorator, "Emotion");
                                        }
                                        filterMood();
                                        item.setChecked(item.isChecked());
                                        return false;
                                    }
                                });
                                popup.show();
                                break;
                            case R.id.allOption:
                                //Delete all filters and refresh data
                                filterDecorator = null;
                                findViewById(R.id.clearButton).setVisibility(View.INVISIBLE);
                                userController.loadFromFile();
                                refreshMoodList();
                                moodAdapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });//Code End of Listener for Sort/Filter Menu

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_filter_menu, popup.getMenu());
                //Set up checkables
                Menu popupMenu = popup.getMenu();
                if(sortingDate.equals("Sort")){
                    popupMenu.findItem(R.id.dateOption).setChecked(true);
                }else if (sortingDate.equals("Reverse Sort")){
                    popupMenu.findItem(R.id.reverseDateOption).setChecked(true);
                }
                if(FilterDecoratorHandler.find(filterDecorator, "Time") != null){
                    //If currently filtering for time, check the time menu box
                    popupMenu.findItem(R.id.timeOption).setChecked(true);
                }
                if(FilterDecoratorHandler.find(filterDecorator, "Emotion") != null){
                    //If currently filtering for emotion, check the emotion menu box
                    popupMenu.findItem(R.id.emotionOption).setChecked(true);
                }
                popup.show();
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter new trigger filter into decorator head
                filterDecorator = FilterDecoratorHandler.findAndReplace(filterDecorator,
                        new FilterTriggerDecorator(((EditText)findViewById(R.id.searchBar)).getText().toString()));
                filterMood();
                findViewById(R.id.clearButton).setVisibility(View.VISIBLE); //Make clear button visible
            }
        });

        ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDecorator = FilterDecoratorHandler.findAndDelete(filterDecorator, "Trigger");
                filterMood();
                findViewById(R.id.clearButton).setVisibility(View.INVISIBLE); //Make clear button visible
            }
        });

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
        filterMood(); //Calls refreshMoodList
        moodAdapter.notifyDataSetChanged();

        Log.d("editing", userController.getActiveUser().getMoodList().get(itemPosition).toString());


        //updating db
        ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
        updateUserTask.execute(UserController.getInstance().getActiveUser());
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

                filterMood(); //Calls refreshMoodList
                moodAdapter.notifyDataSetChanged();
                break;
            }
        }
        //updating db
        ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
        updateUserTask.execute(UserController.getInstance().getActiveUser());
    }

    /**
     * Handles sorting the list, called when an item in the sortMenu is pressed
     * @param dateSort - specifies the type of sort to sort.
     *                 Three options: "null", "Sort", "Reverse Sort"
     */
    public void sortMood(String dateSort){
        if(dateSort == null){
            controller.sortList(moodList, sortingDate); //True = sorting by date
        }
        else{
            sortingDate = dateSort;
            controller.sortList(moodList, sortingDate);
            moodAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Handles filtering the list (multi-filtering included)
     */
    public void filterMood(){
        refreshMoodList();
        if(filterDecorator != null){
            filterDecorator.filter(moodList); //Go through filter decorator
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * findItemInMenu
     * Given a menu and a menuitem, attempts to find the menuitem in menu
     * If no menu item found, returns null
     */
    public MenuItem findItemInMenu(Menu menu, String stringToCheck){
        MenuItem checkedItem = null;
        int menuSize = menu.size();
        //Loop through filter by time menu looking for checked option
        for(int i = 0; i < menuSize; i++){
            checkedItem = menu.getItem(i);
            if(stringToCheck.equals(checkedItem.getTitle().toString())){
                //Break loop when checked item is found
                break;
            }
        }
        return checkedItem;
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

                filterMood(); //Calls refreshMoodList
                moodAdapter.notifyDataSetChanged();

                //TODO: Only update moodList if displaying myMoodList, not following list, otherwise moodList = followingList
                //This to-do applies to the viewMoodActivity and EditMoodActivity result too

                Log.d("userController Added", userController.getActiveUser().getMoodList().toString());

                //update the user
//                if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
//                    ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
//                    addUserTask.execute(UserController.getInstance().getActiveUser());
//                }

                ElasticSearchUserController.UpdateUserTask updateUserTask = new ElasticSearchUserController.UpdateUserTask();
                updateUserTask.execute(UserController.getInstance().getActiveUser());
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

