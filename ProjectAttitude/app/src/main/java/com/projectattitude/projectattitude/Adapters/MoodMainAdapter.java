package com.projectattitude.projectattitude.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projectattitude.projectattitude.Objects.Mood;
import com.projectattitude.projectattitude.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.rgb;

/**
 * Created by Vuk on 2/27/2017.
 */

public class MoodMainAdapter extends ArrayAdapter<Mood> {

    public MoodMainAdapter(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Anger", rgb(255, 0, 0)); //red
        map.put("Confusion", rgb(217, 217, 217)); //grey
        map.put("Disgust", rgb(179, 179, 0)); //green/yellow
        map.put("Fear", rgb(46, 46, 31)); //black
        map.put("Happiness", rgb(68, 204, 0)); //green
        map.put("Sadness", rgb(191, 128, 64)); //brown
        map.put("Shame", rgb(51, 153, 255)); //blue
        map.put("Surprise", rgb(204, 51, 255)); //purple



        Mood mood = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_main_item, parent, false);
        }

        TextView tvEmotionalState = (TextView) convertView.findViewById(R.id.emotionalStateTextView);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView tvTrigger = (TextView) convertView.findViewById(R.id.triggerTextView);
        TextView tvSocialSituation = (TextView) convertView.findViewById(R.id.socialSituationTextView);

        //emotional state will never be null because we error check for it in the CreateMoodActivity
        tvEmotionalState.setText(mood.getEmotionState());
        tvDate.setText(mood.getMoodDate().toString());

        //color background
        Integer val = map.get(mood.getEmotionState());
        convertView.setBackgroundColor(val);

        if(mood.getTrigger().equalsIgnoreCase("")){
            tvTrigger.setVisibility(View.GONE);
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvTrigger.setText("Reason: " + mood.getTrigger());
        }

        if(mood.getSocialSituation().equalsIgnoreCase("Select an social situation")){
            tvSocialSituation.setVisibility(View.GONE);
        }
        else{
            //fix this hardcoded string with string resources, I don't know how to -VUK
            tvSocialSituation.setText("Situation: " + mood.getSocialSituation());
        }

        return convertView;
    }
}
