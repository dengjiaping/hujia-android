package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/28.
 */

public class SetPasswordActivity extends ToolBarActivity implements View.OnClickListener{
	@ViewInject(R.id.setpass_set)
	private RelativeLayout setpassSet;
	@ViewInject(R.id.setpass_state)
	private TextView setpassState;
	@ViewInject(R.id.setpass_reset)
	private TextView setpassReset;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_set_password);
		ViewInjectUtils.inject(this);
		setLeftTitle("支付密码设置");
		setpassSet.setOnClickListener(this);
		setpassReset.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.setpass_set:
				break;
			case R.id.setpass_reset:
				break;
		}
	}
}
