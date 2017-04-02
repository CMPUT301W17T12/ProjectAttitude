package com.projectattitude.projectattitude.Objects;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by cs3 on 4/2/17.
 * This is used in the profile page to display followers/who you follow
 * It is used to create a list which fits 5 names, then scrolls for the rest
 * Taken from: http://stackoverflow.com/questions/11295080/android-wrap-content-is-not-working-with-listview
 * On 4/2/17
 */

public class MyListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public MyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Handles the drawing of the list view when it is updated
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getChildAt(0).getHeight() *5 ;
            setLayoutParams(params);

        }

        super.onDraw(canvas);
    }

}
