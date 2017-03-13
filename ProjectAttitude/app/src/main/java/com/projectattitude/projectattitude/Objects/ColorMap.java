package com.projectattitude.projectattitude.Objects;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.rgb;

/**
 * Created by Chris on 3/8/2017.
 * This object functions the exact same as a HashMap, except it contains preset values for
 * associating color and emotion
 * @See HashMap
 *
 * How to use:
 *      ColorMap<String, Integer> map = new ColorMap<>(); //Creating the Hash
 *      r1.setBackgroundColor((Integer) map.get(mood.getEmotionState())); //r1 is the view, have to cast to int though
 */

public class ColorMap<S, I extends Number> extends HashMap {
    public ColorMap(){
        this.put("Anger", rgb(255, 0, 0)); //red
        this.put("Confusion", rgb(217, 217, 217)); //grey
        this.put("Disgust", rgb(179, 179, 0)); //green/yellow
        this.put("Fear", rgb(46, 46, 31)); //black
        this.put("Happiness", rgb(68, 204, 0)); //green
        this.put("Sadness", rgb(191, 128, 64)); //brown
        this.put("Shame", rgb(51, 153, 255)); //blue
        this.put("Surprise", rgb(204, 51, 255)); //purple
        this.put("", rgb(255, 255, 255));
    }
}
