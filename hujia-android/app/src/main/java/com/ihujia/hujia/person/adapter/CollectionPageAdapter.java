package com.ihujia.hujia.person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ihujia.hujia.base.BaseFragment;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/24.
 */

public class CollectionPageAdapter extends FragmentPagerAdapter {
	private List<BaseFragment> fragments;
	public CollectionPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
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
