package com.ccfruit.android.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class Dialog {
	public static interface OnAddressSelectListener {
		public void onAddressSelect(String addr_first, String addr_second, int idx);
	}
	public static interface OnGenderSelectListener {
		public void onGenderSelect(boolean gender);
	}
	public static interface OnInputListener {
		public void onInput(String text);
	}
	
	private Context context = null;
	private String title = "";
	
	public Dialog(Context context) {
		this(context, "");
	}
	public Dialog(Context context, String title) {
		this.context = context;
		this.title = title;
	}
	public void alert(String message) {
		alert(message, null);
	}
	public void alert(String message, OnClickListener listener) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle(title);
		ab.setMessage(message);
		ab.setCancelable(false);
		if (listener == null) {
			listener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
		}
		ab.setPositiveButton("확인", listener);
		ab.show();
	}

	public void list(String[] arr, OnClickListener listener) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle(title);
		ab.setItems(arr, listener);
		ab.show();
	}
	
	public void list(ArrayList<String> list, OnClickListener listener) {
		String[] arr = new String[list.size()];
		list.toArray(arr);
		
		list(arr, listener);
	}
	
	public void confirm(String message, OnClickListener listener) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle(title);
		ab.setMessage(message);
		ab.setPositiveButton("확인", listener);
		ab.setNegativeButton("취소", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ab.show();
	}
	
	public void input(String hint, int method, final OnInputListener listener) {
		final EditText edit = new EditText(context);
		edit.setHint(hint);
		edit.setInputType(method);
		edit.setMaxLines(1);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle(title);
		ab.setView(edit);
		ab.setPositiveButton("확인", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listener.onInput(edit.getText().toString());
			}
		});
		ab.setNegativeButton("취소", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ab.show();
	}
	
	public void genderPicker(final OnGenderSelectListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("성별 선택");
		builder.setItems(new String[] {"남", "여"}, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, final int idx) {
				dialog.dismiss();
		
				listener.onGenderSelect((idx == 0));
			}
		}).show();
	}

	public void datePicker(OnDateSetListener listener) {
		datePicker(0, listener);
	}
	public void datePicker(int id, OnDateSetListener listener) {
		Calendar c = Calendar.getInstance();
		datePicker(id, listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}
	public void datePicker(OnDateSetListener listener, int year, int month, int day) {
		datePicker(0, listener, year, month, day);
	}
	public void datePicker(int id, OnDateSetListener listener, int year, int month, int day) {
		DatePickerDialog dialog = new DatePickerDialog(context, listener, year, month, day);
		dialog.getDatePicker().setId(id);
		dialog.setTitle("날짜를 선택해주세요");
		dialog.show();
	}
}
