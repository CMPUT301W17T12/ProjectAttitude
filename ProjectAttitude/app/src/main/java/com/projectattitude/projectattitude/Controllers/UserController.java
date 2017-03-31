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

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.projectattitude.projectattitude.Objects.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    private static String ACTIVE_USER_SAV = "active_user.json";


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

    public void saveInFile(){
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File file = new File(Environment.getExternalStorageDirectory(), ACTIVE_USER_SAV);
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                Gson gson = new Gson();
                gson.toJson(activeUser, bufferedWriter);
                bufferedWriter.flush();

                fileOutputStream.close();
            } else {
                throw new IOException("External storage was not available!");
            }

        } catch (FileNotFoundException e) {
            Log.d("No file", "no file");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void loadFromFile() {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                File file = new File(Environment.getExternalStorageDirectory(), ACTIVE_USER_SAV);

                FileInputStream fileInputStream = new FileInputStream(file);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                Gson gson = new Gson();
                activeUser = gson.fromJson(bufferedReader, User.class);
                //setCached(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
