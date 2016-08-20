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
	private static final String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
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
				case 0://手机防盗。
					//判断用户是否设置过密码
					String savedPassword = sp.getString("password", null);
					if(TextUtils.isEmpty(savedPassword)){
						//没有设置过密码
						Log.i(TAG,"没有设置过密码，弹出设置密码对话框");
						showSetupPasswordDialog();
					}else{
						//设置过密码
						Log.i(TAG,"设置过密码,弹出输入密码对话框");
						showEnterPasswordDialog();
					}
					break;
				}
			}
		});
	}

	/**
	 * 输入密码对话框
	 */
	protected void showEnterPasswordDialog() {
		
	}

	/**
	 * 设置密码对话框
	 */
	protected void showSetupPasswordDialog() {
		//自定义内容的对话框
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		builder.setView(view);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// Default Simple Basic Base
	private class HomeAdapter extends BaseAdapter {
		// 返回有多少个条目
		@Override
		public int getCount() {
			return names.length;
		}

		// 返回每个条目的view对象
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
