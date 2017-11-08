package com.ihujia.hujia.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.LogisticEntity;
import com.ihujia.hujia.network.entities.LogisticPageEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.LogisticDetailAdapter;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/12.
 * 物流详情
 */
public class LogisticsDetailActivity extends ToolBarActivity {

	private static final int REQUEST_NET_LOGISTICS_DETAIL = 10;
	private static final String EXTRA_ORDER_ID = "extra_order_id";

	private String orderId;
	private TextView logisticNumber;
	private TextView logisticName;
	private TextView logisticPhone;
	private ListView logisticList;

	public static void startLogisticsDetailActivity(Context context, String orderId) {
		Intent intent = new Intent(context, LogisticsDetailActivity.class);
		intent.putExtra(EXTRA_ORDER_ID, orderId);
		context.startActivity(intent);
	}

	public static void startLogisticsDetailActivity(Context context, String orderId,int flag) {
		Intent intent = new Intent(context, LogisticsDetailActivity.class);
		intent.putExtra(EXTRA_ORDER_ID, orderId);
		intent.addFlags(flag);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_logistic_detail);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle("物流详情");
		orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);

		logisticList = (ListView) findViewById(R.id.logistic_detail_list);

		View topView = getLayoutInflater().inflate(R.layout.logistic_detail_top, null);
		logisticNumber = (TextView) topView.findViewById(R.id.logistic_detail_number);
		logisticName = (TextView) topView.findViewById(R.id.logistic_detail_name);
		logisticPhone = (TextView) topView.findViewById(R.id.logistic_detail_phone);
		logisticList.addHeaderView(topView);
	}

	private void loadData() {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("order_id", orderId);
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_LOGISTICS_DETAIL, REQUEST_NET_LOGISTICS_DETAIL, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		LogisticPageEntity<LogisticEntity> pageEntity = Parsers.getLogistics(data);
		if (pageEntity != null && REQUEST_NET_SUCCESS.equals(pageEntity.getResultCode())) {
			List<LogisticEntity> entities = pageEntity.getLogisticEntities();
			if (entities != null && entities.size() > 0) {
				LogisticDetailAdapter adapter = new LogisticDetailAdapter(this, entities);
				logisticList.setAdapter(adapter);
			}

			logisticNumber.setText(getString(R.string.logistic_number, pageEntity.getLogisticNumber()));
			logisticName.setText(getString(R.string.logistic_name, pageEntity.getLogisticName()));
			if (!StringUtil.isEmpty(pageEntity.getLogisticPhone())) {
				logisticPhone.setText(getString(R.string.logistic_phone, pageEntity.getLogisticPhone()));
			} else {
				logisticPhone.setText(getString(R.string.logistic_phone, getString(R.string.no_data_now)));
			}
		} else {
			ToastUtil.shortShow(this, pageEntity.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}
}
