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

package com.projectattitude.projectattitude.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projectattitude.projectattitude.Objects.User;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * ElasticSearchUserController contains tasks that communicate and update the database with users
 */

public class ElasticSearchUserController {

    private static ElasticSearchUserController instance = new ElasticSearchUserController();

    protected ElasticSearchUserController() {
    }

    /**
     * gets instance of the db
     * @return instance
     */
    public static ElasticSearchUserController getInstance() {
        return instance;
    }

    private static JestDroidClient client;
    //points to server
    private static String DATABASE_URL = "http://cmput301.softwareprocess.es:8080/";
    //type of object stored on DB
    private static final String TYPE = "User";
    //index on server
    private static final String INDEX = "cmput301w17t12";

    /**
     * function used to check if user exists and get them from db, or if they dont exist, add them
     * to db
     * @param user
     * @return boolean
     */
    public boolean verifyUser(User user) {
        GetUserTask getUserTask = new GetUserTask();
        User temp = new User();

        try {
            temp = getUserTask.execute(user.getUserName()).get();
            }
         catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        //if user did not exist, add one
        if(temp == null){
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            return true;
        }
        return  false;
    }

    /**
     * delete user from db
     * @param user
     * @return boolean
     */
    public boolean deleteUser(User user){
        DeleteUserTask deleteUserTask = new DeleteUserTask();
        deleteUserTask.execute(user);
        return true;
    }


    /**
     * Add user to ES DB
     */
    public static class AddUserTask extends AsyncTask<User, Void, Boolean> {

        @Override
        protected Boolean doInBackground(User...search_parameters){
            verifySettings();

            boolean userExist = false;
            Index index = new Index.Builder(search_parameters[0]).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();

            try {
                DocumentResult result = client.execute(index);

                if (result.isSucceeded()){
                    userExist = true;
                    Log.d("user synced", result.getId());
                }
            }
            catch (IOException e) {
                Log.d("Error", "The application failed to build and send the User");
            }

            return userExist;
        }
    }

    /**
     * Gets all users in the datebase.
     */
    public static class GetAllUsersTask extends AsyncTask<String, Void, ArrayList<User>>{
        @Override
        protected ArrayList<User> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<User> users = new ArrayList<User>();

            //Search search = new Search.Builder(search_parameters[0]).addIndex(INDEX).addType(TYPE).build();
            String query = "{\n" +
                    "   \"size\":200,\n" +
                    "    \"query\" : {\n"+
                    "        \"match_all\" : {}\n" +
                    "    }\n" +
                    "}";
//
            Search search = new Search.Builder(query)
                    .addIndex(INDEX)
                    .addType(TYPE)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    List userList = result.getSourceAsObjectList(User.class);
                    users.addAll(userList);

                }
                else{
                    Log.d("Error", "Elasticsearch was not able to get requests.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to connect to DB");
            }
            return users;
        }
    }

    /**
     * Search for username in DB, and return user, either as null or as the object
     */
    public static class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            Log.d("Error", "name: " + search_parameters[0]);
            Get get = new Get.Builder(INDEX, search_parameters[0]).type(TYPE).build();
            //Search get = new Search.Builder("{\"query\" : {\"term\" : { \"userName\" : \"" + search_parameters[0] + "\" }}}").addIndex(INDEX).addType(TYPE).build();

            //Log.d("test1", search_parameters[0]);

            User user = null;
            try {
                JestResult result = client.execute(get);
                //SearchResult result = client.execute(get);
                //user = result.getSourceAsObject(User.class);
//                if(result.getHits(User.class).size()!=0){
//                    Log.d("Error", (result.getHits(User.class).size()) + "");
//                    SearchResult.Hit<User, Void> thing = result.getFirstHit(User.class);
//                    user = thing.source;
//                }
                if(result.isSucceeded()){
                    Log.d("Error", "success getting user");
                    //user = result.getSourceAsObject(User.class);
                    Log.d("Error", "Name that was gotten: " + result.getJsonObject());
//                    Log.d("Error", "Name that was gotten: " + result.getSourceAsObject(User.class).getUserName());
//                    Log.d("Error", "List that was gotten: " + result.getSourceAsObject(User.class).getMoodList());
                   String userJson = result.getSourceAsString();

                    //Json string is exactley whats expected following the retrieve document guide on ES
                    Log.d("Error", "JsonString: " + userJson);

                    //Gson gson = new Gson();
                    //Taken from http://stackoverflow.com/questions/7910734/gsonbuilder-setdateformat-for-2011-10-26t202959-0700
                    //Date: 3/21/2017
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

//                    GsonBuilder gsonBuilder = new GsonBuilder();
//                    Gson gson = gsonBuilder.create();

                    user = gson.fromJson(userJson, User.class);
                    Log.d("Error", "Username: " + user.getUserName() + " MoodList: " + user.getMoodList());
                   //user = result.getSourceAsObject(User.class);
                }

                else{
                    Log.d("Error", "Elasticsearch was not able to get the user.");
                }

                //Log.d("test1", user.getUserName());
            }
            catch (IOException e) {
                Log.i("Error", "The application failed to connect to DB");
            }
            return user;
        }
    }

    /**
     * Deletes a user from the database.
     */
    public static class DeleteUserTask extends AsyncTask<User, Void, Boolean>{

        @Override
        protected Boolean doInBackground(User... search_parameters){
            verifySettings();

            Delete delete = new Delete.Builder(search_parameters[0].getUserName()).index(INDEX).type(TYPE).build();

            try{
                client.execute(delete);
            }
            catch(IOException e) {

            }
            return true;
        }
    }

    /**
     * Updates a user in the database.
     */
    public static class UpdateUserTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            //query = "{\"doc\" : { \"type\" : \"nested\", \"followList\" : " + user.getGsonFollowList() + "}}";

            //for (User user : users) {
            String query = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            String json = gson.toJson(search_parameters[0].getMoodList());
            //json += "meme";

//            json = json.replace("\\\"", "\"");
//            json = json.replace("}\"", "}");
//            json = json.replace("\"{","{");

//            query = "{\"doc\" : { \"type\" : \"nested\", \"moods\" : " + json + "}}";
            query = "{\"doc\" : { \"moods\" : " + json + "}}";
            Update update = new Update.Builder(query).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
            Log.d("error", "UserName of update: " + search_parameters[0].getUserName());
            Log.d("error", "list of update: " +  search_parameters[0].getMoodList().toString());

            //this is also correct when following ES partial update to documents guide, post request
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

    /**
     * Updates a user request in the database.
     */
    public static class UpdateUserRequestTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            String query = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            String json = gson.toJson(search_parameters[0].getFollowList());

            query = "{\"doc\" : { \"followList\" : " + json + "}}";
            Update update = new Update.Builder(query).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
            Log.d("error", "UserName of update: " + search_parameters[0].getUserName());
            Log.d("error", "list of update: " +  search_parameters[0].getFollowList().toString());

            try {
                // where is the client
                JestResult result = client.execute(update);
                if (result.isSucceeded()) {
                    //If the request succeeds, we see a response similar to that of the index request: Similar to the jest
                    //partial update to doc guide, this part looks good, response to post request
                    //if you check db, you can see the update happened.
                    Log.d("error", "Check update Json String " + result.getJsonString());
                } else {
                    Log.i("Error", "Elasticsearch was not able to update the user.");
                }
            } catch (Exception e) {
                Log.i("Error", "The application failed to build and send the user");
            }

            return null;
        }
    }

    /**
     * Updates a user request in the database.
     */
    public static class UpdateUserRequestFollowedTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            String query = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            String json = gson.toJson(search_parameters[0].getFollowedList());

            query = "{\"doc\" : { \"followedList\" : " + json + "}}";
            Update update = new Update.Builder(query).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
            Log.d("error", "UserName of update: " + search_parameters[0].getUserName());
            Log.d("error", "list of update: " +  search_parameters[0].getFollowedList().toString());

            try {
                // where is the client
                JestResult result = client.execute(update);
                if (result.isSucceeded()) {
                    //If the request succeeds, we see a response similar to that of the index request: Similar to the jest
                    //partial update to doc guide, this part looks good, response to post request
                    //if you check db, you can see the update happened.
                    Log.d("error", "Check update Json String " + result.getJsonString());
                } else {
                    Log.i("Error", "Elasticsearch was not able to update the user.");
                }
            } catch (Exception e) {
                Log.i("Error", "The application failed to build and send the user");
            }

            return null;
        }
    }

    /**
     * Updates a user picture in the database.
     */
    public static class UpdateUserPictureTask extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... search_parameters) {
            verifySettings();

            String query = "";
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            String json = gson.toJson(search_parameters[0].getPhoto());

            query = "{\"doc\" : { \"photo\" : " + json + "}}";
            Update update = new Update.Builder(query).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
            Log.d("error", "UserName of update: " + search_parameters[0].getUserName());

            try {
                JestResult result = client.execute(update);
                if (result.isSucceeded()) {
                    //If the request succeeds, we see a response similar to that of the index request: Similar to the jest
                    //partial update to doc guide, this part looks good, response to post request
                    //if you check db, you can see the update happened.
                    Log.d("error", "Check update Json String " + result.getJsonString());
                } else {
                    Log.i("Error", "Elasticsearch was not able to update the user's photo.");
                }
            } catch (Exception e) {
                Log.i("Error", "The application failed to build and send the user");
            }

            return null;
        }
    }

    /**
     * Verifies settings.
     */
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
