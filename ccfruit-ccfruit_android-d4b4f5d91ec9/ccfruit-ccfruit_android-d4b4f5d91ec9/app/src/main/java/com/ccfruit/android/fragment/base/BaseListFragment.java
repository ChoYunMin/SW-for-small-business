package com.ccfruit.android.fragment.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ccfruit.android.util.CustomAdapter;

import java.util.ArrayList;

/**
 * Created by betas on 1/4/16.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements AdapterView.OnItemClickListener {
    protected AbsListView list = null;
    protected CustomAdapter<T> adapter = null;

    private ArrayList<T> items = new ArrayList<T>();

    private int item_layout = 0;

    public void onCreate(Bundle savedInstanceState, int layout, int item) {
        super.onCreate(savedInstanceState, layout);

        this.item_layout = item;
    }

    @Override
    public void onPreLoad() {
        super.onPreLoad();

        this.list = (AbsListView)findViewById(android.R.id.list);
        this.list.setEmptyView(findViewById(android.R.id.empty));

        this.list.setOnItemClickListener(this);

        adapter = new CustomAdapter<T>(getContext(), item_layout, items) {
            @Override
            public CustomAdapter.ViewHolder createHolder(View view) {
                ViewHolder holder = new ViewHolder();

                holder.put("root", view);
                onCreateItem(holder, view);

                return holder;
            }

            @Override
            public void updateView(int idx, CustomAdapter.ViewHolder holder, T item) {
                onUpdateItem(idx, holder, item);
            }
        };

        this.list.setAdapter(adapter);
    }

    public void setEmptyText(String str) {
        ((TextView)findViewById(android.R.id.empty)).setText(str);
    }

    public abstract void onCreateItem(CustomAdapter.ViewHolder holder, View v);
    public abstract void onUpdateItem(int idx, CustomAdapter.ViewHolder holder, T item);

    public AbsListView getListView() {
        return list;
    }
    public View getItemView(int pos) {
        final int firstListItemPosition = list.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + list.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return list.getAdapter().getView(pos, null, list);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return list.getChildAt(childIndex);
        }
    }
    public void clear() {
        this.items.clear();

        adapter.notifyDataSetChanged();
    }

    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    public void setItems(ArrayList<T> arr) {
        this.items.clear();
        this.items.addAll(arr);

        adapter.notifyDataSetChanged();
    }

    public T getItem(int idx) {
        return items.get(idx);
    }

    public void onItemClick(int position, T item) {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(getListView() instanceof ListView) {
            position -= ((ListView)getListView()).getHeaderViewsCount();
        }
        onItemClick(position, items.get(position));
    }
}
