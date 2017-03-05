package com.projectattitude.projectattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.projectattitude.projectattitude.Controllers.MainController;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = new MainController();
    }

    //TODO Build these functions
    /**
     * This method will take the user to the Create Mood view
     */
    private void createMood(){

    }

    /**
     * This method takes a mood the user made and brings them to the edit mood view
     */
    private void editMood(){

    }

    /**
     * This deletes a selected mood.
     * @param mood the mood the user wants to get rid of
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
     * Handles sorting the list, may need several functions for each type of sort.
     * @param item - identifies which item has been clicked
     */
    public void sortMood(MenuItem item){
        switch (item.getItemId()) {
            case R.id.dateOption:
                //TODO: Enter extras in sending intent, parceables for sortMood
                mainController.sortList(getIntent());

            case R.id.reverseDateOption:
                mainController.sortList(getIntent());
        }
    }

    /**
     * Handles filtering the list
     * @param item
     */
    public void filterMood(MenuItem item){
        switch (item.getItemId()) {
            case R.id.timeOption:
                //TODO: Enter extras in sending intent, parceables, for filterMood
                PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.time_menu, popup.getMenu());
                popup.show();

            case R.id.followingOption:
                mainController.filterList(getIntent());

            case R.id.allOption:
                mainController.filterList(getIntent());
        }
    }

    /**
     * Handles filtering the list, but specifically for the time menu
     * @param item
     */
    public void filterMoodsByTime(MenuItem item){
        switch (item.getItemId()) {
            case R.id.dayOption:
                //TODO: Enter extras in sending intent, parceables, for filterMoodsByTime
                mainController.filterList(getIntent());

            case R.id.monthOption:
                mainController.filterList(getIntent());

            case R.id.yearOption:
                mainController.filterList(getIntent());
        }
    }

    /**
     * When the user clicks the map button this takes them to the map view
     */
    private void goToMap(){
    }

    /**
     * When the user clicks the profile button it will take them to the profile view
     * Later may take a profile as an argument to go to someone elses profile.
     */
    private void viewProfile(){
    }

    /**
     * Logs the current profile out of the application and returns the user to the log in view.
     */
    private void logOut(){

    }

    /**
     * OpenSFMenu - Open Sort/Filter Menu
     * Is used when the sort/filter button is pressed to display a menu
     * @param view
     */
    public void openSFMenu(View view){
        //TODO: Test all this popupmenu crap
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_filter_menu, popup.getMenu());
        popup.show();
    }

    public void openSortMenu(MenuItem view){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sort_menu, popup.getMenu());
        popup.show();
    }

    public void openFilterMenu(MenuItem view){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.filterButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_menu, popup.getMenu());
        popup.show();
    }
}
