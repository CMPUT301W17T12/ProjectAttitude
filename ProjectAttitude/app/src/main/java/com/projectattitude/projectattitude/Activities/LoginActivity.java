package com.projectattitude.projectattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import java.util.concurrent.ExecutionException;

/**
 * LoginActivity allows users to log into the service and connect to the Database.
 * Users who do not currently have an account can create an account by entering a username
 * and password. Otherwise, entering a username with a valid corresponding password
 * will log into the account, and load the appropriate information on the database in regards
 * to that user.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameView = (EditText) findViewById(R.id.usernameField);

        final Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // button for signing in
                usernameView.setError(null);

                User user = new User();

                String username = usernameView.getText().toString();
                user.setUserName(username);

                Boolean cancel = false;

                // If username exists continue with log in, otherwise create new account
                if (username.equals("")){
                    usernameView.setError(getString(R.string.error_field_required));
                    cancel = true;
                }

                if (cancel) {
                    usernameView.requestFocus();
                }
                else {

                    if(isNetworkAvailable()){
                        //need to get a static instance, check for existence of user
                        //user doesnt exist
                        if (ElasticSearchUserController.getInstance().verifyUser(user)) {

                            //creates user using ElasticSearchUserController and switch to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("PassUserToMain", user);
                            startActivity(intent);
                            finish();
                        } else {

                            //grab user from db and pass to MainActivity, since they exist
                            User user1 = new User();
                            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();

                            try {
                                user1 = getUserTask.execute(user.getUserName()).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("PassUserToMain", user1);
                            startActivity(intent);
                            finish();
                        }
                    }

                    else{
                        Toast.makeText(LoginActivity.this, "Must be connected to internet to login!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    /**
     * This returns a boolean value if the device is connected to the internet
     * Taken from http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
