package com.projectattitude.projectattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import android.widget.ListView;

import com.projectattitude.projectattitude.Adapters.MoodMainAdapter;
import com.projectattitude.projectattitude.Controllers.ElasticSearchController;
import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ArrayList<Mood> moodList = new ArrayList<Mood>();
    private MoodMainAdapter moodAdapter;
    private ListView moodListView;
    private MainController controller;
    private boolean viewingMyList;

    private  int listItem; //This is the index of the item pressed in the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new MainController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moodListView = (ListView) findViewById(R.id.moodListView);
        FloatingActionButton addMoodButton = (FloatingActionButton) findViewById(R.id.addMoodButton);
        moodAdapter = new MoodMainAdapter(this, moodList);
        moodListView.setAdapter(moodAdapter);
        viewingMyList = false;

        registerForContextMenu(moodListView);

        //on click listener for adding moods
        addMoodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createMood();
            }
        });

        //return all moods from db, so it can populate view on start
        ElasticSearchController.GetMoodsTask getMoodsTask = new ElasticSearchController.GetMoodsTask();
        getMoodsTask.execute("");

        try{
            moodList = getMoodsTask.get();
        }
        catch(Exception e){
            Log.d("Error", "Failed to get the moods from the async object");
        }

        moodAdapter = new MoodMainAdapter(this, moodList);
        moodListView.setAdapter(moodAdapter);
    }



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
    private void editMood(){

    }

    /**
     * This deletes a selected mood.
     * @param mood the mood the user wants to get rid of0
     */
    private void deleteMood(Mood mood){

    }

    /**
     * This is the method that handles finding moods with a given keyword
     * Will probably return a mood list object in time, or set the current one.
     */
    private void searchMood(){

    }

    /**
     * Handles sorting the list, called when an item in the sortMenu is pressed
     * @param item - identifies which item has been pressed on
     */
    public void sortMood(MenuItem item){
        switch (item.getItemId()) {
            case R.id.dateOption:
                controller.sortList(moodList, "Sort"); //True = sorting by date
            case R.id.reverseDateOption:
                controller.sortList(moodList, "Reverse Sort"); //False = sorting by reverse date
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * Handles filtering the list
     * @param item
     */
    public void filterMood(MenuItem item){
        switch (item.getItemId()) {
            case R.id.timeOption:
                PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.time_menu, popup.getMenu());
                popup.show();

            case R.id.followingOption:
                controller.changeList(moodList);

            case R.id.allOption:
                //TODO: Just paste in a "update list" thing here
        }
    }

    /**
     * Handles filtering the list, but specifically for the time menu
     * @param item
     */
    public void filterMoodsByTime(MenuItem item){
        switch (item.getItemId()) {
            case R.id.dayOption:
                controller.filterListByTime(moodList, "Day");

            case R.id.monthOption:
                controller.filterListByTime(moodList, "Month");

            case R.id.yearOption:
                controller.filterListByTime(moodList, "Year");
        }
        moodAdapter.notifyDataSetChanged();
    }

    /**
     * When the user clicks the map button this takes them to the map view
     */
    public void goToMap(MenuItem item){
    }

    /**
     * When the user clicks the profile button it will take them to the profile view
     * Later may take a profile as an argument to go to someone elses profile.
     */
    public void viewProfile(MenuItem item){
    }

    /**
     * Logs the current profile out of the application and returns the user to the log in view.
     */
    public void logOut(MenuItem item){

    }


    /**
     * OpenSFMenu - Open Sort/Filter Menu
     * Is used when the sort/filter button is pressed to display a menu
     * @param view
     */
  //TODO: Resolve SF Menu vs sort and filter menu function below
    public void openSFMenu(View view){
        //TODO: Test all this popupmenu crap
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_filter_menu, popup.getMenu());
        popup.show();
    }

    public void openMainMenu(View view){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.menuButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_menu, popup.getMenu());
        popup.show();
    }

    //accept returned information from activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Mood returnedMood;

        //CreateMoodActivity results, updating mood listview
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                returnedMood = (Mood) data.getSerializableExtra("addMoodIntent");
                moodList.add(returnedMood);
                moodAdapter.notifyDataSetChanged();

                //add newly created mood to DB
                ElasticSearchController.AddMoodsTask addMoodsTask = new ElasticSearchController.AddMoodsTask();
                addMoodsTask.execute(returnedMood);
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
        int itemPosition = info.position;
        boolean edit = true; //For some reason view as also bringing up the edit window
        //This bool fixes that
        switch(item.getItemId()) {
            case R.id.view:
                //TODO Make this MVC. Use a controller that calls this code?
                //On second though this is all UI so it doenst need a controller?
                edit = false;//Makes it so the edit window will not pop up
                Intent intentView = new Intent(MainActivity.this, ViewMoodActivity.class);
                intentView.putExtra("mood", moodList.get(itemPosition));
                startActivity(intentView);


            case R.id.edit: //When edit is pressed
                if (edit) {
                    Intent intentEdit = new Intent(MainActivity.this, EditMoodActivity.class);
                    intentEdit.putExtra("mood", moodList.get(itemPosition));
                    startActivityForResult(intentEdit, 2); //Handled in the results section
                    listItem = itemPosition;
                }
                return true;


            case R.id.delete: //When delete is pressed the item is removed, and everything is updated
                moodList.remove(itemPosition);
                moodAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
