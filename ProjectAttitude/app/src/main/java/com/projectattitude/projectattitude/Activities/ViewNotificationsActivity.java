package com.projectattitude.projectattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.projectattitude.projectattitude.Controllers.ElasticSearchRequestController;
import com.projectattitude.projectattitude.Objects.FollowRequest;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This activity will handle the viewing and handling of notifications
 * such as follow requests
 */
public class ViewNotificationsActivity extends AppCompatActivity {

    //I created a notification_item.xml to handle notifications
    //On second thought we could use toast pop ups to handle instead of buttons

    ArrayAdapter<FollowRequest> adapter;
    ElasticSearchRequestController requestController = ElasticSearchRequestController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        User user = (User)getIntent().getSerializableExtra("user");

        ListView requestList = (ListView)findViewById(R.id.notification_list);
        //Obtain follow requests that pertain to current user
        ElasticSearchRequestController.GetRequestsTask getRequestsTask = new ElasticSearchRequestController.GetRequestsTask();
        ArrayList<FollowRequest> requests = null;
        try{
            requests = getRequestsTask.execute(user.getUserName()).get(); //Input user's ID as filter
        }
        catch(Exception e){
            Log.d("Error", "Failed to obtain request list");
        }

        if(requests != null){ //If requests are found, display them using adapter
            adapter = new ArrayAdapter<FollowRequest>(this, R.layout.notification_item, requests);
            requestList.setAdapter(adapter);
        }

        //TODO: On-click listeners for accept/deny
    }
}
