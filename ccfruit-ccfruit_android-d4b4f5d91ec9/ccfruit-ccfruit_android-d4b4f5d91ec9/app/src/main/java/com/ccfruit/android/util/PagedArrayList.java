package com.ccfruit.android.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class PagedArrayList<T> extends TreeMap<Integer, LinkedList<T>> {
	private static final long serialVersionUID = 1L;
	
	protected int page_size;
	protected boolean order_desc = true;
	
	public PagedArrayList() {
		this(20, true);
	}
	public PagedArrayList(int max_size) {
		this(max_size, true);
	}
	public PagedArrayList(boolean order_desc) {
		this(20, order_desc);
	}
	public PagedArrayList(int max_size, boolean order_desc) {
		this.page_size = max_size;
		this.order_desc = order_desc;
	}
	
	public void insert(T data) {
		insert(1, data);
	}
	public void insert(int page, T data) {
		if(!containsKey(page))
			put(page, new LinkedList<T>());
		
		if(order_desc) {
			get(page).addLast(data);
		} else {
			get(page).addFirst(data);
		}
		
		while(get(page).size() > page_size) {
			T tmp;
			if(order_desc) {
				tmp = get(page).removeFirst();
			} else {
				tmp = get(page).removeLast();
			}
			page++;
			if(!containsKey(page))
				put(page, new LinkedList<T>());

			if(order_desc) {
				get(page).addLast(tmp);
			} else {
				get(page).addFirst(tmp);
			}
		}
	}
	
	public boolean containsKey(Integer key) {
		if(order_desc)
			return super.containsKey(-key);
		return super.containsKey(key);
	}
	
	@Override
	public LinkedList<T> put(Integer key, LinkedList<T> value) {
		if(order_desc)
			return super.put(-key, value);
		return super.put(key, value);
	}
	
	public LinkedList<T> get(Integer key) {
		if(order_desc)
			return super.get(-key);
		return super.get(key);
	}
	
	public ArrayList<T> getList() {
		ArrayList<T> datas = new ArrayList<T>();
		for(int p: this.keySet()) {
			datas.addAll(super.get(p));
		}
		
		return datas;
	}
	
	public int getPageSize() {
		return this.page_size;
	}
}
