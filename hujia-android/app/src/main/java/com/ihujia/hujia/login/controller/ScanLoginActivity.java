package com.ihujia.hujia.login.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2017/1/13.
 * 扫码登录
 */
public class ScanLoginActivity extends ToolBarActivity implements View.OnClickListener {

	private static final String EXTRA_URL = "extra_url";

	@ViewInject(R.id.scan_login_confirm)
	private View scanLoginConfirm;
	@ViewInject(R.id.scan_login_cancel)
	private View scanLoginCancel;

	private String url;

	public static void startScanLoginActivity(Context context, String url) {
		Intent intent = new Intent(context, ScanLoginActivity.class);
		intent.putExtra(EXTRA_URL, url);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_scan_login);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.login_scan));
		url = getIntent().getStringExtra(EXTRA_URL);
		scanLoginConfirm.setOnClickListener(this);
		scanLoginCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.scan_login_confirm:
				if (!TextUtils.isEmpty(url)) {
					requestHttpData(url + "&loginName=" + UserCenter.getUserLoginName(this), 1);
					finish();
				} else {
					ToastUtil.shortShow(this, getString(R.string.login_qr_code_error));
				}
				break;
			case R.id.scan_login_cancel:
				finish();
				break;
		}
	}
}
