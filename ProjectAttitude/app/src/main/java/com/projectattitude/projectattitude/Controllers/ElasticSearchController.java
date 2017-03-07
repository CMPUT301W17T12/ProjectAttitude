package com.projectattitude.projectattitude.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.projectattitude.projectattitude.Objects.Mood;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

/**
 * Created by Vuk on 3/6/2017.
 */

public class ElasticSearchController {

    private static JestDroidClient client;

    //points to server
    private static String DATABASE_URL = "http://cmput301.softwareprocess.es:8080/";

    //index on server
    private static final String INDEX = "cmput301w17t12";

    //type of object stored on DB
    private static final String TYPE = "mood";

    private static final String REQUEST = "request";



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

    //copied from lonely twitter
    public static class AddMoodsTask extends AsyncTask<Mood, Void, Void>{

        @Override

        protected Void doInBackground(Mood...moods){
            verifySettings();

            for(Mood mood : moods) {
                Log.d("Mood here", mood.getEmotionState());
                Index index = new Index.Builder(mood).index(INDEX).type(TYPE).build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        mood.setId(result.getId());
                    } else {
                        Log.d("Error", "Elasticsearch was not able to add the mood");
                    }

                } catch (IOException e) {
                    Log.d("Error", "The application failed to build and send the tweets");
                }
            }
            return null;
        }
    }
}
