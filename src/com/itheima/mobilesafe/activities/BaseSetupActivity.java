package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {

	/**
	 * �ڸ��������ʼ����sharedpreference
	 */
	protected SharedPreferences sp;
	
	/**
	 * 1.������������һ������ʶ����
	 */
	protected GestureDetector mGestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//2.��ʼ������ʶ����  �ڶ���������������ע�������ʶ��ļ�����
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		
	}
	
	/**
	 * ������ʵ�ֵ���ʾ��һ������
	 */
	protected abstract void showNext();
	
	/**
	 * ������ʵ�ֵ���ʾ��һ������
	 */
	protected abstract void showPre();
	
	
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

		//���û�����Ļ����ָ�Ҷ�����������ʱ����õķ�����
		//e1��ָ�Ӵ�����Ļ��Ӧ�Ķ���
		//e2��ָ�뿪��Ļ��һ˲���Ӧ�Ķ���
		//m/s km/h
		//velocityX ˮƽ������ٶ�  px/m
		//velocityY ��ֱ������ٶ�
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if(Math.abs(e1.getRawY() - e2.getRawY()) > 100 ){ //��Ч�Ķ���{
				Toast.makeText(getApplicationContext(), "������ô����", 0).show();
				return true;
			}
			
			if(e1.getRawX() - e2.getRawX() > 200){
				//��ʾ��һ������
				//���಻֪����ʾ��һ������Ĵ�����ôд
				showNext();
				return true;
			}
			
			if(e2.getRawX() - e1.getRawX() > 200){
				//��ʾ��һ�����档
				//���಻֪����ʾ��һ������Ĵ�����ôд
				showPre();
				return true;
			}
			
			
			return super.onFling(e1, e2, velocityX, velocityY);
		}
		
	}
	
	
	/**
	 * ������ʾһ��activity
	 * @param cls Ҫ��ʾ��activity���ֽ���
	 */
	public void loadActivity(Class<?> cls){
		Intent intent = new Intent(this,cls);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//3.�����ǵ�����ʶ��������ʱ��
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	
}
