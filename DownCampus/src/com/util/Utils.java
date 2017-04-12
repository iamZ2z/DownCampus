package com.util;

import java.util.Calendar;

public class Utils {
	// 存储路径
	private static String path = "/sdcard/MobileCampus/";
	// 当前日期
	private Calendar cal = Calendar.getInstance();
	private String mCalendar = cal.get(Calendar.YEAR) + "-"
			+ (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
	public String getmCalendar() {
		return mCalendar;
	}
	public void setmCalendar(String mCalendar) {
		this.mCalendar = mCalendar;
	}
	
	private String role;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
