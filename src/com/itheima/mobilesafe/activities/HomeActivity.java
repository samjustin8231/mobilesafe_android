package com.itheima.mobilesafe.activities;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.Md5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private SharedPreferences sp;
	private static final String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe_selector, R.drawable.app,
			R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
			R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
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
				case 0:// �ֻ�������
						// �ж��û��Ƿ����ù�����
					String savedPassword = sp.getString("password", null);
					if (TextUtils.isEmpty(savedPassword)) {
						// û�����ù�����
						Log.i(TAG, "û�����ù����룬������������Ի���");
						showSetupPasswordDialog();
					} else {
						// ���ù�����
						Log.i(TAG, "���ù�����,������������Ի���");
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
		// �Զ������ݵĶԻ���
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		et_password = (EditText) view.findViewById(R.id.et_password);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�û���������롣
				String password = et_password.getText().toString().trim();
				if(TextUtils.isEmpty(password)){
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
					return;
				}
				//�õ�ԭ�������������ܺ������
				String savedPassword = sp.getString("password", "");
				if(Md5Utils.encode(password).equals(savedPassword)){
					//������ȷ�������ֻ������Ľ��档
					dialog.dismiss();
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "�������", 1).show();
				}
			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	private EditText et_password;
	private EditText et_password_confirm;
	private Button bt_ok;
	private Button bt_cancel;
	private AlertDialog dialog;
	//��Ԫģʽ

	/**
	 * ��������Ի���
	 */
	protected void showSetupPasswordDialog() {
		// �Զ������ݵĶԻ���
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		et_password = (EditText) view.findViewById(R.id.et_password);
		et_password_confirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText()
						.toString().trim();
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(getApplicationContext(), "�Բ������벻��Ϊ��", 0)
							.show();
					return;
				}
				if (!password.equals(password_confirm)) {
					Toast.makeText(getApplicationContext(), "�������벻һ��", 0)
							.show();
					return;
				}
				Editor editor = sp.edit();
				editor.putString("password", Md5Utils.encode(password));
				editor.commit();
				dialog.dismiss();
			}
		});
		builder.setView(view);
		dialog = builder.create();
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
			View view = View.inflate(getApplicationContext(),
					R.layout.item_home, null);
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
