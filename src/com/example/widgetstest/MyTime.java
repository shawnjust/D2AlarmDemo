package com.example.widgetstest;

import java.util.ArrayList;

import android.text.format.Time;
import android.util.Log;

public class MyTime {
	boolean isOn;
	ArrayList<Integer> week;
	// int month;
	// int day;
	int hour;
	int minute;

	public MyTime(boolean isOn, ArrayList<Integer> week, int hour, int minute) {
		this.isOn = isOn;
		this.week = week;
		this.hour = hour;
		this.minute = minute;
	}

	public float isOK(int nowHour, int nowMinute, int nowWeekday) {
		float angle = 0.0f;
		if (compare(nowHour, nowMinute, nowWeekday)) {
			Log.i("----------------", "" + nowHour + " " + nowMinute + " " + nowWeekday);
			for (int i = 0; i < week.size(); ++i) {
				if (week.get(i) == nowWeekday && this.biggerThan(nowHour, nowMinute)) {
					float a = hour / 12 * 30 + minute / 2;
					float b = nowHour / 12 * 30 + nowMinute / 2;
					angle = a - b;
				} else if (week.get(i) == nowWeekday + 1 && this.smallerThan(nowHour, nowMinute)) {
					float a = hour / 12 * 30 + minute / 2;
					float b = nowHour / 12 * 30 + nowMinute / 2;
					angle = a - b;
				}
			}
		}
		return angle;
	}

	public boolean compare(int nowHour, int nowMinute, int nowWeekday) {
//		Time time = new Time();
//		time.setToNow();
//		int nowHour = time.hour;
//		int nowMinute = time.minute;
//		int nowWeekday = time.weekDay;
		if (isOn) {
			for (int i = 0; i < week.size(); ++i) {
				if (week.get(i) == nowWeekday && this.biggerThan(nowHour, nowMinute) || week.get(i) == nowWeekday + 1
						&& this.smallerThan(nowHour, nowMinute)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean biggerThan(int nowHour, int nowMinute) {
		if (hour == nowHour && minute > nowMinute || hour > nowHour) {
			return true;
		}
		return false;
	}

	public boolean smallerThan(int nowHour, int nowMinute) {
		if (hour == nowHour && minute < nowMinute || hour < nowHour) {
			return true;
		}
		return false;
	}
}
