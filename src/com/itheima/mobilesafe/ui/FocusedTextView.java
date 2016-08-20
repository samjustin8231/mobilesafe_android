package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {

	public FocusedTextView(Context context) {
		super(context);
	}

	//���Լ� xml�ļ�������
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ��ƭ��ϵͳ����Ϊ�������textview�ǵõ������ˡ�
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
}
