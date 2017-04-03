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

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.projectattitude.projectattitude.Objects.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Vuk on 3/10/2017.
 */

/**
 * The UserController handles which user is logged in and is able to save and load to and from
 * the gson files.
 */

public class UserController {

    private static UserController instance = new UserController();
    private ElasticSearchUserController ES = ElasticSearchUserController.getInstance();

    private User activeUser;
    private static final String FILENAME = "user_cache.sav";


    public static UserController getInstance() {
        return instance;
    }

    private UserController() {
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        //saveInFile();
    }

    /**
     * Saves the file in a gson file.
     */
    public void saveInFile(Context context){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            Gson gson = new Gson();
            gson.toJson(activeUser, bufferedWriter);
            Log.d("SAVED", activeUser.getUserName());
            bufferedWriter.flush();

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Loads from the gson file.
     */
    public void loadFromFile(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            Gson gson = new Gson();

            activeUser = gson.fromJson(bufferedReader, User.class);

        }catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Returns whether there's a user currently logged in
     * @return
     */
    public boolean isLoggedIn(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            Gson gson = new Gson();

            User user = gson.fromJson(bufferedReader, User.class);

            if(user == null){
                Log.d("LOGINTEST", "no user");
                return false;
            }
            else if (user.getUserName().equals("___NULL___USER___")) {
                Log.d("LOGINTEST", "logged out");
                return false;
            }
            else {
                return true;
            }

        }catch (FileNotFoundException e) {
            Log.d("LOGINTEST", "failed - filenotfound");
            activeUser = new User();
            saveInFile(context);
            return false;
        }
    }

    /**
     * Clears the gson file
     */
    public void clearCache(Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            Gson gson = new Gson();
            User user = new User();
            user.setUserName("___NULL___USER___");
            gson.toJson(user, bufferedWriter);
            bufferedWriter.flush();

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
