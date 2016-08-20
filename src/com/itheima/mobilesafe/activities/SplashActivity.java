package com.itheima.mobilesafe.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
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
	private TextView tv_splash_version;
	private RelativeLayout rl_splash_root;
	//����һ����Ϣ������
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SERVER_CODE_ERROR:
				Toast.makeText(getApplicationContext(), "��ȡ������Ϣʧ�ܣ������룺"+SERVER_CODE_ERROR, 1).show();
				loadMainUI();
				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "��ȡ������Ϣʧ�ܣ������룺"+URL_ERROR, 1).show();
				loadMainUI();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "�������粻���������������¹���", 1).show();
				loadMainUI();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "��ȡ������Ϣʧ�ܣ������룺"+JSON_ERROR, 1).show();
				loadMainUI();
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
        String version = getVersion();
        tv_splash_version.setText("�汾��:"+version);
        //����һ������Ч����
        playAnimation();
        
        //���ӷ�������������Ϣ
        checkVersion();
        
    }

    /**
     * ����������
     */
    protected void loadMainUI() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();//�رյ�splash���档
	}

	/**
     * ���ӷ�������������Ϣ
     * ����Ҫ�����߳�ʵ�֡�
     */
    private void checkVersion() {
    	new Thread(){
    		public void run() {
    			Message msg  = Message.obtain();
    			//127.0.0.1 ���ػػ���ַ�� localhost
    			try {
					URL url = new URL(getResources().getString(R.string.serverurl)); //http: https:// ftp:// svn://
					HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");//���ִ�Сд
					conn.setConnectTimeout(5000);//�������ӳ�ʱʱ��
					//conn.setReadTimeout(5000);  ��ȡ��ʱʱ�䡣
					int code = conn.getResponseCode();//��ȡ������״̬��
					if(code == 200){//����ɹ�
						InputStream is = conn.getInputStream();//json �ַ�����
						String result = StreamTools.readStream(is);
						JSONObject jsonObj = new JSONObject(result);
						String version = (String) jsonObj.get("version");
						String description = (String) jsonObj.get("description");
						String path = jsonObj.getString("path");
						Log.i(TAG,"result:"+result);
						Log.i(TAG,"version:"+version);
						Log.i(TAG,"description:"+description);
						Log.i(TAG,"path:"+path);
						//�ж� �������İ汾�� �� �ͻ��˵İ汾���Ƿ�һ�¡�
						if(getVersion().equals(version)){
							Log.i(TAG,"�汾����ͬ������������");
						}else{
							Log.i(TAG,"�汾�Ų���ͬ�������������ѶԻ���");
						}
						
					}else{
						//״̬�벻��ȷ��
						msg.what = SERVER_CODE_ERROR;
					}
				} catch (MalformedURLException e) {//·������Э������ˣ�//�Զ���һЩ�����롣
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (NotFoundException e) {//�������� ·���Ҳ���
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) { //�����������
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) { //����json�ļ������ˡ�
					e.printStackTrace();
					msg.what = JSON_ERROR;
				}finally{
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
     * @return Ӧ�ó���İ汾��
     */
	private String getVersion() {
		//��ȡһ��ϵͳ�İ���������
        PackageManager pm = getPackageManager();
        try {
        	//�嵥�ļ� manifest.xml�ļ������е���Ϣ
        	PackageInfo  packInfo  = pm.getPackageInfo(getPackageName(), 0);
        	String version = packInfo.versionName;
        	return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			//can't reach �����ܷ������쳣
			return "";
		}
	}
}
