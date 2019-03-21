package com.ccfruit.android.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ccfruit.android.Preference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageDownloader extends Thread {
	public static interface OnDownloadListener{
		public void onDownloadEnd();
	}
	
	private ImageDownloader() {
		start();
	}
	private static ImageDownloader _instance = new ImageDownloader();
	public static ImageDownloader getInstance() {
		return _instance;
	}
	
	private class Task {
		public String url;
		public ImageView view;
		public ProgressBar progress = null;
		public Bitmap no_image = null;
		public OnDownloadListener listener = null;
		public boolean end = false;
		public int sample = 2;
	}
	
	private HashMap<String, Bitmap> resources = new HashMap<String, Bitmap>();
	private ArrayList<Task> tasks = new ArrayList<Task>(); 
	
	private Bitmap _download(String url, int sample) throws MalformedURLException, IOException {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither=false;                    	 				//Disable Dithering mode
		opts.inPurgeable=true;                   			//Tell to gc that whether it needs free memory, the Bitmap can be cleared
		opts.inInputShareable=true;              		//Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		opts.inSampleSize=sample;
	    
		HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
		
		return BitmapFactory.decodeStream(conn.getInputStream(), null, opts);
	}

	public void add(String url, ImageView view, int sample) {
		add(url, view, sample, null);
	}
	public void add(String url, ImageView view, int sample, ProgressBar progress) {
		add(url, view, Preference.no_image, sample, progress, null, true);
	}
	public void add(String url, ImageView view, int sample, ProgressBar progress, OnDownloadListener listener) {
		add(url, view, Preference.no_image, sample, progress, listener, true);
	}
	public void add(String url, ImageView view, Bitmap defaultImage, int sample, ProgressBar progress, OnDownloadListener listener, boolean clear) {
		if(url == null || url.length() <= 0) {
			view.setImageBitmap(defaultImage);
			if(progress != null)
				progress.setVisibility(View.GONE);
			
			return;
		}
		
		if(resources.containsKey(url)) {
			Bitmap bitmap = resources.get(url);
			
			if(bitmap == null || bitmap == Preference.no_image) {
				view.setImageBitmap(defaultImage);
			} else {
				view.setImageBitmap(bitmap);
			}
			
			if(progress != null) {
				progress.setVisibility(View.GONE);
			}
			
			if(listener != null) {
				listener.onDownloadEnd();
			}
			
			return;
		}
		
		if(url != null && view != null && url.length() > 0) {
			Task task = new Task();
			task.url = url;
			task.view = view;
			task.no_image = defaultImage;
			task.progress = progress;
			task.listener = listener;
			task.sample = sample;
			tasks.add(task);

			if(clear)
				task.view.setImageBitmap(null);
			
			if(progress != null) {
				progress.setVisibility(View.VISIBLE);
			}
			
			synchronized (this) {
				notify();
			}
		}
	}
	
	public Bitmap getImageBitmap(String url) {
		return resources.get(url);
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				// List Copy
				ArrayList<Task> tmp_tasks = new ArrayList<Task>();
				tmp_tasks.addAll(tasks);
				
				// Task Run
				for(final Task task: tmp_tasks) {
					if(task.end) continue;
					
					if(!resources.containsKey(task.url)) {
						try {
							final Bitmap bitmap = _download(task.url, task.sample);
							resources.put(task.url, bitmap);
							
							task.view.post(new Runnable() {
								public void run() {
									task.view.setImageBitmap(bitmap);
										
									if(task.progress != null) {
										task.progress.setVisibility(View.GONE);
									}

									if(task.listener != null) {
										task.listener.onDownloadEnd();
									}
								}
							});
						} catch (Exception e) {
							resources.put(task.url, null);
							
							task.view.post(new Runnable() {
								public void run() {
									task.view.setImageBitmap(task.no_image);
									
									if(task.progress != null) {
										task.progress.setVisibility(View.GONE);
									}
									
									if(task.listener != null) {
										task.listener.onDownloadEnd();
									}
								}
							});
						}
					} else {
						task.view.post(new Runnable() {
							public void run() {
								if(task.no_image != null)
									task.view.setImageBitmap(resources.get(task.url));
								
								if(task.progress != null) {
									task.progress.setVisibility(View.GONE);
								}
								
								if(task.listener != null) {
									task.listener.onDownloadEnd();
								}
							}
						});
					}
					
					tasks.remove(task);
				}
				
				if(tasks.size() == 0) {
					tasks.clear();
					synchronized(this) {
						wait();
					}
				}
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
