package com.projectattitude.projectattitude.Controllers;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.Objects.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.lang.Math.toIntExact;

/**
 * Created by Chris on 2/24/2017.
 */

public class MainController {
    private User user;
    private ArrayList<User> followList; //The people the user follows?
    private ArrayList<User> followedList; //The people that follow the user?
    private MoodList myMoodList;
    private MoodList followedMoodList;

    /**
     * Gets the username of a given user
     * @param user the user you want the name of
     * @return a string of the users name
     * This function actually kinda works if we override toString!
     */
    public String getUsername(User user){
        return user.toString();
    }

    /**
     * Sorts an array list holding moods depending on its date.
     * @param moodList: Arraylist containing moods.
     * @param sortOrder: "Sort" if sorting in normal order, "Reverse Sort" if sorting in reverse order.
     *
     * Postcondition: List is sorted, but arrayAdapter needs to be notified.
     */
    public void sortList(ArrayList<Mood> moodList, String sortOrder){
        //Taken from http://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
        //Date: 3/6/2017

        //Taken from http://stackoverflow.com/questions/1590831/safely-casting-long-to-int-in-java
        //Date: 3/9/2017
        class dateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                long tempValue = -(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
                if (tempValue < 0){ return -1; }
                if (tempValue > 0){ return 1; }
                return 0; //else, return 0
            }
        }

        class reverseDateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                long tempValue = ((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime();
                if (tempValue < 0){ return -1; }
                if (tempValue > 0){ return 1; }
                return 0; //else, return 0
            }
        }

        if(sortOrder.equals("Sort")){//Sort
            Collections.sort(moodList, new dateComparator());
        }else if (sortOrder.equals("Reverse Sort")){//Reverse Sort
            Collections.sort(moodList, new reverseDateComparator());
        }
        //else, don't sort or anything.

    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' date
     * Removes moods from moodList that don't have times less than what's specified in the timeParameter
     * @param moodList - moods to be filtered
     * @param timeParameter - time in milliseconds to filter by
     */
    public void filterListByTime(ArrayList<Mood> moodList, long timeParameter){
        ArrayList<Mood> newList = new ArrayList<Mood>();
        long currentTime = new Date().getTime();
        Log.d("Error", "Time Parameter: "+Long.toString(timeParameter));
        Log.d("Error", "MoodList Size: "+Integer.toString(moodList.size()));

        for(int i = moodList.size() - 1; i >= 0; --i) { //Go backwards on list, to work around moodList.remove()
            Log.d("Error", moodList.get(i).getEmotionState());
            //If time is greater than timeParameter, remove it from moodList
            long moodTime = ((Date)moodList.get(i).getMoodDate()).getTime();
            Log.d("Error", "This Mood's Time: "+Long.toString(currentTime - moodTime));
            if(Math.abs(currentTime - moodTime) > timeParameter){
                moodList.remove(i);
            }
        }

    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' emotional state
     * Removes moods from moodList that don't have the correct emotional state
     * @param moodList - moods to be filtered
     * @param emotion - String of mood's emotional state
     */
    public void filterListByEmotion(ArrayList<Mood> moodList, String emotion){
        for(int i = moodList.size() - 1; i >= 0; --i){
            if(!(moodList.get(i).getEmotionState().equals(emotion))){ //If mood's emotion is not equal to emotion Parameter
                moodList.remove(i);
            }
        }
    }

    /**
     * Filters an array list of moods, resulting in the moodList but by moods' trigger
     * Removes moods from moodList that don't have the correct emotional state
     * @param moodList - moods to be filtered
     * @param reason - Word that filters mood by finding if word is in its' reason field.
     */
    public void filterListByTrigger(ArrayList<Mood> moodList, String reason){
        reason = reason.toLowerCase(); //Not case-sensitive searching
        for(int i = moodList.size() - 1; i >= 0; --i){
            //Split mood's trigger sentence into individual words, then try to find the word specified by reason
            String currentTrigger = moodList.get(i).getTrigger().toLowerCase();
            String triggerWords[] = currentTrigger.split(" ");
            boolean foundWord = false; //Flag for if word is found or not
            for(int k = 0; k < triggerWords.length; ++k){
                if(reason.equals(triggerWords[k])){
                    foundWord = true;
                    break;
                }
            }
            if (!foundWord){
                moodList.remove(i);
            }
        }
    }

    /**
     * Changes to following list...????
     * @param moodList - moods to be filtered
     */
    public void changeList(ArrayList<Mood> moodList){
        //TODO: Implementation of following
    }

    /**
     * This does sorting maybe?
     * @param array whats to be sorted?
     * @return a sorted array
     * Could just edit the list instead of returning a new one
     */
    private ArrayList returnSortedList(ArrayList array){
        return array;
    }

    public MoodList getMyMoodList() {
        ElasticSearchController.GetMoodsTask getMoodsTask = new ElasticSearchController.GetMoodsTask();

        getMoodsTask.execute("");

        try {
            //Update myMoodList with new moods
            ArrayList<Mood> tempList = getMoodsTask.get();
            myMoodList.setMoodList(new MoodList(tempList));
        } catch (Exception e) {
            Log.d("Error", "Failed to get the moods from the async object");
        }

        return myMoodList.clone();
    }

    //Precondition: myMoodList shouldn't be touchable by anyone else
    public void setMyMoodList(MoodList tempList) {
        this.myMoodList = tempList.clone();
    }

    public MoodList getFollowedMoodList() {
        //TODO: Get Followed List
        return followedMoodList;
    }

    public void setFollowedMoodList(MoodList followedMoodList) {
        this.followedMoodList = followedMoodList;
    }
}




//    //function to add moods to elastic search
//    public static class AddMoodsTask extends AsyncTask<Mood, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Mood... moods){
//            verifySettings();
//
//            for (Mood mood : moods){
//                Index index = new Index.Builder(mood).index("cmput301w17t12").type("tweet").build();
//
//                try{
//                    DocumentResult result = client.execute(index);
//                    if (result.isSucceeded()){
//                        moods.setId(result.getId());
//                    }
//
//                    else{
//                        Log.i("Error", "The application failed to build and send the moods");
//                    }
//                }
//                catch(Exception e){
//                    Log.i("Error", "The application failed to build and send the moods");
//                }
//            }
//            return null;
//        }
//    }
//
//    public static class GetMoodsTask extends AsyncTask<String, Void, ArrayList<Mood>>{
//        @Override
//        protected ArrayList<Mood> doInBackground(String... search_parameters) {
//            verifySettings();
//
//            ArrayList<Mood> moods = new ArrayList<Mood>();
//
//            String query;
//            if(search_parameters[0].equals("")){
//                query = search_parameters[0];
//            }
//
//            else{
//                query = "{\n" +
//                        "   \"query\" : {\n" +
//                        "       \"term\" : { \"Message\" : \"" + search_parameters[0] + "\" }\n" +
//                        "   }\n" +
//                        "}";
//            }
//
//            Search search = new Search.Builder(query);
//                    .addIndex("cmput301w17t12")
//                    .addType("Mood")
//                    .build();
//
//            try{
//                SearchResult result = client.execute(search);
//                if(result.isSucceeded()){
//                    List<Mood> foundMoods = result.getSourceAsObjectList(Mood.class);
//                    moods.addAll(foundMoods);
//                }
//                else{
//                    Log.i("Error", "The search query failed to find any moods that matched");
//                }
//            }
//            catch(Exception e){
//                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
//            }
//            return moods;
//        }
//    }
//
//    public static void verifySettings() {
//        if (client == null) {
//            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
//            DroidClientConfig config = builder.build();
//
//            JestClientFactory factory = new JestClientFactory();
//            factory.setDroidClientConfig(config);
//            client = (JestDroidClient) factory.getObject();
//        }
//    }