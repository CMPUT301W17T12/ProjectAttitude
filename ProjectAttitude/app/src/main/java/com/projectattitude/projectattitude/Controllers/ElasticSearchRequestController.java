package com.projectattitude.projectattitude.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projectattitude.projectattitude.Objects.FollowRequest;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

/**
 * Created by henry on 3/26/2017.
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
    private static final String TYPE = "FollowRequest";
    //index on server
    private static final String INDEX = "cmput301w17t12";

    //add user to ES DB
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

    //search for username in DB, and return user, either as null or as the object
    public static class GetRequestTask extends AsyncTask<FollowRequest, Void, FollowRequest> {

        @Override
        protected FollowRequest doInBackground(FollowRequest... search_parameters) {
            verifySettings();

            Get get = new Get.Builder(INDEX, search_parameters[0].getID()).type(TYPE).build();
            FollowRequest request = null;
            try {
                JestResult result = client.execute(get);
                if(result.isSucceeded()){
                    Log.d("Error", "Success getting request");
                    String requestJson = result.getSourceAsString();

                    //Json string is exactly whats expected following the retrieve document guide on ES
                    Log.d("Error", "JsonString: " + requestJson);

                    //Gson gson = new Gson();
                    //Taken from http://stackoverflow.com/questions/7910734/gsonbuilder-setdateformat-for-2011-10-26t202959-0700
                    //Date: 3/21/2017
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    request = gson.fromJson(requestJson, FollowRequest.class);
                    Log.d("Error", "Requestee: " + request.getRequestee() + " Requester: " + request.getRequester());
                }

                else{
                    Log.d("Error", "Elasticsearch was not able to get the request.");
                }

            }
            catch (IOException e) {
                Log.i("Error", "The application failed to connect to DB");
            }
            return request;
        }
    }

    public static class DeleteRequestTask extends AsyncTask<FollowRequest, Void, Boolean>{

        @Override
        protected Boolean doInBackground(FollowRequest... search_parameters){
            verifySettings();

            Delete delete = new Delete.Builder(search_parameters[0].getID()).index(INDEX).type(TYPE).id(search_parameters[0].getID()).build();

            try{
                client.execute(delete);
                Log.d("Error", "Request deleted.");
            }
            catch(IOException e) {
                Log.d("Error", "Elasticsearch failed.");
            }
            return true;
        }
    }

    //copied from lonelytwitter
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
