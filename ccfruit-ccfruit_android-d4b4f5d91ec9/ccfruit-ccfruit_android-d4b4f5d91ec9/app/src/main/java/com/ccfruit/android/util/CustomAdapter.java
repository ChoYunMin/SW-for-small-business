package com.ccfruit.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccfruit.android.ui.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CustomAdapter<T> extends BaseAdapter {
	public static class ViewHolder {
		private HashMap<String, View> holder = new HashMap<String, View>();
		
		public void put(String name, View view) {
			holder.put(name, view);
		}
		public View get(String name) {
			return holder.get(name);
		}
		public TextView getTextView(String name) {
			return (TextView)get(name);
		}
		public ImageView getImageView(String name) {
			return (ImageView)get(name);
		}
		public NetworkImageView getNetworkImageView(String name) {
			return (NetworkImageView)get(name);
		}
		public Button getButton(String name) {
			return (Button)get(name);
		}
		public ImageButton getImageButton(String name) {
			return (ImageButton)get(name);
		}
	}
	
	protected Context context 		= null;
	protected int layoutid 			= 0;
	protected ArrayList<T> items 	= null;
	
	public CustomAdapter(Context context, int layoutid, ArrayList<T> items) {
		this.context 	= context;
		this.layoutid	= layoutid;
		this.items		= items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public T getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutid, null);
			holder = createHolder(convertView);

			convertView.setTag(holder);
		} else if(convertView.getTag() == null) {
			holder = createHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		updateView(index, holder, getItem(index));
		
		return convertView;
	}
	
	public abstract ViewHolder createHolder(View view);
	public abstract void updateView(int idx, ViewHolder holder, T item);
}
