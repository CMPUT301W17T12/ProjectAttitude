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

import com.projectattitude.projectattitude.Objects.User;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

/**
 * ElasticSearchUserController contains tasks that communicate and update the dtabase with users
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


    //add user to ES DB
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

    //search for username in DB, and return user, either as null or as the object
    public static class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();
            Get get = new Get.Builder(INDEX, search_parameters[0]).type(TYPE).id(search_parameters[0]).build();

            User user = null;
            try {
                JestResult result = client.execute(get);
                user = result.getSourceAsObject(User.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }
    }

    public static class DeleteUserTask extends AsyncTask<User, Void, Boolean>{

        @Override
        protected Boolean doInBackground(User... search_parameters){
            verifySettings();

            Delete delete = new Delete.Builder(search_parameters[0].getUserName()).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();

            try{
                client.execute(delete);
            }
            catch(IOException e) {

            }
            return true;
        }
    }

//    /**
//     * doesnt work for some reason
//     */
//    public static class UpdateUserTask extends  AsyncTask<User, Void, Void> {
//        @Override
//        protected Void doInBackground(User... search_parameters) {
//            verifySettings();
//
//            //for (User user : users) {
//                Update update = new Update.Builder(search_parameters[0]).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();
//            Log.d("Username:", search_parameters[0].getUserName());
//            Log.d("Username moodList", search_parameters[0].getMoodList().toString());
//
//                try {
//                    // where is the client
//                    JestResult result = client.execute(update);
//                    //Log.d("InAsyncTask ID", result.getId());
//                    if (result.isSucceeded()) {
//                        //Log.d("In AsyncTask ID", result.getId());
//                    } else {
//                        Log.i("Error", "Elasticsearch was not able to update the user.");
//                    }
//                } catch (Exception e) {
//                    Log.i("Error", "The application failed to build and send the user");
//                }
//            //}
//            return null;
//        }
//    }

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
