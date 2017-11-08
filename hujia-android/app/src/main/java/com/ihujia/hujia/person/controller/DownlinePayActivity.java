package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class DownlinePayActivity extends ToolBarActivity {
	@ViewInject(R.id.downline_pay_bar_code)
	private SimpleDraweeView barCode;
	@ViewInject(R.id.downline_pay_qr_code)
	private SimpleDraweeView qrCode;
	@ViewInject(R.id.downline_pay_refresh_code)
	private TextView refreshCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_downline_pay);
		ViewInjectUtils.inject(this);
		setLeftTitle("付款");
	}
}
