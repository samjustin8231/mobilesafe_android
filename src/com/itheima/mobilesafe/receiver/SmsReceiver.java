package com.itheima.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.itheima.mobilesafe.R;

public class SmsReceiver extends BroadcastReceiver {
	
	private LocationManager lm;
	private MyListener listener;
	private static final String TAG = "SmsReceiver";
	private DevicePolicyManager dpm;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"�ж��ŵ�����");
		//�ж��Ƿ������ֻ�������
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protectingstatus = sp.getBoolean("protectingstatus", false);
		if(protectingstatus){
			Log.i(TAG,"�ֻ�����״̬��ʼ�ģ���������Ķ���ָ��");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			for(Object obj :objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				String body = smsMessage.getMessageBody();
				if("#*location*#".equals(body)){
					Log.i(TAG,"��ȡ�ֻ���λ�ã����ҷ��ظ���ȫ����");
					abortBroadcast();//��ֹ�㲥������С͵��������
					//��ȡ�ֻ���λ�ã����ҷ��ء�
					lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
					listener = new MyListener(sp.getString("safenumber", ""));
					lm.requestLocationUpdates("gps", 0, 0, listener);
					
					
				}else if("#*alarm*#".equals(body)){
					Log.i(TAG,"���ű�������");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();
				}else if("#*wipedata*#".equals(body)){
					Log.i(TAG,"Զ��������ݡ�");
					dpm.wipeData(0);
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body)){
					Log.i(TAG,"Զ��������");
					dpm.resetPassword("123", 0);
					dpm.lockNow();
					abortBroadcast();
				}
			}
		}
	}
	
	
	private class MyListener implements LocationListener{
		
		private String safenumber;
		public MyListener(String safenumber) {
			this.safenumber = safenumber;
		}

		//��λ�ñ仯��ʱ����õķ���
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude:"+location.getLatitude();
			String longitude  = "longitude:"+location.getLongitude();
			String accuarcy = "accuracy:"+location.getAccuracy();
			SmsManager smsManager = SmsManager.getDefault();
			
			smsManager.sendTextMessage(safenumber, null, latitude+"-"+longitude+"-"+accuarcy, null, null);
			lm.removeUpdates(listener);
			listener = null;
		}

		//��״̬�仯��ʱ����õķ���������--��������  ������--������
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		//��һ��λ���ṩ�߿��õ�ʱ��
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		//��һ��λ���ṩ�߲����õ�ʱ��
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}
}
