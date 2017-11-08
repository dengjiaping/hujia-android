package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.utils.VersionInfoUtils;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.base.WebViewActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2017/3/3.
 * 关于我们
 */

public class AboutUsActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.about_us_version)
	private TextView aboutUsVersion;
	@ViewInject(R.id.about_us_product_introduce)
	private TextView productIntroduce;
	@ViewInject(R.id.about_us_user_protocal)
	private TextView userProtocal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_about_us);

		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_about_us));

		String versionName = VersionInfoUtils.getVersion(this);
		aboutUsVersion.setText(getString(R.string.person_about_us_version, versionName));
		productIntroduce.setOnClickListener(this);
		userProtocal.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.about_us_product_introduce:
				toWebView(getString(R.string.person_about_us_product_introduce), Constants.Urls.URL_BASE_DOMAIN + "/h5/aboutUs.html");
				break;
			case R.id.about_us_user_protocal:
				toWebView(getString(R.string.person_about_us_user_protocal), Constants.Urls.URL_BASE_DOMAIN + "/h5/agreement.html");
				break;
		}
	}

	private void toWebView(String title, String url) {
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.EXTRA_TITLE, title);
		intent.putExtra(WebViewActivity.EXTRA_URL, url);
		startActivity(intent);
	}
}
