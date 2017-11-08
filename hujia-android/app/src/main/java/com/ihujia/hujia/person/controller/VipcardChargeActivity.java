package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.common.view.GridViewForInner;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.network.entities.IntegralExchangeEntity;
import com.ihujia.hujia.person.adapter.IntegralExchangeAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/27.
 */

public class VipcardChargeActivity extends ToolBarActivity implements View.OnClickListener{
	@ViewInject(R.id.vipcard_charge_grid)
	private GridViewForInner vipcardChargeGrid;
	@ViewInject(R.id.pay_way_zhifubao)
	private View payZhifubao;
	@ViewInject(R.id.pay_way_weixin)
	private View payWeixin;
	@ViewInject(R.id.vipcard_charge_pay)
	private TextView payNow;

	private List<IntegralExchangeEntity> entities;
	private IntegralExchangeAdapter adapter;
	private RadioButton zhifubaoButton;
	private RadioButton weixinButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_vipcard_recharge);
		setLeftTitle("贵宾卡充值");
		ViewInjectUtils.inject(this);
		ImageView zhifubaoLogo = (ImageView) payZhifubao.findViewById(R.id.pay_way_logo);
		TextView zhifubaoText = (TextView) payZhifubao.findViewById(R.id.pay_way_text);
		zhifubaoButton = (RadioButton) payZhifubao.findViewById(R.id.pay_way_button);
		ImageView weixinLogo = (ImageView) payWeixin.findViewById(R.id.pay_way_logo);
		TextView weixinText = (TextView) payWeixin.findViewById(R.id.pay_way_text);
		weixinButton = (RadioButton) payWeixin.findViewById(R.id.pay_way_button);

		zhifubaoLogo.setImageResource(R.drawable.pay_zhifubao);
		weixinLogo.setImageResource(R.drawable.pay_wechart);
		zhifubaoText.setText("支付宝支付");
		weixinText.setText("微信支付");
		zhifubaoButton.setChecked(true);

		payZhifubao.setOnClickListener(this);
		payWeixin.setOnClickListener(this);
		payNow.setOnClickListener(this);

		initData();
	}

	private void initData() {
		entities = new ArrayList<>();
		IntegralExchangeEntity entity0 = new IntegralExchangeEntity("10", "1000", false);
		IntegralExchangeEntity entity1 = new IntegralExchangeEntity("20", "2000", false);
		IntegralExchangeEntity entity2 = new IntegralExchangeEntity("30", "3000", false);
		IntegralExchangeEntity entity3 = new IntegralExchangeEntity("50", "5000", false);
		IntegralExchangeEntity entity4 = new IntegralExchangeEntity("100", "10000", false);
		IntegralExchangeEntity entity5 = new IntegralExchangeEntity("200", "20000", true);
		entities.add(entity0);
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		adapter = new IntegralExchangeAdapter(this, entities);
		vipcardChargeGrid.setAdapter(adapter);
		vipcardChargeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				IntegralExchangeEntity integralExchangeEntity = entities.get(i);
				for (IntegralExchangeEntity entity : entities) {
					entity.setChecked(false);
				}
				integralExchangeEntity.setChecked(true);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pay_way_zhifubao:
				if (!zhifubaoButton.isChecked()) {
					zhifubaoButton.setChecked(true);
					weixinButton.setChecked(false);
				}
				break;
			case R.id.pay_way_weixin:
				if (!weixinButton.isChecked()) {
					zhifubaoButton.setChecked(false);
					weixinButton.setChecked(true);
				}
				break;
			case R.id.vipcard_charge_pay:
				break;
		}
	}
}
