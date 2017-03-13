package com.projectattitude.projectattitude.Objects;

import java.util.HashMap;

import com.projectattitude.projectattitude.R;

/**
 * Created by rfsh on 2017-03-13.
 */

public class EmoticonMap<S, I extends Number> extends HashMap {
    public EmoticonMap(){
        // Icons are sourced from https://icons8.com/

        this.put("Anger", R.drawable.ic_anger);
        this.put("Confusion", R.drawable.ic_confusion);
        this.put("Disgust", R.drawable.ic_disgust);
        this.put("Fear", R.drawable.ic_fear);
        this.put("Happiness", R.drawable.ic_happiness);
        this.put("Sadness", R.drawable.ic_sadness);
        this.put("Shame", R.drawable.ic_shame);
        this.put("Surprise", R.drawable.ic_surprise);
    }
}