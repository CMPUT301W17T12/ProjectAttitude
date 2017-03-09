package com.projectattitude.projectattitude.Controllers;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.projectattitude.projectattitude.Activities.MainActivity;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.Objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        //TODO: Test this function
        //Taken from http://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
        //Date: 3/6/2017

        class dateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                return (int)(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
            }
        }

        class reverseDateComparator implements Comparator<Mood> {
            @Override
            public int compare(Mood mood1, Mood mood2) {
                return (int)-(((Date)mood1.getMoodDate()).getTime() - ((Date)mood2.getMoodDate()).getTime());
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
        long currentTime = new Date().getTime();
        for(int i = 0; i < moodList.size(); ++i) {
            //If time is greater than timeParameter, remove it from moodList
            long moodTime = ((Date)moodList.get(i).getMoodDate()).getTime();
            if(currentTime - moodTime > timeParameter){
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
        for(int i = 0; i < moodList.size(); ++i){
            if(!(moodList.get(i).getEmotionState().equals(emotion))){ //If mood's emotion is not equal to emotion Parameter
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

    public MoodList getMyMoodList(boolean viewingMyList) {
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

    public void setMyMoodList(MoodList myMoodList) {
        this.myMoodList = myMoodList;
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