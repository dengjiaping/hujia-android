package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.person.adapter.CollectionPageAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/24.
 * 我的收藏(包括两个tab，商品和店铺)
 */

public class CollectionActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.collection_back)
	private ImageView collectionBack;
	@ViewInject(R.id.collection_group)
	private RadioGroup collectionGroup;
	@ViewInject(R.id.collection_edit)
	public TextView collectionEdit;
	@ViewInject(R.id.collection_viewpager)
	private ViewPager collectionViewpager;

	private boolean isEdit;//为true的时候是编辑状态，编辑按钮显示“完成”，商品可删除
	private CollectionProductFrag productFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_collection);
		ViewInjectUtils.inject(this);

		initFragments();

		changeOperate();

		collectionEdit.setOnClickListener(this);
		collectionBack.setOnClickListener(this);
	}

	private void changeOperate() {
		collectionViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 0:
						collectionGroup.check(R.id.collection_product);
						collectionEdit.setVisibility(View.VISIBLE);
						break;
					case 1:
						collectionGroup.check(R.id.collection_store);
						collectionEdit.setVisibility(View.GONE);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		collectionGroup.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.collection_product:
					collectionViewpager.setCurrentItem(0);
					collectionEdit.setVisibility(View.VISIBLE);
					break;
				case R.id.collection_store:
					collectionViewpager.setCurrentItem(1);
					collectionEdit.setVisibility(View.GONE);
					break;
			}
		});
	}

	private void initFragments() {
		List<BaseFragment> fragments = new ArrayList<>();
		productFrag = new CollectionProductFrag();
		CollectionStoreFrag storeFrag = new CollectionStoreFrag();
		fragments.add(productFrag);
		fragments.add(storeFrag);
		CollectionPageAdapter pageAdapter = new CollectionPageAdapter(getSupportFragmentManager(), fragments);
		collectionViewpager.setAdapter(pageAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.collection_back:
				finish();
				break;
			case R.id.collection_edit:
				if (isEdit) {
					collectionEdit.setText("编辑");
					productFrag.showDelete(false);
				} else {
					collectionEdit.setText("完成");
					productFrag.showDelete(true);
				}
				isEdit = !isEdit;
				break;
		}
	}
}
