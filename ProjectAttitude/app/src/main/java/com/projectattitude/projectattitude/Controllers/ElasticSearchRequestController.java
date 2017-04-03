package com.projectattitude.projectattitude.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projectattitude.projectattitude.Objects.FollowRequest;
import com.projectattitude.projectattitude.Objects.User;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Created by henry on 3/26/2017.
 */

/**
 * A modification to the ElasticSearchController specifically designed for making requests to follow
 * other users.
 */
public class ElasticSearchRequestController {

    private static ElasticSearchRequestController instance = new ElasticSearchRequestController();

    public ElasticSearchRequestController() {
    }

    /**
     * gets instance of the db
     * @return instance
     */
    public static ElasticSearchRequestController getInstance() {
        return instance;
    }

    private static JestDroidClient client;
    //points to server
    private static String DATABASE_URL = "http://cmput301.softwareprocess.es:8080/";
    //type of object stored on DB
    private static final String TYPE = "User";
    //index on server
    private static final String INDEX = "cmput301w17t12";

    //adds request to ES DB
    public static class AddRequestTask extends AsyncTask<FollowRequest, Void, Boolean> {

        @Override
        protected Boolean doInBackground(FollowRequest...search_parameters){
            verifySettings();

            boolean requestExist = false;
            Index index = new Index.Builder(search_parameters[0]).index(INDEX).type(TYPE).build();

            try {
                DocumentResult result = client.execute(index);

                if (result.isSucceeded()){
                    requestExist = true;
                    search_parameters[0].setID(result.getId());
                    Log.d("Request", "added");
                }
            }
            catch (IOException e) {
                Log.d("Error", "The application failed to build and send the User");
            }

            return requestExist;
        }
    }

    /**
     * Check if requests for requester exists orn ot
     */
    public static class CheckRequestTask extends AsyncTask<String, Void, ArrayList<FollowRequest>> {

        @Override
        protected ArrayList<FollowRequest> doInBackground(String... search_parameters) { //enter requester's username
            verifySettings();

            ArrayList<FollowRequest> requests = null;

            String query = "{\n" +
                    "    \"query\" : {\n"+
                    "        \"bool\" : {\n" +
                    "            \"must\" : [\n" +
                    "                { \"term\" : { \"requester\" : \""+search_parameters[0]+"\" } },\n" + //Requester's user name
                    "                { \"term\" : { \"requestee\" : \""+search_parameters[1]+"\" } }\n" + //Requestee's user name
                    "            ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType(TYPE)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    requests = new ArrayList<FollowRequest>();
                    Log.d("Error", "Success getting request");
                    List<FollowRequest> foundRequests = result.getSourceAsObjectList(FollowRequest.class);
                    requests.addAll(foundRequests);
                    Log.d("Error", requests.toString());
                }
                else{
                    Log.d("Error", "Elasticsearch was not able to get requests.");
                }
            }
            catch (IOException e) {
                Log.i("Error", "The application failed to connect to DB");
            }
            return requests;
        }
    }

    /**
     * Search for requests in DB, and return list of follow requests or null depending on how many follow requests for user
     */
    public static class GetRequestsTask extends AsyncTask<String, Void, ArrayList<FollowRequest>> {

        @Override
        protected ArrayList<FollowRequest> doInBackground(String... search_parameters) { //enter requestee's username
            verifySettings();

            ArrayList<FollowRequest> requests = null;

            String query = "{\n" +
                    "    \"query\" : {\n"+
                    "        \"term\" : { \"requestee\" : \""+search_parameters[0]+"\" }\n" + //Requestee's user name
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType(TYPE)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    requests = new ArrayList<FollowRequest>();
                    Log.d("Error", "Success getting request");
                    List<FollowRequest> foundRequests = result.getSourceAsObjectList(FollowRequest.class);
                    requests.addAll(foundRequests);
                    Log.d("Error", requests.toString());
                }
                else{
                    Log.d("Error", "Elasticsearch was not able to get requests.");
                }
            }
            catch (IOException e) {
                Log.i("Error", "The application failed to connect to DB");
            }
            return requests;
        }
    }

    /**
     * Deletes request if needed.
     */
    public static class DeleteRequestTask extends AsyncTask<FollowRequest, Void, Boolean>{

        @Override
        protected Boolean doInBackground(FollowRequest... search_parameters){
            verifySettings();
            String query = "{\n" +
                    "    \"query\" : {\n"+
                    "        \"bool\" : {\n" +
                    "            \"must\" : [\n" +
                    "                { \"term\" : { \"requester\" : \""+search_parameters[0].getRequester()+"\" } },\n" + //Requester's user name
                    "                { \"term\" : { \"requestee\" : \""+search_parameters[0].getRequestee()+"\" } }\n" + //Requestee's user name
                    "            ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(query).addIndex(INDEX).addType(TYPE).build();

            try{
                client.execute(deleteByQuery);
                Log.d("Error", "Request deleted.");
            }
            catch(IOException e) {
                Log.d("Error", "Elasticsearch failed.");
            }
            return true;
        }
    }

    /**
     * Updates a user in the database.
     */
    public static class UpdateRequestsTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            String query = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            String json = gson.toJson(search_parameters[0].getRequests());

            query = "{\"doc\" : { \"requests\" : " + json + "}}";
            Update update = new Update.Builder(query).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
            Log.d("error", "UserName of update: " + search_parameters[0].getUserName());
            Log.d("error", "list of update: " +  search_parameters[0].getRequests().toString());

            Log.d("error", "Check update value string: " + update.toString());

            try {
                // where is the client
                JestResult result = client.execute(update);
                //Log.d("InAsyncTask ID", result.getId());
                if (result.isSucceeded()) {
                    //If the request succeeds, we see a response similar to that of the index request: Similar to the jest
                    //partial update to doc guide, this part looks good, response to post request
                    //if you check db, you can see the update happened.
                    Log.d("error", "Check update Json String " + result.getJsonString());
                    //Log.d("In AsyncTask ID", result.getId());
                } else {
                    Log.i("Error", "Elasticsearch was not able to update the user.");
                }
            } catch (Exception e) {
                Log.i("Error", "The application failed to build and send the user");
            }
            //}
            return null;
        }
    }

    //copied from lonelytwitter on March 31st, at 16:38
    private static void verifySettings(){
        if(client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(DATABASE_URL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
