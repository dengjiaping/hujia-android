package com.ihujia.hujia.person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.LinearLayout;

import com.ihujia.hujia.person.controller.MyCouponFragment;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/23.
 */

public class CouponPageAdapter extends FragmentPagerAdapter {
	private List<MyCouponFragment> fragments;
	public CouponPageAdapter(FragmentManager fm, List<MyCouponFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
