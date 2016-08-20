package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_lostfind_number;
	private ImageView iv_lostfind_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �ж��û��Ƿ���й������򵼣�
		boolean finishsetup = sp.getBoolean("finishsetup", false);
		if (finishsetup) { // ������ù� ����ʾ������ui����
			//�Ѿ���ɹ������� ������������ui���档
			setContentView(R.layout.activity_lostfind);
			tv_lostfind_number = (TextView) findViewById(R.id.tv_lostfind_number);
			tv_lostfind_number.setText(sp.getString("safenumber", ""));
			
			iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
			boolean protectingstatus = sp.getBoolean("protectingstatus", false);
			if(protectingstatus){
				iv_lostfind_status.setImageResource(R.drawable.lock);
			}else{
				iv_lostfind_status.setImageResource(R.drawable.unlock);
			}
		}else{
			//����ҳ�浽�����򵼽��档
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			this.finish();
		}
	}
	
	
	public void reEntrySetup(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		this.finish();
	}
}
