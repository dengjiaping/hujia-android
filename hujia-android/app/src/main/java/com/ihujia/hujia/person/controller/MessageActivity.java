package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/30.
 * 消息
 */
public class MessageActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.rl_system_message_layout)
	private RelativeLayout rlSystemMessageLayout;
	@ViewInject(R.id.rl_logistics_message_layout)
	private RelativeLayout rlLogisticsMessageLayout;
	@ViewInject(R.id.rl_assets_message_layout)
	private RelativeLayout rlAssetsMessageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_message);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.message_title));
		rlSystemMessageLayout.setOnClickListener(this);
		rlLogisticsMessageLayout.setOnClickListener(this);
		rlAssetsMessageLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rl_system_message_layout:
				MessageListActivity.startMessageListActivity(this, MessageListActivity.TYPE_SYSTEM);
				break;
			case R.id.rl_logistics_message_layout:
				MessageListActivity.startMessageListActivity(this, MessageListActivity.TYPE_LOGISTICS);
				break;
			case R.id.rl_assets_message_layout:
				MessageListActivity.startMessageListActivity(this, MessageListActivity.TYPE_ASSETS);
				break;
		}
	}
}
