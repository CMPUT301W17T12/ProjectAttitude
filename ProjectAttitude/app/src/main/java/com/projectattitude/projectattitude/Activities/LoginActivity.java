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

package com.projectattitude.projectattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.ExecutionException;

/**
 * LoginActivity allows users to log into the service and connect to the Database.
 * Users who do not currently have an account can create an account by entering a username a username
 * and if its unique, it creates the account for them, Otherwise, entering a username with a valid existing username
 * will log into the account, and load the appropriate information on the database in regards
 * to that user.
 */
public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "HzITcd99DosfymtjdZzjGviFU";
    private static final String TWITTER_SECRET = "k4P5AadHdAbhqSVjIbF1kzc2TppZ7kQOwJm23JPXhBRTrIVgt7";


    private EditText usernameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);

        usernameView = (EditText) findViewById(R.id.usernameField);

        final Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameView.setError(null);

                User user = new User();

                String username = usernameView.getText().toString();
                user.setUserName(username);

                Boolean cancel = false;
                //no username entered
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
                        //user does not exist
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
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
