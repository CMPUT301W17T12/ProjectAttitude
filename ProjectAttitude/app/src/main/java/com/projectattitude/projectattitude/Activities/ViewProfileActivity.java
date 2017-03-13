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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.projectattitude.projectattitude.Controllers.UserController;
import com.projectattitude.projectattitude.Objects.User;
import com.projectattitude.projectattitude.R;


/**
 * This is a temporary profile, just shows name and number of moods
 */
public class ViewProfileActivity extends AppCompatActivity {
    private UserController userController = UserController.getInstance();

    private TextView nameView;
    private TextView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        nameView = (TextView) findViewById(R.id.profileUname);
        countView = (TextView) findViewById(R.id.profileCount);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Integer count = getIntent().getIntExtra("moodCount", 0);
        nameView.setText(userController.getActiveUser().getUserName());
        countView.setText("Number of moods: " + Integer.toString(count));
    }

}
