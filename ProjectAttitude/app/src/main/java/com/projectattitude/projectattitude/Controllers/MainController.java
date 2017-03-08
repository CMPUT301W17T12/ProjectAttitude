package com.projectattitude.projectattitude.Controllers;

import android.content.Intent;
import android.net.ConnectivityManager;

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
    //private MoodList myMoodList; - not really needed
    //private MoodList followedMoodList; - not really needed
    private boolean displayingMyMoodList = true; //Which list is being displayed currently. 1 = myMoodList, 0 = followedMoodList

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
     * I Guess this is where the bulk of the filtering gets done?
     * @param intent
     */
    public void filterListByTime(){
        //need to filter
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