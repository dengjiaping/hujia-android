package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.CouponPageAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/23.
 * 我的优惠券
 */

public class MyCouponActivity extends ToolBarActivity implements View.OnClickListener {

	public static final int COUPONTYPE_UNUSED = 1;
	public static final int COUPONTYPE_USED = 2;
	public static final int COUPONRTYPE_OVERDUE = 3;
	private static final int REQUEST_NET_ADD_COUPON = 10;

	@ViewInject(R.id.coupon_radiogroup)
	private RadioGroup couponGroup;
	@ViewInject(R.id.coupon_unused)
	private RadioButton couponUnused;
	@ViewInject(R.id.coupon_used)
	private RadioButton couponUsed;
	@ViewInject(R.id.coupon_overdue)
	private RadioButton couponOverdue;
	@ViewInject(R.id.coupon_viewpager)
	private ViewPager couponViewPager;

	private int extraFrom;
	private MyCouponFragment unUsedFrag;
	private int currentPage;
	private CouponPageAdapter couponPageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_coupon);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_my_coupon));
		extraFrom = getIntent().getIntExtra(EXTRA_FROM, 0);
		setRightTitle(R.drawable.add_icon);
		mBtnTitleRight.setOnClickListener(this);
		initFragment();
	}

	private void initFragment() {
		unUsedFrag = new MyCouponFragment();
		unUsedFrag.setArgs(COUPONTYPE_UNUSED, extraFrom, couponUnused, couponUsed, couponOverdue);
		MyCouponFragment usedFrag = new MyCouponFragment();
		usedFrag.setArgs(COUPONTYPE_USED, extraFrom, couponUnused, couponUsed, couponOverdue);
		MyCouponFragment overDueFrag = new MyCouponFragment();
		overDueFrag.setArgs(COUPONRTYPE_OVERDUE, extraFrom, couponUnused, couponUsed, couponOverdue);

		List<MyCouponFragment> fragments = new ArrayList<>();
		fragments.add(unUsedFrag);
		fragments.add(usedFrag);
		fragments.add(overDueFrag);

		couponPageAdapter = new CouponPageAdapter(getSupportFragmentManager(), fragments);
		couponViewPager.setAdapter(couponPageAdapter);
		couponViewPager.setOnPageChangeListener(new MyCouponActivity.MyPageChangeListener());
		couponGroup.setOnCheckedChangeListener(new MyCouponActivity.MyGroupCheckChanged());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.right_button:
				showAlertDialog(getString(R.string.coupon_add), getString(R.string.coupon_input_code), getString(R.string.button_cancel), getString(R.string.button_ok), v -> {
					closeDialog();
				}, (v, editText) -> {
					String couponNum = editText.getText().toString().trim();
					if (!TextUtils.isEmpty(couponNum)) {
						closeDialog();
						addCoupon(couponNum);
					} else {
						ToastUtil.shortShow(MyCouponActivity.this, getString(R.string.coupon_code_not_empty));
					}
				});
				break;
		}
	}

	private void addCoupon(String coupon) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("coupon_code", coupon);
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_ADD_COUPON, REQUEST_NET_ADD_COUPON, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			ToastUtil.shortShow(this, getString(R.string.coupon_add_success));
			if (unUsedFrag != null && couponPageAdapter != null && currentPage != couponPageAdapter.getCount() - 1) {
				unUsedFrag.refresh();
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

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			currentPage = position;
			switch (position) {
				case 0:
					couponGroup.check(R.id.coupon_unused);
					break;
				case 1:
					couponGroup.check(R.id.coupon_used);
					break;
				case 2:
					couponGroup.check(R.id.coupon_overdue);
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
				case R.id.coupon_unused:
					couponViewPager.setCurrentItem(0);
					break;
				case R.id.coupon_used:
					couponViewPager.setCurrentItem(1);
					break;
				case R.id.coupon_overdue:
					couponViewPager.setCurrentItem(2);
					break;
			}
		}
	}
}
