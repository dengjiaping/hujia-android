package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.person.adapter.OrderPageAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单
 */

public class OrderListActivity extends ToolBarActivity {
	public static final int ORDERTYPE_ALL = 10;
	public static final int ORDERTYPE_PAYING = 1;
	public static final int ORDERTYPE_DELIVER = 2;
	public static final int ORDERTYPE_GETING = 3;
	public static final int ORDERTYPE_FINISH = 4;
	public static final int ORDERTYPE_CLOSE = 9;//已关闭
	public static final String EXTRA_ORDERTYPE = "extra_ordertype";

	@ViewInject(R.id.order_radiogroup)
	private RadioGroup orderGroup;
	@ViewInject(R.id.order_viewpager)
	private ViewPager viewPager;

	private int state = ORDERTYPE_PAYING;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_order_list);
		ViewInjectUtils.inject(this);
		state = getIntent().getIntExtra(EXTRA_ORDERTYPE, ORDERTYPE_ALL);
		setLeftTitle(getString(R.string.person_order));
		initFragment();
		showState();
	}

	private void showState() {
		switch (state) {
			case ORDERTYPE_ALL:
				viewPager.setCurrentItem(0);
				orderGroup.check(R.id.order_all);
				break;
			case ORDERTYPE_PAYING:
				viewPager.setCurrentItem(1);
				orderGroup.check(R.id.order_paying);
				break;
			case ORDERTYPE_DELIVER:
				viewPager.setCurrentItem(2);
				orderGroup.check(R.id.order_deliver);
				break;
			case ORDERTYPE_GETING:
				viewPager.setCurrentItem(3);
				orderGroup.check(R.id.order_getting);
				break;
		}
	}

	private void initFragment() {
		OrderListFragment allOrderFrag = new OrderListFragment();
		allOrderFrag.setArgs(ORDERTYPE_ALL);
		OrderListFragment payingOrderFrag = new OrderListFragment();
		payingOrderFrag.setArgs(ORDERTYPE_PAYING);
		OrderListFragment deliverOrderFrag = new OrderListFragment();
		deliverOrderFrag.setArgs(ORDERTYPE_DELIVER);
		OrderListFragment gettingOrderFrag = new OrderListFragment();
		gettingOrderFrag.setArgs(ORDERTYPE_GETING);

		List<OrderListFragment> fragments = new ArrayList<>();
		fragments.add(allOrderFrag);
		fragments.add(payingOrderFrag);
		fragments.add(deliverOrderFrag);
		fragments.add(gettingOrderFrag);

		OrderPageAdapter orderPageAdapter = new OrderPageAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(orderPageAdapter);
		viewPager.addOnPageChangeListener(new MyPageChangeListener());
		orderGroup.setOnCheckedChangeListener(new MyGroupCheckChanged());
	}

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			switch (position) {
				case 0:
					orderGroup.check(R.id.order_all);
					break;
				case 1:
					orderGroup.check(R.id.order_paying);
					break;
				case 2:
					orderGroup.check(R.id.order_deliver);
					break;
				case 3:
					orderGroup.check(R.id.order_getting);
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
				case R.id.order_all:
					viewPager.setCurrentItem(0);
					break;
				case R.id.order_paying:
					viewPager.setCurrentItem(1);
					break;
				case R.id.order_deliver:
					viewPager.setCurrentItem(2);
					break;
				case R.id.order_getting:
					viewPager.setCurrentItem(3);
					break;
			}
		}
	}
}
