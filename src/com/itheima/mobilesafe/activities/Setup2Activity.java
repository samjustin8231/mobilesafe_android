package com.itheima.mobilesafe.activities;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {
	private TelephonyManager tm;
	private ImageView iv_setup2_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
		//�ж�sim���󶨵�״̬�� ���sim�����޸�ͼ�ꡣ
		String savedSim = sp.getString("sim", "");
		if (!TextUtils.isEmpty(savedSim)) {
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
		
	}

	public void next(View view) {
		//�ж� �Ƿ����sim�� ���û�а���ʾ�û��󶨡� 
		String savedSim = sp.getString("sim", "");
		if (TextUtils.isEmpty(savedSim)) {
			Toast.makeText(this, "���Ȱ�sim��", 1).show();
			return;
		}
		
		loadActivity(Setup3Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	public void pre(View view) {
		loadActivity(Setup1Activity.class);
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

	/**
	 * ��sim����Ӧ�ĵ���¼�
	 * 
	 * @param view
	 */
	public void bindSim(View view) {
		// �ж��Ƿ��Ѿ�����sim����
		String savedSim = sp.getString("sim", "");
		if (TextUtils.isEmpty(savedSim)) {
			String sim = tm.getSimSerialNumber();// ��ȡsim����Ψһ���š�
			Editor editor = sp.edit();
			editor.putString("sim", sim);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.lock);
		}else{//ԭ���󶨹�sim��
			Editor editor = sp.edit();
			editor.putString("sim", null);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}
	}
}
