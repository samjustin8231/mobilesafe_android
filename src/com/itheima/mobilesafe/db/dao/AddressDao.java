package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 电话号码归属地数据库的查询工具类
 * @author Administrator
 *
 */
public class AddressDao {
	private static final String path = "/data/data/com.itheima.mobilesafe/files/address.db";
	
	/**
	 * 查询电话号码的归属地  
	 * @param number 电话号码
	 * @return 归属地位置信息
	 */
	public static String find (String number){
		String location = null;
		//在使用这个asset数据库文件之前，把文件拷贝到手机的内部存储系统里面/data/data/包名/files目录。
		
		//path 数据库的路径 
		//直接采用file:///android_asset路径写法 只是对网页资源有效，如果是数据库 不能这样写路径
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
