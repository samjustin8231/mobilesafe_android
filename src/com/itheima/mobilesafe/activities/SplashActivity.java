package com.itheima.mobilesafe.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class SplashActivity extends Activity {
	private TextView tv_splash_version;
	private RelativeLayout rl_splash_root;

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
