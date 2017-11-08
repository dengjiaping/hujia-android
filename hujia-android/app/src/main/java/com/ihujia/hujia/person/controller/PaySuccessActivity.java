package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/27.
 * 支付成功
 */
public class PaySuccessActivity extends ToolBarActivity implements View.OnClickListener {

	public static final String FROM_INTEGRAL = "from_integral";
	public static final String FROM_PAY = "from_pay";
	public static final String EXTRA_MONEY = "extra_money";

	@ViewInject(R.id.pay_success_describe)
	private TextView describe;
	@ViewInject(R.id.pay_success_moneyinfo)
	private TextView moneyInfo;
	@ViewInject(R.id.pay_success_money)
	private TextView montyNum;
	@ViewInject(R.id.pay_success_integtalinfo)
	private TextView integralInfo;
	@ViewInject(R.id.pay_success_integral)
	private TextView integralNum;
	@ViewInject(R.id.pay_success_paylayout)
	private LinearLayout payLayout;
	@ViewInject(R.id.pay_success_home)
	private TextView toHome;
	@ViewInject(R.id.pay_success_order)
	private TextView toOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_success);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		String from = getIntent().getStringExtra(EXTRA_FROM);
		String money = getIntent().getStringExtra(EXTRA_MONEY);
		if (FROM_INTEGRAL.equals(from)) {
			setLeftTitle(getString(R.string.exchange_success));
			describe.setText(getString(R.string.exchange_success));
			moneyInfo.setText(getString(R.string.exchange_amount));
			payLayout.setVisibility(View.GONE);
		} else if (FROM_PAY.equals(from)) {
			setLeftTitle(getString(R.string.pay_success));
			describe.setText(getString(R.string.pay_success));
			moneyInfo.setText(getString(R.string.pay_amount));
			integralInfo.setVisibility(View.GONE);
			integralNum.setVisibility(View.GONE);
		}
		montyNum.setText(getString(R.string.product_price, money));
		toHome.setOnClickListener(this);
		toOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pay_success_home:
				toMainActivity(0);
				break;
			case R.id.pay_success_order:
				startActivity(new Intent(this, OrderListActivity.class));
				finish();
				break;
		}
	}

	private void toMainActivity(int which) {
		Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_WHICH_TAB, which);
		startActivity(intent);
	}

	@Override
	public void finish() {
		ExitManager.instance.closeBuyNowActivity();
		super.finish();
	}
}
