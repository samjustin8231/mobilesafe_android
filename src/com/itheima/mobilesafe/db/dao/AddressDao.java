package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �绰������������ݿ�Ĳ�ѯ������
 * @author Administrator
 *
 */
public class AddressDao {
	private static final String path = "/data/data/com.itheima.mobilesafe/files/address.db";
	
	/**
	 * ��ѯ�绰����Ĺ�����  
	 * @param number �绰����
	 * @return ������λ����Ϣ
	 */
	public static String find (String number){
		String location = null;
		//��ʹ�����asset���ݿ��ļ�֮ǰ�����ļ��������ֻ����ڲ��洢ϵͳ����/data/data/����/filesĿ¼��
		
		//path ���ݿ��·�� 
		//ֱ�Ӳ���file:///android_asset·��д�� ֻ�Ƕ���ҳ��Դ��Ч����������ݿ� ��������д·��
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select location from data2 where id = (select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});
		if(cursor.moveToNext()){
			location = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return location;
	}
}
