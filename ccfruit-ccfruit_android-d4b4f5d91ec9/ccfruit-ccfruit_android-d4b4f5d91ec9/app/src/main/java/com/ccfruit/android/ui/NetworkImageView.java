package com.ccfruit.android.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ccfruit.android.Preference;
import com.ccfruit.android.network.ImageDownloader;

public class NetworkImageView extends RelativeLayout implements OnClickListener {
	private ImageView image = null;
	private ProgressBar progress = null;
	
	private ImageDownloader.OnDownloadListener listener = null;
	
	private String url = "";
	private int sampleRate = 2;

	private Bitmap no_image = Preference.no_image;

    private boolean circle = false;
	
	public NetworkImageView(Context context, boolean circle) {
		super(context);

		this.circle = circle;
		
		initialize();
	}
	
	public NetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NetworkImageView(Context context, AttributeSet attrs, int theme) {
		super(context, attrs, theme);

        sampleRate = attrs.getAttributeIntValue(null, "sampleRate", 2);
        int no_image = attrs.getAttributeResourceValue(null, "no_image", 0);
        if(no_image != 0) {
            this.no_image = BitmapFactory.decodeResource(getResources(), no_image);
        }
        circle = attrs.getAttributeBooleanValue(null, "circle", false);

		initialize();

		setClick(attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "clickable", false));
	}
	
	public void setSampleRate(int rate) {
		sampleRate = rate;
	}

	public void setNoImage(int drawable) {
		this.no_image = BitmapFactory.decodeResource(getResources(), drawable);
	}

	public void initialize() {
        if(circle) {
            image = new CircleImageView(getContext());
        } else {
            image = new ImageView(getContext());
        }
		progress = new ProgressBar(getContext());
		
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setImageBitmap(no_image);
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(image, lp);
		
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_IN_PARENT);
		addView(progress, lp);
		
		progress.setVisibility(View.GONE);
	}
	
	public void setClick(boolean click) {
		if(click)
			setOnClickListener(this);
	}
	
	public void setOnDownloadListener(ImageDownloader.OnDownloadListener listener) {
		this.listener = listener;
	}
	
	public void setScaleType(ScaleType type) {
		image.setScaleType(type);
	}

	public void setImageURL(String url) {
		this.url = url;
		
		progress.setVisibility(VISIBLE);
        ImageDownloader.getInstance().add(url, image, no_image, sampleRate, progress, listener, true);
	}

	public ImageView getImageView() {
		return image;
	}
	
	@Override
	public void onClick(View v) {
		/*
		if(url != null && url.length() > 0) {
			Intent intent = new Intent(getContext(), ImageActivity.class);
			intent.putExtra("url", url);
			getContext().startActivity(intent);
		}
		*/
	}
}
