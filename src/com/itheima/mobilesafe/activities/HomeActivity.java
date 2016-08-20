package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv  = new TextView(getApplicationContext());
		tv.setText("我是主界面");
		setContentView(tv);
	}
}
