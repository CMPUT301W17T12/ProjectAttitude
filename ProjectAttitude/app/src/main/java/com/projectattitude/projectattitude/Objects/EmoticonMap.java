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

import com.projectattitude.projectattitude.R;

import java.util.HashMap;

/**
 * Created by rfsh on 2017-03-13.
 * This object functions the exact same as a HashMap, except it contains preset values for
 * associating emoticons and emotion
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