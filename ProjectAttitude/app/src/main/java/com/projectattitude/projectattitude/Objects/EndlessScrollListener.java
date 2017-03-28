package com.projectattitude.projectattitude.Objects;

import android.widget.AbsListView;

/**
 * Created by Boris on 26/03/2017.
 */
/**
 * Implementing pagination with a scroll view. This class modified the onScrollListener to allow it
 * to load segments using ElasticSearch.
 */

//Taken from http://benjii.me/2010/08/endless-scrolling-listview-in-android/ Mar 26th, 2017 at 17:13
//Can be used by the function ListView.setOnScrollListener(new EndlessScrollListener());
public class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int visibleThreshold = 5;   //amount of items loaded in
    private int currentPage = 0;        //"pages" that have been loaded
    private int previousTotal = 0;
    private boolean loading = true;

    public EndlessScrollListener() {
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading & (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            //Calls elasticsearch function to "load" the next few moods
            //TODO need ElasticSearch function here
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
