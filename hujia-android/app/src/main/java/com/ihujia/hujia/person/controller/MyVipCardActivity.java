package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class MyVipCardActivity extends ToolBarActivity implements View.OnClickListener{
	@ViewInject(R.id.vipcard_banlance)
	private TextView balance;
	@ViewInject(R.id.vipcard_charge_record)
	private TextView chargeRecord;
	@ViewInject(R.id.vipcard_consume_record)
	private TextView consumeRecord;
	@ViewInject(R.id.vipcard_charge_now)
	private TextView chargeNow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_vipcard);
		ViewInjectUtils.inject(this);
		setLeftTitle("我的会员卡");
		chargeNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.vipcard_charge_record:
				break;
			case R.id.vipcard_consume_record:
				break;
			case R.id.vipcard_charge_now:
				startActivity(new Intent(this, VipcardChargeActivity.class));
				break;
		}
	}
}
