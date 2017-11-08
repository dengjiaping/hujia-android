package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.common.view.GridViewForInner;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.network.entities.IntegralExchangeEntity;
import com.ihujia.hujia.person.adapter.IntegralExchangeAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 积分兑换弹窗
 */

public class IntegralExchangeActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.integral_exchange_num)
	private TextView integralNum;
	@ViewInject(R.id.integral_exchange_grid)
	private GridViewForInner integralGrid;
	@ViewInject(R.id.integral_exchange_exchange)
	private TextView integralExchange;
	private List<IntegralExchangeEntity> entities;
	private IntegralExchangeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_integral_exchange_act);
		ViewInjectUtils.inject(this);
		initStyle();
		initData();
		integralExchange.setOnClickListener(this);
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
		integralGrid.setAdapter(adapter);
		integralGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

	private void initStyle() {
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams wl = window.getAttributes();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		this.onWindowAttributesChanged(wl);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.actionsheet_dialog_out);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.integral_exchange_exchange:
				startActivity(new Intent(this, PaySuccessActivity.class).putExtra(EXTRA_FROM, PaySuccessActivity.FROM_INTEGRAL));
				finish();
				break;
		}
	}
}
