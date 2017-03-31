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

package com.projectattitude.projectattitude.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectattitude.projectattitude.Objects.ColorMap;
import com.projectattitude.projectattitude.Objects.EmoticonMap;
import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Vuk on 2/27/2017.
 * The MoodMainAdapter updates the listview as moods are manipulated and updated using a MVC model.
 */

public class MoodMainAdapter extends ArrayAdapter<Mood> {

    public MoodMainAdapter(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ColorMap<String, Integer> cMap = new ColorMap<>();
        EmoticonMap<String, Integer> eMap = new EmoticonMap<>();

        Mood mood = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_main_item, parent, false);
        }

        TextView tvEmotionalState = (TextView) convertView.findViewById(R.id.emotionalStateTextView);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView tvMaker = (TextView) convertView.findViewById(R.id.makerTextView);
        TextView tvTrigger = (TextView) convertView.findViewById(R.id.triggerTextView);
        TextView tvSocialSituation = (TextView) convertView.findViewById(R.id.socialSituationTextView);
        ImageView moodEmoticon = (ImageView) convertView.findViewById(R.id.mainImageView);

        //emotional state will never be null because we error check for it in the CreateMoodActivity
        tvEmotionalState.setText(mood.getEmotionState());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        tvDate.setText(sdf.format(mood.getMoodDate()));

        //color background
        Integer val = (Integer) cMap.get(mood.getEmotionState());
        convertView.setBackgroundColor(val);

        int res = (int) eMap.get(mood.getEmotionState());
        moodEmoticon.setImageResource(res);

        if(mood.getMaker() == null){
            tvMaker.setText("Created by: None");
        }
        else{
            tvMaker.setText("Created by: " + mood.getMaker());
        }

        if(mood.getTrigger().equals("")){
            tvTrigger.setText("Trigger: None");
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvTrigger.setText("Reason: " + mood.getTrigger());
        }

        if(mood.getSocialSituation().equals("")){
            tvSocialSituation.setText("Situation: None");
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvSocialSituation.setText("Situation: " + mood.getSocialSituation());
        }

        return convertView;
    }


}
