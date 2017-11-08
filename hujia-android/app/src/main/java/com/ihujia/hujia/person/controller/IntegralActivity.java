package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.person.adapter.IntegralPageAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaoweiwei on 2016/12/22.
 * 我的积分
 */

public class IntegralActivity extends ToolBarActivity implements View.OnClickListener{

	public static final int INTEGRALTYPE_ALL = 0;
	public static final int INTEGRALTYPE_INCOM = 1;
	public static final int INTEGRALRTYPE_COST = 2;

	@ViewInject(R.id.integral_view_pager)
	private ViewPager viewPager;
	@ViewInject(R.id.integral_radiogroup)
	private RadioGroup integralGroup;
	@ViewInject(R.id.integral_exchange_now)
	private TextView exchangeNow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_integral);
		ViewInjectUtils.inject(this);
		setLeftTitle("我的积分");
		initFragment();
		exchangeNow.setOnClickListener(this);
	}

	private void initFragment() {
		IntegralFragment allIntegralFrag = new IntegralFragment();
		allIntegralFrag.setArgs(INTEGRALTYPE_ALL);
		IntegralFragment incomeIntegralFrag = new IntegralFragment();
		incomeIntegralFrag.setArgs(INTEGRALTYPE_INCOM);
		IntegralFragment costIntegralFrag = new IntegralFragment();
		costIntegralFrag.setArgs(INTEGRALRTYPE_COST);

		List<IntegralFragment> fragments = new ArrayList<>();
		fragments.add(allIntegralFrag);
		fragments.add(incomeIntegralFrag);
		fragments.add(costIntegralFrag);

		IntegralPageAdapter integralPageAdapter = new IntegralPageAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(integralPageAdapter);
		viewPager.setOnPageChangeListener(new IntegralActivity.MyPageChangeListener());
		integralGroup.setOnCheckedChangeListener(new IntegralActivity.MyGroupCheckChanged());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.integral_exchange_now:
				startActivity(new Intent(this,IntegralExchangeActivity.class));
				break;
		}
	}

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			switch (position) {
				case 0:
					integralGroup.check(R.id.integral_all);
					break;
				case 1:
					integralGroup.check(R.id.integral_income);
					break;
				case 2:
					integralGroup.check(R.id.integral_cost);
					break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	private class MyGroupCheckChanged implements RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			switch (i) {
				case R.id.integral_all:
					viewPager.setCurrentItem(0);
					break;
				case R.id.integral_income:
					viewPager.setCurrentItem(1);
					break;
				case R.id.integral_cost:
					viewPager.setCurrentItem(2);
					break;
			}
		}
	}
}
