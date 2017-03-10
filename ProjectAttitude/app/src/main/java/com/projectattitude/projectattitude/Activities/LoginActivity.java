package com.projectattitude.projectattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projectattitude.projectattitude.Controllers.ElasticSearchUserController;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;

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

        final Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameView.setError(null);
                passwordView.setError(null);

                User user = new User();

                String username = usernameView.getText().toString();
                user.setUserName(username);
                String password = passwordView.getText().toString();

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
//                    showProgress(true); // show the progress animation
//                    ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
//                    addUserTask.execute(user);

                    //need to get a static instance
                    if(ElasticSearchUserController.getInstance().verifyUser(user)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    }

                    else{
                        usernameView.setError("this name already exists");
                    }


//                    LoginTask loginTask = new LoginTask(username, password);
//                    loginTask.execute((Void) null);
                }
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        titleView = findViewById(R.id.title_label);
    }

//    private boolean authenticate (String username) {
//
//        ElasticSearchUserController.UserExistTask userExistTask = new ElasticSearchUserController.UserExistTask();
//        userExistTask.execute(username);
//
//        try{
//            if(userExistTask.get()){
//                Log.d("User", "exist");
//                return true;
//            }
//
//            else{
//                Log.d("User", "Does not exist");
//                return false;
//            }
//        }
//        catch (Exception e){
//            Log.d("User", "An error happened");
//            return false;
//        }
//    }

    /**
     * Login background task, allows us to run the long part of login asynchronously
     */
//    public class LoginTask extends AsyncTask<Void, Void, Boolean> {
//        private final String username;
//        private final String password;
//
//        LoginTask(String username, String password){
//            this.username = username;
//            this.password = password;
//        }

//        @Override
//        protected Boolean doInBackground(Void... params){
//            return authenticate(username, password);
//        }

//        @Override
//        protected void onPostExecute(final Boolean success) {
//            showProgress(false);
//
//            if (success) {
//                finish();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                LoginActivity.this.startActivity(intent);
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            showProgress(false);
//        }
//    }

//    /**
//     * Login authentication, if given a username that doesn't exist in the database
//     * will create a new account for that given username password
//     * returns true if login/signup succeeded
//     * @param username
//     * @param password
//     * @return
//     */
//    private boolean authenticate (String username, String password) {
//
//        if (checkUsernameExists(username)) {
//
//            // Find if username exists in database
//
//            // Check if password for username matches
//            if (BCrypt.checkpw(password, getHashedPasswordFromServer(username))){
//                // Now logged into account, switch to mainactivity
//
//                return true;
//            }
//            // Password was incorrect
//            else {
//                passwordView.setError(getString(R.string.error_incorrect_password));
//                return false;
//            }
//        }
//        // Create New account in database with username password
//        else {
//            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//        }
//        return true;
//    }

//    /**
//     * Retrieve the salted hash of username's password
//     * @param username
//     * @return hashed
//     */
//    private String getHashedPasswordFromServer(String username) {
//        return BCrypt.hashpw("test", BCrypt.gensalt());
//    }
//
//    /**
//     * Checks if there exists an account in the database with name username
//     * @param username
//     * @return
//     */
//    private boolean checkUsernameExists (String username) {
//        // Check username with elasticsearch server
//
//        return false;
//    }

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
}
