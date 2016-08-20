package com.itheima.mobilesafe.activities;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private SharedPreferences sp;
	private static final String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe_selector, R.drawable.app, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
			R.drawable.atools, R.drawable.settings };
	protected static final String TAG = "HomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		
		gv_home.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0://�ֻ�������
					//�ж��û��Ƿ����ù�����
					String savedPassword = sp.getString("password", null);
					if(TextUtils.isEmpty(savedPassword)){
						//û�����ù�����
						Log.i(TAG,"û�����ù����룬������������Ի���");
						showSetupPasswordDialog();
					}else{
						//���ù�����
						Log.i(TAG,"���ù�����,������������Ի���");
						showEnterPasswordDialog();
					}
					break;
				}
			}
		});
	}

	/**
	 * ��������Ի���
	 */
	protected void showEnterPasswordDialog() {
		
	}

	/**
	 * ��������Ի���
	 */
	protected void showSetupPasswordDialog() {
		//�Զ������ݵĶԻ���
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		builder.setView(view);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// Default Simple Basic Base
	private class HomeAdapter extends BaseAdapter {
		// �����ж��ٸ���Ŀ
		@Override
		public int getCount() {
			return names.length;
		}

		// ����ÿ����Ŀ��view����
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_home_icon);
			TextView tv = (TextView) view.findViewById(R.id.tv_home_name);
			iv.setImageResource(icons[position]);
			tv.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

}
