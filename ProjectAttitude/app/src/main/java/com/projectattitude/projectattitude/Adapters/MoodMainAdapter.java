package com.projectattitude.projectattitude.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Base64;
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

import java.util.ArrayList;

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
        TextView tvTrigger = (TextView) convertView.findViewById(R.id.triggerTextView);
        TextView tvSocialSituation = (TextView) convertView.findViewById(R.id.socialSituationTextView);
        ImageView moodEmoticon = (ImageView) convertView.findViewById(R.id.mainImageView);

        //emotional state will never be null because we error check for it in the CreateMoodActivity
        tvEmotionalState.setText(mood.getEmotionState());
        tvDate.setText(mood.getMoodDate().toString());

        //color background
        Integer val = (Integer) cMap.get(mood.getEmotionState());
        convertView.setBackgroundColor(val);

        int res = (int) eMap.get(mood.getEmotionState());
        moodEmoticon.setImageResource(res);

        if(mood.getTrigger().equalsIgnoreCase("")){
            tvTrigger.setVisibility(View.GONE);
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvTrigger.setText("Reason: " + mood.getTrigger());
        }

        if(mood.getSocialSituation().equalsIgnoreCase("Select a social situation")){
            tvSocialSituation.setVisibility(View.GONE);
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvSocialSituation.setText("Situation: " + mood.getSocialSituation());
        }

        return convertView;
    }
}
