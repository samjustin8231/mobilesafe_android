package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"���������ֻ������ˡ�����");
		//�ж��û��Ƿ������ֻ��������ܡ�
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protectingstatus = sp.getBoolean("protectingstatus", false);
		if(protectingstatus){
			Log.i(TAG,"�ֻ������ǿ���i״̬");
			//�жϵ�ǰ�ֻ������sim�� �� ��ԭ���󶨵�sim���Ƿ�һ�¡�
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber();//��ǰ�ֻ������sim������
			String bindSim = sp.getString("sim", "")+"adfsagb";
			if(bindSim.equals(currentSim)){
				//sim��û�б仯��������Ŀ�
			}else{
				//sim���仯�ˡ����п����ֻ�������͵͵�ں�̨����һ���������š�
				SmsManager smsManager = SmsManager.getDefault();
				String safenumber = sp.getString("safenumber", "");
				smsManager.sendTextMessage(safenumber, null, "sim changed", null, null);
			}
		}else{
			Log.i(TAG,"�ֻ�������û�п�����״̬");
		}
		
	}

}
