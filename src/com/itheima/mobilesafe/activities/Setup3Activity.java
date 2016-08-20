package com.itheima.mobilesafe.activities;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_setup3_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);
		et_setup3_phone.setText(sp.getString("safenumber", ""));
	}
	public void next(View view){
		String safenumber = et_setup3_phone.getText().toString().trim();
		if(TextUtils.isEmpty(safenumber)){
			Toast.makeText(this, "«Î…Ë÷√∞≤»´∫≈¬Î", 0).show();
			return;
		}
		Editor editor = sp.edit();
		editor.putString("safenumber", safenumber);
		editor.commit();
		loadActivity(Setup4Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	public void pre(View view){
		loadActivity(Setup2Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	@Override
	protected void showNext() {
		next(null);
		
	}
	@Override
	protected void showPre() {
		pre(null);
	}
}
