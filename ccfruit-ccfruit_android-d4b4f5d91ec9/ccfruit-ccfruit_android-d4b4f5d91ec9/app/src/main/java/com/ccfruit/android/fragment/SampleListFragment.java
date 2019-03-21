package com.ccfruit.android.fragment;

import android.os.Bundle;
import android.view.View;

import com.ccfruit.android.R;
import com.ccfruit.android.fragment.base.BasePagedListFragment;
import com.ccfruit.android.item.SampleItem;
import com.ccfruit.android.util.CustomAdapter;

import java.util.LinkedList;

/**
 * Created by betas on 7/16/16.
 */
public class SampleListFragment extends BasePagedListFragment<SampleItem> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_list, R.layout.item_sample);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onScrollLast(int page) {
        LinkedList<SampleItem> arr = new LinkedList<SampleItem>();
        for(int i=0; i<20; i++) {
            arr.add(new SampleItem("TEST"+i, "설명설명설명설명설명설명설명설명설명설명설명\n설명설명설명설명"));
        }

        currentPage++;
        setItems(currentPage, arr);
    }

    @Override
    public void onCreateItem(CustomAdapter.ViewHolder holder, View v) {
        holder.put("title", v.findViewById(R.id.txt_title));
        holder.put("desc", v.findViewById(R.id.txt_desc));
    }

    @Override
    public void onUpdateItem(int idx, CustomAdapter.ViewHolder holder, SampleItem item) {
        holder.getTextView("title").setText(item.getTitle());
        holder.getTextView("desc").setText(item.getDesc());
    }
}
