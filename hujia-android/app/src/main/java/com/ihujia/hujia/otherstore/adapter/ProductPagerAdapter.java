package com.ihujia.hujia.otherstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/10.
 * 商品页adapter
 */
public class ProductPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private String [] titles;

	public ProductPagerAdapter(FragmentManager fm, List<Fragment> fragments, String ... titles) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
