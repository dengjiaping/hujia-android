package com.ihujia.hujia.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2017/2/16.
 * 意见反馈
 */
public class FeedBackActivity extends ToolBarActivity {

	private static final int REQUEST_NET_FEED_BACK = 10;

	@ViewInject(R.id.feedback_input)
	private EditText feedbackInput;
	@ViewInject(R.id.feedback_submit)
	private View feedbackSubmit;

	public static void startFeedBackActivity(Context context) {
		context.startActivity(new Intent(context, FeedBackActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_feed_back);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_setting_feedback));
		feedbackSubmit.setOnClickListener(v -> {
			String content = feedbackInput.getText().toString().trim();
			if (StringUtil.isEmpty(content)) {
				ToastUtil.shortShow(this, getString(R.string.setting_input_suggestion));
				return;
			}
			showProgressDialog();
			IdentityHashMap<String, String> params = new IdentityHashMap<>();
			params.put("user_id", UserCenter.getUserId(this));
			params.put("content", content);
			requestHttpData(Constants.Urls.URL_POST_FEED_BACK, REQUEST_NET_FEED_BACK, FProtocol.HttpMethod.POST, params);
		});
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_FEED_BACK == requestCode && REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			ToastUtil.shortShow(this, getString(R.string.setting_submit_success));
			finish();
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}
}
