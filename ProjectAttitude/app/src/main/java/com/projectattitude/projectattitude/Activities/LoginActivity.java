package com.projectattitude.projectattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projectattitude.projectattitude.Controllers.ElasticSearchController;
import com.projectattitude.projectattitude.R;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;
    private EditText passwordView;

    private View titleView;
    private View loginFormView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameView = (EditText) findViewById(R.id.usernameField);
        passwordView = (EditText) findViewById(R.id.passwordField);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the authentication process
                showProgress(true); // show the progress animation

                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();

                authenticate(username, password);
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        titleView = findViewById(R.id.title_label);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            titleView.setVisibility(show ? View.GONE : View.VISIBLE);
            titleView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    titleView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Login authentication, if given a username that doesn't exist in the database
     * will create a new account for that given username password
     * @param username
     * @param password
     * @return
     */
    private void authenticate (String username, String password) {
        // If username exists continue with log in, otherwise create new account
        if (checkUsernameExists(username)) {

            // Find if username exists in database

            // Check if password for username matches
            if (BCrypt.checkpw(password, getHashedPasswordFromServer(username))){
                // Now logged into account, switch to mainactivity

                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            // Password was incorrect
            else {
                passwordView.setError("Incorrect Password");
            }
        }
        // Create New account in database with username password
        else {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }

    /**
     * Retrieve the salted hash of username's password
     * @param username
     * @return hashed
     */
    private String getHashedPasswordFromServer(String username) {
        return BCrypt.hashpw("test", BCrypt.gensalt());
    }

    /**
     * Checks if there exists an account in the database with name username
     * @param username
     * @return
     */
    private boolean checkUsernameExists (String username) {
        // Check username with elasticsearch server

        return false;
    }
}
