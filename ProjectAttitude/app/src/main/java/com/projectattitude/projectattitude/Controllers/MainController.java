package com.projectattitude.projectattitude.Controllers;

import android.content.Intent;

import com.projectattitude.projectattitude.Objects.MoodList;
import com.projectattitude.projectattitude.Objects.User;

import java.util.ArrayList;

/**
 * Created by Chris on 2/24/2017.
 */

public class MainController {
    private User user;
    private ArrayList<User> followList; //The people the user follows?
    private ArrayList<User> followedList; //The people that follow the user?
    private MoodList myMoodList;
    private MoodList followedMoodList;
    private int sortBy;//No idea of this one

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
     * I Guess this is where the bulk of the filtering gets done?
     * @param intent
     */
    private void filterList(Intent intent){

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