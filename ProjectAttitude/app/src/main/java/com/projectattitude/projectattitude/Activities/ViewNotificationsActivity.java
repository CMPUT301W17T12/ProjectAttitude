package com.projectattitude.projectattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.projectattitude.projectattitude.Adapters.RequestAdapter;
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


    private ArrayList<FollowRequest> requests = new ArrayList<FollowRequest>();
    private ListView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        //requests = new ArrayList<FollowRequest>();
        requestList = (ListView)findViewById(R.id.notification_list);
        requests = new ArrayList<FollowRequest>();
        RequestAdapter adapter = new RequestAdapter(this, requests);
        requestList.setAdapter(adapter);
//        requests.add(new FollowRequest("vusdfk", "henrsdfy"));
//        requests.add(new FollowRequest("vasuk", "henrsasay"));

        User user = (User)getIntent().getSerializableExtra("user");

        //Obtain follow requests that pertain to current user
        try{
            ElasticSearchRequestController.GetRequestsTask getRequestsTask = new ElasticSearchRequestController.GetRequestsTask();
            getRequestsTask.execute(user.getUserName());//Input user's ID as filter
            requests.addAll(getRequestsTask.get());
        }
        catch(Exception e){
            Log.d("Error", "Failed to obtain request list");
        }

        if(requests.size() == 0){ //If no requests, show toast message
            Toast.makeText(ViewNotificationsActivity.this, "No pending requests.",
                    Toast.LENGTH_LONG).show();
        }

        setResult(RESULT_OK);
        adapter.notifyDataSetChanged();

    }
}
