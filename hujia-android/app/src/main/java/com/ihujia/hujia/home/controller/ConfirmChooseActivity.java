package com.ihujia.hujia.home.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.adapter.PayWayAdapter;
import com.ihujia.hujia.home.utils.PayUtils;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.CommitOrderEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.PayWayEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoweiwei on 2017/1/10.
 * 选择支付方式
 */

public class ConfirmChooseActivity extends ToolBarActivity implements View.OnClickListener {

	public static final String EXTRA_COMMIT_SUCCESS_ENTITY = "commit_success_entity";
	private static final int REQUEST_NET_PAY = 10;

	@ViewInject(R.id.choose_payway_order_total)
	private TextView chooseTotal;
	@ViewInject(R.id.confirm_choose_list)
	private ListView chooseList;
	@ViewInject(R.id.confirm_choose_pay)
	private TextView choosePay;

	private CommitOrderEntity commitOrderEntity;
	private PayWayAdapter adapter;
	private int position;
	private String payChannel;
	public static String amount;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Map<String, String> result = (Map<String, String>) msg.obj;
			String resultStatus = "";
			for (String key : result.keySet()) {
				if (TextUtils.equals(key, "resultStatus")) {
					resultStatus = result.get(key);
				}
			}
			/*
			 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
			 */
			// 判断resultStatus 为9000则代表支付成功
			if (TextUtils.equals(resultStatus, "9000")) {
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				PayUtils.gotoPaySuccess(ConfirmChooseActivity.this);
				setResult(RESULT_OK);
				finish();
			} else {
				// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
				ToastUtil.shortShow(ConfirmChooseActivity.this, getString(R.string.pay_failed));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitManager.instance.addBuyNowActivity(this);
		setContentView(R.layout.confirm_choose_way);
		setLeftTitle(getString(R.string.pay_select_way));
		ViewInjectUtils.inject(this);
		choosePay.setOnClickListener(this);
		Intent intent = getIntent();
		commitOrderEntity = intent.getParcelableExtra(EXTRA_COMMIT_SUCCESS_ENTITY);
		amount = commitOrderEntity.getOrderTotal();
		chooseTotal.setText(getString(R.string.price, commitOrderEntity.getOrderTotal()));
		initList();
	}

	private void initList() {
		List<PayWayEntity> entities = new ArrayList<>();
		PayWayEntity zhifubaoEntity = new PayWayEntity(getString(R.string.pay_alipay), "1");
		PayWayEntity weixinEntity = new PayWayEntity(getString(R.string.pay_wxpay), "2");
		PayWayEntity guibinkaEntity = new PayWayEntity(getString(R.string.pay_vip), "3");
		//支付方式 1：支付宝、2：微信、3：贵宾卡
		String payType = commitOrderEntity.getPayType();
		if (!StringUtil.isEmpty(payType)) {
			String[] splitPayType = payType.split(",");
			for (int i = 0; i < splitPayType.length; i++) {
				if (splitPayType.length > 0) {
					payChannel = splitPayType[0];
				}
				if ("1".equals(splitPayType[i])) {
					entities.add(zhifubaoEntity);
				} else if ("2".equals(splitPayType[i])) {
					entities.add(weixinEntity);
				} else if ("3".equals(splitPayType[i])) {
					entities.add(guibinkaEntity);
				}
			}
		}
		if (entities.size() > 0) {
			entities.get(0).setChecked(true);
		}
		adapter = new PayWayAdapter(this, entities);
		chooseList.setAdapter(adapter);
		chooseList.setOnItemClickListener((adapterView, view, i, l) -> {
			for (PayWayEntity entity : entities) {
				entity.setChecked(false);
			}
			position = i;
			entities.get(i).setChecked(true);
			payChannel = entities.get(i).getType();
			adapter.notifyDataSetChanged();
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.confirm_choose_pay:
				AnalysisUtil.onEvent(this, "zhifu");
				pay();
				break;
		}
	}

	private void pay() {
		if (adapter != null && adapter.getCount() > position) {
			PayWayEntity payWayEntity = adapter.getItem(position);
			if (commitOrderEntity != null && payWayEntity != null) {
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("order_id", commitOrderEntity.getOrderId());
				params.put("pay_type", payWayEntity.getType());
				params.put(Constants.USERID, UserCenter.getUserId(this));
				requestHttpData(Constants.Urls.URL_POST_PAY, REQUEST_NET_PAY, FProtocol.HttpMethod.POST, params);
			}
		}
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			if ("1".equals(payChannel)) {
				PayUtils.startAliPay(this, mHandler, Parsers.getWXPrepayId(data));
			} else if ("2".equals(payChannel)) {
				PayUtils.startWXPay(this, Parsers.getWXPrepayId(data));
			}
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
