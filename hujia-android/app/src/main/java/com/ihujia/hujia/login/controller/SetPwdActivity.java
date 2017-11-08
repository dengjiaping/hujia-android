package com.ihujia.hujia.login.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2016/12/19.
 * 设置支付密码
 */
public class SetPwdActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.tv_set_pwd_hint)
	private TextView tvSetPwdHint;
	@ViewInject(R.id.piv_set_pwd_input)
	private EditText pivSetPwdInput;
	@ViewInject(R.id.tv_set_pwd_confirm)
	private View tvSetPwdConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_set_pwd);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle("设置支付密码");
		tvSetPwdConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_set_pwd_confirm:
				ToastUtil.shortShow(this, "设置支付密码成功");
				finish();
				break;
		}
	}
}
