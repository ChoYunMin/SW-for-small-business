package com.ccfruit.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ccfruit.android.Preference;
import com.ccfruit.android.R;

public class DotPageStatusView extends LinearLayout {
	private int max = 0;
	
	public DotPageStatusView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public DotPageStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setGravity(Gravity.CENTER);
	}
	
	public void setMax(int max) {
		removeAllViews();
		
		this.max = max;
		for(int i=0; i<max; i++) {
			ImageView view = new ImageView(getContext());
			LayoutParams params = new LayoutParams(Preference.getPixelByDp(getContext(), 10), Preference.getPixelByDp(getContext(), 10));
			params.setMargins(Preference.getPixelByDp(getContext(), 5), 0, Preference.getPixelByDp(getContext(), 5), 0);
			addView(view, params);
		}
		
		setPos(0);
	}
	public int getMax() {
		return max;
	}
	
	public void setPos(int pos) {
		for(int i = 0; i < getChildCount(); i++){
			ImageView ivFirstDot = (ImageView)getChildAt(i);
			ivFirstDot.setBackgroundResource(R.drawable.img_dot_off);
			if(i == pos){
				ivFirstDot.setBackgroundResource(R.drawable.img_dot_on);
			}
		}
	}
}