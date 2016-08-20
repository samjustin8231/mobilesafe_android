package com.itheima.mobilesafe.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	protected static final int SERVER_CODE_ERROR = 2001;
	protected static final int URL_ERROR = 2002;
	protected static final int NETWORK_ERROR = 2003;
	protected static final int JSON_ERROR = 2004;
	protected static final int SHOW_UPDATE_DIALOG = 2005;
	private TextView tv_splash_version;
	private TextView tv_splash_progress;
	private RelativeLayout rl_splash_root;
	private String description;
	private String path;
	// ����һ����Ϣ������
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SERVER_CODE_ERROR:
				Toast.makeText(getApplicationContext(),
						"��ȡ������Ϣʧ�ܣ������룺" + SERVER_CODE_ERROR, 1).show();
				loadMainUI();
				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(),
						"��ȡ������Ϣʧ�ܣ������룺" + URL_ERROR, 1).show();
				loadMainUI();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "�������粻���������������¹���", 1)
						.show();
				loadMainUI();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(),
						"��ȡ������Ϣʧ�ܣ������룺" + JSON_ERROR, 1).show();
				loadMainUI();
				break;
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);
		String version = getVersion();
		tv_splash_version.setText("�汾��:" + version);
		// ����һ������Ч����
		playAnimation();

		// ���ӷ�������������Ϣ
		checkVersion();

		// ��Ӧ�ó���򿪵�splash�������� ��� ���ݿ��ļ��ĳ�ʼ����
		copyDB();

	}

	/**
	 * ��ʼ����������ص����ݿ�
	 */
	private void copyDB() {
		try {
			File file = new File(getFilesDir(), "address.db");
			if (file.exists() && file.length() > 0) {
				Log.i(TAG, "���ݿ��Ѿ��������追��");
			} else {
				Log.i(TAG, "������������ص����ݿ�");
				InputStream is = getAssets().open("address.db");

				FileOutputStream fos = new FileOutputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʾ�������ѶԻ���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				loadMainUI();
			}
		});
		builder.setTitle("��������");
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �ж�sd���Ƿ����
				// ����apk���滻��װ��
				FinalHttp fh = new FinalHttp();
				// ����download������ʼ����
				HttpHandler handler = fh.download(path, Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/xx.apk", false, new AjaxCallBack<File>() {
					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
						int progress = (int) (current * 100 / count);
						tv_splash_progress.setText(("���ؽ��ȣ�" + progress + "%"));
					}

					@Override
					public void onSuccess(File t) {
						super.onSuccess(t);
						Toast.makeText(getApplicationContext(), "���سɹ����滻��װ", 0)
								.show();
						// <action android:name="android.intent.action.VIEW" />
						// <category
						// android:name="android.intent.category.DEFAULT" />
						// <data android:scheme="content" />
						// <data android:scheme="file" />
						// <data
						// android:mimeType="application/vnd.android.package-archive"
						// />
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						// intent.setType("application/vnd.android.package-archive");
						// intent.setData(Uri.fromFile(t));
						intent.setDataAndType(Uri.fromFile(t),
								"application/vnd.android.package-archive");
						startActivity(intent);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(getApplicationContext(), "����ʧ��", 0)
								.show();
						loadMainUI();
					}
				});
			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		builder.show();
	}

	/**
	 * ����������
	 */
	protected void loadMainUI() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();// �رյ�splash���档
	}

	/**
	 * ���ӷ�������������Ϣ ����Ҫ�����߳�ʵ�֡�
	 */
	private void checkVersion() {
		new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				Message msg = Message.obtain();

				// 127.0.0.1 ���ػػ���ַ�� localhost
				try {
					URL url = new URL(getResources().getString(
							R.string.serverurl)); // http: https:// ftp://
													// svn://
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");// ���ִ�Сд
					conn.setConnectTimeout(2000);// �������ӳ�ʱʱ��
					// conn.setReadTimeout(5000); ��ȡ��ʱʱ�䡣
					int code = conn.getResponseCode();// ��ȡ������״̬��
					if (code == 200) {// ����ɹ�
						InputStream is = conn.getInputStream();// json �ַ�����
						String result = StreamTools.readStream(is);
						JSONObject jsonObj = new JSONObject(result);
						String version = (String) jsonObj.get("version");
						description = (String) jsonObj.get("description");
						path = jsonObj.getString("path");
						Log.i(TAG, "result:" + result);
						Log.i(TAG, "version:" + version);
						Log.i(TAG, "description:" + description);
						Log.i(TAG, "path:" + path);
						// �ж� �������İ汾�� �� �ͻ��˵İ汾���Ƿ�һ�¡�
						if (getVersion().equals(version)) {
							Log.i(TAG, "�汾����ͬ������������");
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							loadMainUI();
						} else {
							Log.i(TAG, "�汾�Ų���ͬ�������������ѶԻ���");
							msg.what = SHOW_UPDATE_DIALOG;
						}
					} else {
						// ״̬�벻��ȷ��
						msg.what = SERVER_CODE_ERROR;
					}
				} catch (MalformedURLException e) {// ·������Э������ˣ�//�Զ���һЩ�����롣
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (NotFoundException e) {// �������� ·���Ҳ���
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) { // �����������
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) { // ����json�ļ������ˡ�
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void playAnimation() {
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(2000);
		rl_splash_root.startAnimation(aa);
	}

	/**
	 * ��ȡӦ�ó���İ汾����Ϣ
	 * 
	 * @return Ӧ�ó���İ汾��
	 */
	private String getVersion() {
		// ��ȡһ��ϵͳ�İ���������
		PackageManager pm = getPackageManager();
		try {
			// �嵥�ļ� manifest.xml�ļ������е���Ϣ
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach �����ܷ������쳣
			return "";
		}
	}
}
