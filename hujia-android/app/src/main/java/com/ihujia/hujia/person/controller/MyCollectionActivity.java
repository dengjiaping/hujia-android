package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;

/**
 * Created by zhaoweiwei on 2017/3/24.
 * 我的收藏，不包括tab，只有商品收藏
 */

public class MyCollectionActivity extends ToolBarActivity implements View.OnClickListener{

	private FragmentTransaction transaction;
	private CollectionProductFrag productFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_mycollect);
//		FrameLayout layout = (FrameLayout) findViewById(R.id.collection_framelayout);
		productFrag = new CollectionProductFrag();
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_my_favorite));
		setRightTitle(R.drawable.edit_icon);
		mBtnTitleRight.setOnClickListener(this);
		rightText.setOnClickListener(this);

		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.collection_framelayout, productFrag);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		boolean isEnable = productFrag.getEnable();
		if (isEnable) {
			switch (v.getId()) {
				case R.id.right_button:
					productFrag.showDelete(true);
					setRightText(getString(R.string.person_finish));
					break;
				case R.id.rigth_text:
					productFrag.showDelete(false);
					setRightTitle(R.drawable.edit_icon);
					break;
			}
		}
	}

	public void setEdit() {
		setRightTitle(R.drawable.edit_icon);
	}
}
