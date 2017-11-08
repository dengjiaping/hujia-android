package com.ihujia.hujia.home.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.ihujia.hujia.widget.PasswordInputView;

/**
 * Created by zhaoweiwei on 2017/1/11.
 */

public class VipCardPayDialog extends BaseActivity implements View.OnClickListener{
	@ViewInject(R.id.vippay_close)
	private ImageView vippayClose;
	@ViewInject(R.id.vippay_title)
	private TextView vippayTitle;
	@ViewInject(R.id.vippay_product_name)
	private TextView vippayProductName;
	@ViewInject(R.id.vippay_product_price)
	private TextView vippayPrice;
	@ViewInject(R.id.vippay_not_enouth)
	private TextView vippayNotEnough;
	@ViewInject(R.id.vippay_balance)
	private TextView vippayBalance;
	@ViewInject(R.id.vippay_password)
	private PasswordInputView vippayPassword;
	@ViewInject(R.id.vippay_charge)
	private TextView vippayCharge;
	@ViewInject(R.id.vippay_cancel)
	private TextView vippayCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_card_pay);
		ViewInjectUtils.inject(this);
		vippayClose.setOnClickListener(this);
		vippayCharge.setOnClickListener(this);
		vippayCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.vippay_close:
				finish();
				break;
			case R.id.vippay_charge:
				showAlertDialog("支付失败", "支付遇到问题，您可以到我的订单页面查看订单详情", "知道了", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						closeDialog();
					}
				});
				break;
			case R.id.vippay_cancel:
				// TODO: 2017/1/15 忘记支付密码  或者是取消
				break;
		}
	}
}
