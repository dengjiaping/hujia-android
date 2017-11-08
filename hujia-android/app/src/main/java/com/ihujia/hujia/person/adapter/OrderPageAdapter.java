package com.ihujia.hujia.person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.ihujia.hujia.person.controller.OrderListFragment;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 */

public class OrderPageAdapter extends FragmentPagerAdapter {
	private List<OrderListFragment> fragments;
	public OrderPageAdapter(FragmentManager fm, List<OrderListFragment> fragments) {
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
