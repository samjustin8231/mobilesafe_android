package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * �ṩ��ϵ�˵���Ϣ
 * 
 * @author Administrator
 * 
 */
public class ContactInfoProvider {

	/**
	 * ��ȡϵͳ�������е���ϵ����Ϣ��
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static List<ContactInfo> getContactInfos(Context context) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ContentResolver resolver = context.getContentResolver();
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		Uri rawcontacturi = Uri
				.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		// 1.��ѯraw_contact �� id����ѯ����
		Cursor cursor = resolver.query(rawcontacturi,
				new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			System.out.println("��ϵ�˵�id��" + id);
			if (id != null) {
				ContactInfo info = new ContactInfo();
				Cursor dataCursor = resolver.query(datauri, new String[] {
						"data1", "mimetype" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						info.setName(data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						info.setPhone(data1);
					}
				}
				dataCursor.close();
				infos.add(info);
			}
		}
		cursor.close();
		return infos;
	}
}
