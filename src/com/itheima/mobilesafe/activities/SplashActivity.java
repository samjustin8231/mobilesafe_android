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
	//创建一个消息处理器
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SERVER_CODE_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+SERVER_CODE_ERROR, 1).show();
				loadMainUI();
				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+URL_ERROR, 1).show();
				loadMainUI();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "您的网络不给力啊，再试下下哈哈", 1).show();
				loadMainUI();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+JSON_ERROR, 1).show();
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
        tv_splash_version.setText("版本号:"+version);
        //播放一个动画效果。
        playAnimation();
        
        //连接服务器检查更新信息
        checkVersion();
        
    }

    /**
     * 进入主界面
     */
    protected void loadMainUI() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();//关闭掉splash界面。
	}

	/**
     * 连接服务器检查更新信息
     * 必须要在子线程实现。
     */
    private void checkVersion() {
    	new Thread(){
    		public void run() {
    			Message msg  = Message.obtain();
    			//127.0.0.1 本地回环地址。 localhost
    			try {
					URL url = new URL(getResources().getString(R.string.serverurl)); //http: https:// ftp:// svn://
					HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");//区分大小写
					conn.setConnectTimeout(5000);//设置连接超时时间
					//conn.setReadTimeout(5000);  读取超时时间。
					int code = conn.getResponseCode();//获取服务器状态码
					if(code == 200){//请求成功
						InputStream is = conn.getInputStream();//json 字符串。
						String result = StreamTools.readStream(is);
						JSONObject jsonObj = new JSONObject(result);
						String version = (String) jsonObj.get("version");
						String description = (String) jsonObj.get("description");
						String path = jsonObj.getString("path");
						Log.i(TAG,"result:"+result);
						Log.i(TAG,"version:"+version);
						Log.i(TAG,"description:"+description);
						Log.i(TAG,"path:"+path);
						//判断 服务器的版本号 和 客户端的版本号是否一致。
						if(getVersion().equals(version)){
							Log.i(TAG,"版本号相同，进入主界面");
						}else{
							Log.i(TAG,"版本号不相同，弹出更新提醒对话框");
						}
						
					}else{
						//状态码不正确。
						msg.what = SERVER_CODE_ERROR;
					}
				} catch (MalformedURLException e) {//路径错误（协议错误了）//自定义一些错误码。
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (NotFoundException e) {//域名或者 路径找不到
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) { //访问网络出错
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) { //解析json文件出错了。
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
     * 获取应用程序的版本号信息
     * @return 应用程序的版本号
     */
	private String getVersion() {
		//获取一个系统的包管理器。
        PackageManager pm = getPackageManager();
        try {
        	//清单文件 manifest.xml文件的所有的信息
        	PackageInfo  packInfo  = pm.getPackageInfo(getPackageName(), 0);
        	String version = packInfo.versionName;
        	return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			//can't reach 不可能发生的异常
			return "";
		}
	}
}
