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
 * Created by Vuk on 3/9/2017.
 */

public class ElasticSearchUserController {

    private static ElasticSearchUserController instance = new ElasticSearchUserController();

    protected ElasticSearchUserController() {
    }

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

    public boolean deleteUser(User user){
        DeleteUserTask deleteUserTask = new DeleteUserTask();
        deleteUserTask.execute(user);
        return true;
    }

//    public void saveInFile(){
//
//    }

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
