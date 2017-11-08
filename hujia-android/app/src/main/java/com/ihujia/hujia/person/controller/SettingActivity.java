package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.utils.DataCleanManager;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 设置
 */

public class SettingActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.person_setting_clean_cache)
	private TextView cleanCache;
	@ViewInject(R.id.person_setting_feedback)
	private TextView feedback;
	@ViewInject(R.id.person_setting_check_update)
	private TextView checkUpdate;
	@ViewInject(R.id.person_setting_logout)
	private TextView loginout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_setting);
		setLeftTitle(getString(R.string.person_setting_title));
		ViewInjectUtils.inject(this);
		cleanCache.setOnClickListener(this);
		feedback.setOnClickListener(this);
		checkUpdate.setOnClickListener(this);
		if (UserCenter.isLogin(this)) {
			loginout.setVisibility(View.VISIBLE);
			loginout.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.person_setting_clean_cache:
				cleanCache();
				break;
			case R.id.person_setting_feedback:
				FeedBackActivity.startFeedBackActivity(this);
				break;
			case R.id.person_setting_check_update:
				showProgressDialog();
				checkUpdateVersion();
				break;
			case R.id.person_setting_logout:
				UserCenter.cleanUserInfo(this);
				loginout.setVisibility(View.GONE);
				ToastUtil.shortShow(this, getString(R.string.setting_logout_hint));
				break;
		}
	}

	private void cleanCache() {
		String cache = null;
		try {
			cache = DataCleanManager.getTotalCacheSize(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = String.format(getString(R.string.person_setting_clear_cache), cache);
		showAlertDialog(getString(R.string.setting_hint_text), message, getString(R.string.button_cancel), getString(R.string.button_ok), v -> closeDialog(), v -> {
			DataCleanManager.clearAllCache(SettingActivity.this);
			closeDialog();
		});
	}
}
