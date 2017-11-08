package com.ihujia.hujia.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.network.entities.MessageEntity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/2/25.
 * 系统消息详情
 */
public class SysMsgDetailActivity extends ToolBarActivity {

	private static final String EXTRA_MESSAGE = "extra_message";

	@ViewInject(R.id.tv_msg_detail_title)
	private TextView tvMsgDetailTitle;
	@ViewInject(R.id.tv_msg_detail_date)
	private TextView tvMsgDetailDate;
	@ViewInject(R.id.tv_msg_detail_content)
	private TextView tvMsgDetailContent;

	public static void startSysMsgDetailActivity(Context context, MessageEntity message) {
		Intent intent = new Intent(context, SysMsgDetailActivity.class);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sys_msg_detail);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.message_detail));
		MessageEntity messageEntity = getIntent().getParcelableExtra(EXTRA_MESSAGE);
		if (messageEntity != null) {
			tvMsgDetailTitle.setText(messageEntity.getName());
			tvMsgDetailDate.setText(messageEntity.getDate());
			tvMsgDetailContent.setText(messageEntity.getContent());
		}
	}
}
