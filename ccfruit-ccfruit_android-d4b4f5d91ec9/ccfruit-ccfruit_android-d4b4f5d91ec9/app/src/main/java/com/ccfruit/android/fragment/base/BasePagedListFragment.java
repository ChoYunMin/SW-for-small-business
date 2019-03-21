package com.ccfruit.android.fragment.base;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;

import com.ccfruit.android.util.PagedArrayList;

import java.util.LinkedList;

/**
 * Created by betas on 1/4/16.
 */
public abstract class BasePagedListFragment<T> extends BaseListFragment<T> {
    protected PagedArrayList<T> pageData = null;
    protected int currentPage = 0;
    protected boolean loading = false;

    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                onScrollLast(currentPage+1);
                Log.e("currPage", "" + currentPage);
                loading = true;
            } else if(loading && !((totalItemCount - visibleItemCount) <= firstVisibleItem)) {
                loading = false;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    public void onCreate(Bundle savedInstanceState, int layout, int item_layout, boolean desc) {
        pageData = new PagedArrayList<T>(desc);
        super.onCreate(savedInstanceState, layout, item_layout);
    }

    public void onCreate(Bundle savedInstanceState, int layout, int item_layout) {
        onCreate(savedInstanceState, layout, item_layout, false);
    }

    @Override
    public void onPreLoad() {
        currentPage = 0;
        loading = false;

        super.onPreLoad();
        list.setOnScrollListener(new EndlessScrollListener());
    }

    public void setItems(int page, LinkedList<T> data) {
        pageData.put(page, data);

        setItems(pageData.getList());
    }

    @Override
    public void clear() {
        pageData.clear();
        currentPage = 0;
        loading = false;

        super.clear();
    }

    public abstract void onScrollLast(int page);
}
