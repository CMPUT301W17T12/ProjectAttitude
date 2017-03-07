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


        Mood mood = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_main_item, parent, false);
        }

        TextView tvEmotionalState = (TextView) convertView.findViewById(R.id.emotionalStateTextView);

        tvEmotionalState.setText(mood.getEmotionState());

        return convertView;
    }
}
