package com.example.widgetstest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button addAlarm;
	Button test;
	MyTime myTime;

	public void init() {
		addAlarm = (Button) findViewById(R.id.addAlarm);
		addAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		ArrayList<Integer> abcc = new ArrayList<Integer>();
		abcc.add(new Integer(0));
		myTime = new MyTime(true, abcc, 13, 0);
		Time time = new Time();
		time.setToNow();
		Log.i("------------------------", time.hour + " " + time.minute + " " + time.weekDay);
		Log.i("------------------------", "" + myTime.isOK(time.hour, time.minute, time.weekDay));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
