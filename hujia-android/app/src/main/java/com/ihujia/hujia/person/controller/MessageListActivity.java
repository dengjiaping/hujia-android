package com.ihujia.hujia.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.MessageEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.AssetsMsgAdapter;
import com.ihujia.hujia.person.adapter.LogisticMsgAdapter;
import com.ihujia.hujia.person.adapter.SysMsgAdapter;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2017/2/25.
 * 系统消息列表
 */
public class MessageListActivity extends ToolBarActivity implements RefreshRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener {

	public static final String EXTRA_TYPE = "extra_type";
	public static final String TYPE_SYSTEM = "1";
	public static final String TYPE_LOGISTICS = "2";
	public static final String TYPE_ASSETS = "3";
	private static final int REQUEST_NET_MESSAGE_LIST = 10;
	private static final int REQUEST_NET_MESSAGE_LIST_MORE = 20;

	private RefreshRecyclerView rrvSystemMessageList;
	private String type;
	private SysMsgAdapter sysMsgAdapter;
	private LogisticMsgAdapter logisticMsgAdapter;
	private AssetsMsgAdapter assetsMsgAdapter;

	public static void startMessageListActivity(Context context, String type) {
		Intent intent = new Intent(context, MessageListActivity.class);
		intent.putExtra(EXTRA_TYPE, type);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_system_message);
		initView();
		loadData(false);
	}

	private void initView() {
		type = getIntent().getStringExtra(EXTRA_TYPE);
		switch (type) {
			case TYPE_SYSTEM:
				setLeftTitle(getString(R.string.message_system));
				break;
			case TYPE_LOGISTICS:
				setLeftTitle(getString(R.string.message_logistics));
				break;
			case TYPE_ASSETS:
				setLeftTitle(getString(R.string.message_assets));
				break;
		}

		rrvSystemMessageList = (RefreshRecyclerView) findViewById(R.id.rrv_system_message_list);
		rrvSystemMessageList.setHasFixedSize(true);
		rrvSystemMessageList.setMode(RefreshRecyclerView.Mode.BOTH);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rrvSystemMessageList.setLayoutManager(layoutManager);
		rrvSystemMessageList.setOnRefreshAndLoadMoreListener(this);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int requestCode = REQUEST_NET_MESSAGE_LIST;
		if (isMore) {
			requestCode = REQUEST_NET_MESSAGE_LIST_MORE;
			switch (type) {
				case TYPE_SYSTEM:
					page = sysMsgAdapter.getPage() + 1;
					break;
				case TYPE_LOGISTICS:
					page = logisticMsgAdapter.getPage() + 1;
					break;
				case TYPE_ASSETS:
					page = assetsMsgAdapter.getPage() + 1;
					break;
			}
		}
		params.put("mess_type", type);
		params.put(Constants.USERID, UserCenter.getUserId(this));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_20);
		requestHttpData(Constants.Urls.URL_POST_MESSAGE_LIST, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		rrvSystemMessageList.resetStatus();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			if (REQUEST_NET_MESSAGE_LIST == requestCode) {
				PagesEntity<MessageEntity> message = Parsers.getMessage(data);
				switch (type) {
					case TYPE_SYSTEM:{
						sysMsgAdapter = new SysMsgAdapter(message.getDatas(), this);
						rrvSystemMessageList.setAdapter(sysMsgAdapter);
						break;
					}
					case TYPE_LOGISTICS:{
						logisticMsgAdapter = new LogisticMsgAdapter(message.getDatas(), this);
						rrvSystemMessageList.setAdapter(logisticMsgAdapter);
						break;
					}
					case TYPE_ASSETS:{
						assetsMsgAdapter = new AssetsMsgAdapter(message.getDatas());
						rrvSystemMessageList.setAdapter(assetsMsgAdapter);
						break;
					}
				}
				if (message.getTotalPage() > 1) {
					rrvSystemMessageList.setCanAddMore(true);
				} else {
					rrvSystemMessageList.setCanAddMore(false);
				}
			} else if (REQUEST_NET_MESSAGE_LIST_MORE == requestCode) {
				PagesEntity<MessageEntity> message = Parsers.getMessage(data);
				switch (type) {
					case TYPE_SYSTEM:{
						sysMsgAdapter.addDatas(message.getDatas());
						if (message.getTotalPage() <= sysMsgAdapter.getPage()) {
							rrvSystemMessageList.setCanAddMore(false);
						}
						break;
					}
					case TYPE_LOGISTICS:{
						logisticMsgAdapter.addDatas(message.getDatas());
						if (message.getTotalPage() <= logisticMsgAdapter.getPage()) {
							rrvSystemMessageList.setCanAddMore(false);
						}
						break;
					}
					case TYPE_ASSETS:{
						assetsMsgAdapter.addDatas(message.getDatas());
						if (message.getTotalPage() <= assetsMsgAdapter.getPage()) {
							rrvSystemMessageList.setCanAddMore(false);
						}
						break;
					}
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvSystemMessageList.resetStatus();
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onRefresh() {
		loadData(false);
	}

	@Override
	public void onLoadMore() {
		loadData(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.cv_sys_msg_detail:{
				MessageEntity messageEntity = (MessageEntity) v.getTag();
				if (messageEntity != null) {
					SysMsgDetailActivity.startSysMsgDetailActivity(this, messageEntity);
				}
				break;
			}
			case R.id.cv_logistic_msg_detail:{
				MessageEntity messageEntity = (MessageEntity) v.getTag();
				if (messageEntity != null) {
					LogisticsDetailActivity.startLogisticsDetailActivity(this, messageEntity.getOrderNo());
				}
				break;
			}
		}
	}
}
