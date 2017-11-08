package com.ihujia.hujia.person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.ihujia.hujia.person.controller.IntegralFragment;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class IntegralPageAdapter extends FragmentPagerAdapter {
	List<IntegralFragment> fragments;
	public IntegralPageAdapter(FragmentManager fm, List<IntegralFragment> fragments) {
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
