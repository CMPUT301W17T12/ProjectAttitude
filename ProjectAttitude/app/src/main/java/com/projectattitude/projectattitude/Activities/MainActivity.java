package com.projectattitude.projectattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.NetWorkUtil;
import com.projectattitude.projectattitude.Objects.Photo;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The MainActivity is where the primary information for the user can be found. This is achieved by
 * syncing to the database individually for each username. A list of moods
 * is displayed filled with moods created by the user. Long clicking on a mood will provide
 * additional options such as to view the mood, edit the mood or delete the mood. Filtering
 * is available in the top right corner, as well as search functionality.
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
    //private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    private  int listItem; //This is the index of the item pressed in the list
    private NetWorkUtil netWorkUtil = new NetWorkUtil();

    TimerTask mTimerTask;
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new MainController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get passed user from LoginActivity
        User user = (User) getIntent().getSerializableExtra("PassUserToMain");
        userController.setActiveUser(user);

        moodListView = (ListView) findViewById(R.id.moodListView);
        FloatingActionButton addMoodButton = (FloatingActionButton) findViewById(R.id.addMoodButton);
        //moodAdapter = new MoodMainAdapter(this, moodList);
        //adapter is fed from moodList inside user
        moodAdapter = new MoodMainAdapter(this, moodList); //userController.getActiveUser().getMoodList()
        moodListView.setAdapter(moodAdapter);
        viewingMyList = false;
        Button viewMapButton = (Button) findViewById(R.id.viewMapButton);

        //Load user and mood, and update current displayed list
        userController.loadFromFile();
        Log.d("userController load", userController.getActiveUser().getMoodList().toString());
        refreshMoodList();

        registerForContextMenu(moodListView);

        //on click listener for adding moods
        addMoodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createMood();
            }
        });

        //on click listener for viewing map
        viewMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMap();
            }
        });

        //check if person is online every 30 seconds, and updates the db every time there is a connection
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                if(isNetworkAvailable()){
//                    if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
//                        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
//                        addUserTask.execute(UserController.getInstance().getActiveUser());
//                    }
//                }
//            }
//        };
//
//        mTimer = new Timer();
//        /**1st argument: task to be scheduled
//         * 2nd argument: delay before task is executed
//         * 3rd arugument: delay between successive executions
//         */
//        mTimer.scheduleAtFixedRate(mTimerTask, 1000, 30000);    // time in millisec, = 30 second intervals

        try{
//            ArrayList<Mood> tempList = getMoodsTask.get();
            ArrayList<Mood> tempList = userController.getActiveUser().getMoodList();
            Log.d("moodlist1", tempList.toString());
            refreshMoodList();
            Log.d("moodList2", moodList.toString());
        }
        catch(Exception e){
            Log.d("Error", "Failed to get the moods from the async object");
        }

//        controller.setMyMoodList(new MoodList(moodList));
//        moodAdapter = new MoodMainAdapter(this, moodList);
//        moodListView.setAdapter(moodAdapter);
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
     * OpenMainMenu
     * Is used when the main menu button is pressed to display a menu
     * @param view - the main menu button
     */
    public void openMainMenu(View view){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.menuButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.show();
    }
    //---------------------------------------------

    //TODO Build these functions
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
        userController.getActiveUser().getMoodList().set(itemPosition,returnedMood);
        userController.saveInFile();
        refreshMoodList();
        moodAdapter.notifyDataSetChanged();

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
        Mood delMood = userController.getActiveUser().getMoodList().get(i);
        //moodList = controller.getMyMoodList().getMoodList();
        //moodList.remove(delMood);
        userController.getActiveUser().getMoodList().remove(delMood);
        //controller.setMyMoodList(new MoodList(moodList));
        //Log.d("deleting", moodList.get(i).toString());
        userController.saveInFile();
        Log.d("userController deleted", userController.getActiveUser().getMoodList().toString());

        refreshMoodList();
        moodAdapter.notifyDataSetChanged();

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
     * When the user clicks the map button this takes them to the map view
     */

    private void goToMap(){
        Intent viewMapIntent = new Intent(MainActivity.this, MapActivity.class);
        startActivityForResult(viewMapIntent, 0);
    }

    /**
     * When the user clicks the profile button it will take them to the profile view
     * Later may take a profile as an argument to go to someone elses profile.
     */
    public void viewProfile(MenuItem item){
    }

    /**
     * Logs the current profile out of the application and returns the user to the log in view.
     * No data will be saved if user logs out while offline
     * and you will not be able to log back in when offline
     */
    public void logOut(MenuItem item){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
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
    }

    //accept returned information from activities
    @Override
    // requestCode 0 = Add mood
    // requestCode 1 = View mood -- resultCode 2 = delete, 3 = Edit Mood
    // requestCode 2 = Edit Mood
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Mood returnedMood;
        Photo returnedPhoto;

        //CreateMoodActivity results, updating mood listview
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                returnedMood = (Mood) data.getSerializableExtra("addMoodIntent");
                returnedPhoto = (Photo) data.getSerializableExtra("addPhotoIntent");

                //moodList.add(returnedMood);
                userController.getActiveUser().getMoodList().add(returnedMood);
                userController.saveInFile();

                refreshMoodList();
                moodAdapter.notifyDataSetChanged();
                //RefreshMoodList accomplishes the next 2 lines of code already
                //moodList.add(returnedMood);
                //controller.setMyMoodList(new MoodList(moodList));
                //TODO: Only update moodList if displaying myMoodList, not following list, otherwise moodList = followingList
                //This to-do applies to the viewMoodActivity and EditMoodActivity result too

                Log.d("userController Added", userController.getActiveUser().getMoodList().toString());

//                if(returnedPhoto.getPhoto() == ""){
                    if(ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())){
                        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
                        addUserTask.execute(UserController.getInstance().getActiveUser());
                    }
//                }
//                else {
//                    ElasticSearchUserController.AddPhotoTask addPhotoTask = new ElasticSearchUserController.AddPhotoTask();
//                    addPhotoTask.execute(returnedPhoto);
//
//                    if (ElasticSearchUserController.getInstance().deleteUser(userController.getActiveUser())) {
//                        ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
//                        addUserTask.execute(UserController.getInstance().getActiveUser());
//                    }
//                }
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
        boolean edit = true; //For some reason view as also bringing up the edit window
        //This bool fixes that
        switch(item.getItemId()) {
            case R.id.view:
                //TODO Make this MVC. Use a controller that calls this code?
                //On second though this is all UI so it doenst need a controller?
                edit = false;//Makes it so the edit window will not pop up
                Intent intentView = new Intent(MainActivity.this, ViewMoodActivity.class);
                //intentView.putExtra("mood", moodList.get(itemPosition));
                intentView.putExtra("mood", userController.getActiveUser().getMoodList().get(itemPosition));
                startActivityForResult(intentView, 1);

            case R.id.edit: //When edit is pressed
                if (edit) {
                    Intent intentEdit = new Intent(MainActivity.this, EditMoodActivity.class);
//                    intentEdit.putExtra("mood", moodList.get(itemPosition));
                    intentEdit.putExtra("mood", userController.getActiveUser().getMoodList().get(itemPosition));
                    startActivityForResult(intentEdit, 2); //Handled in the results section
                    listItem = itemPosition;
                }
                return true;

            case R.id.delete: //When delete is pressed the item is removed, and everything is updated
                deleteMood(itemPosition);
                return true;
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

//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        ElasticSearchController.GetMoodsTask getMoodsTask = new ElasticSearchController.GetMoodsTask();
//        getMoodsTask.execute("");
//
//        try{
//            moodList = getMoodsTask.get();
//        }
//        catch(Exception e){
//            Log.d("Error", "Failed to get the moods from the async object");
//        }
//
//        adapter = new ArrayAdapter<Mood>(this, R.layout.list_item, moodList);
//        moodListView.setAdapter(adapter);
//
//
//    }
}
