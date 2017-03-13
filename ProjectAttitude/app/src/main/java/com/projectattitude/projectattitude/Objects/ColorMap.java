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
        this.put("Anger", rgb(227, 51, 62)); //red
        this.put("Confusion", rgb(237, 139, 95)); //grey
        this.put("Disgust", rgb(192, 202, 85)); //green/yellow
        this.put("Fear", rgb(104, 79, 21)); //black
        this.put("Happiness", rgb(127, 199, 175)); //green
        this.put("Sadness", rgb(145, 145, 133)); //brown
        this.put("Shame", rgb(0, 88, 133)); //blue
        this.put("Surprise", rgb(227, 104, 32)); //purple
        this.put("", rgb(255, 255, 255));
    }
}
