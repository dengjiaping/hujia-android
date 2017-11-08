package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.ViewInjectUtils;


/**
 * Created by zhaoweiwei on 2016/12/28.
 * 我的购物车
 */

public class MyShopCarActivity extends ToolBarActivity {
	@ViewInject(R.id.shopcar_framelayout)
	private FrameLayout frameLayout;

	private FragmentTransaction transaction;
	private ShopCarFragment shopCarFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitManager.instance.addBuyNowActivity(this);
		setContentView(R.layout.person_act_shopcar);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle("我的购物车");
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.shopcar_framelayout, new ShopCarFragment());
		transaction.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shopCarFragment = (ShopCarFragment) getSupportFragmentManager().findFragmentById(R.id.shopcar_framelayout);
		RelativeLayout title = (RelativeLayout) shopCarFragment.getView().findViewById(R.id.shopcar_title);
		title.setVisibility(View.GONE);
	}
}
