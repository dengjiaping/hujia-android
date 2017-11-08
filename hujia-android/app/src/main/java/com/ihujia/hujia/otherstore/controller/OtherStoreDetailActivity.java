package com.ihujia.hujia.otherstore.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.network.entities.OtherStoreDetailEntity;
import com.ihujia.hujia.otherstore.adapter.ProductPagerAdapter;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2017/2/8.
 * 第三方店铺详情
 */
public class OtherStoreDetailActivity extends BaseActivity {

	@ViewInject(R.id.iv_other_store_detail_back)
	private View ivOtherStoreDetailBack;
	@ViewInject(R.id.sdv_other_store_detail_bg)
	private SimpleDraweeView sdvOtherStoreDetailBg;
	@ViewInject(R.id.sdv_other_store_detail_logo)
	private SimpleDraweeView sdvOtherStoreDetailLogo;
	@ViewInject(R.id.tv_other_store_name)
	private TextView tvOtherStoreName;
	@ViewInject(R.id.tv_other_store_stock)
	private TextView tvOtherStoreStock;
	@ViewInject(R.id.tl_other_store_detail_tab)
	private TabLayout tlOtherStoreDetailTab;
	@ViewInject(R.id.vp_other_store_detail_content)
	private ViewPager vpOtherStoreDetailContent;

	private String[] titles = new String[]{"推荐商品", "热销商品", "全部商品"};

	public static void startOtherStoreDetailActivity(Context context, String id) {
		Intent intent = new Intent(context, OtherStoreDetailActivity.class);
		intent.putExtra(EXTRA_ID, id);
		context.startActivity(intent);
	}

	public static void startOtherStoreDetailActivity(Context context, String id, int flag) {
		Intent intent = new Intent(context, OtherStoreDetailActivity.class);
		intent.putExtra(EXTRA_ID, id);
		intent.addFlags(flag);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_other_store_detail);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		ivOtherStoreDetailBack.setOnClickListener(v -> finish());

		String id = getIntent().getStringExtra(EXTRA_ID);

		List<Fragment> fragments = new ArrayList<>();
		ProductFragment productFragment1 = new ProductFragment();
		ProductFragment productFragment2 = new ProductFragment();
		ProductFragment productFragment3 = new ProductFragment();
		productFragment1.setArgs(ProductFragment.TYPE_RECOMMEND, id);
		productFragment2.setArgs(ProductFragment.TYPE_HOT, id);
		productFragment3.setArgs(ProductFragment.TYPE_ALL, id);
		fragments.add(productFragment1);
		fragments.add(productFragment2);
		fragments.add(productFragment3);
		ProductPagerAdapter pagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), fragments, titles);

		vpOtherStoreDetailContent.setAdapter(pagerAdapter);
		tlOtherStoreDetailTab.setupWithViewPager(vpOtherStoreDetailContent, true);
	}

	/**
	 * @param otherStoreDetail 通过下面的fragment来更新头部数据
	 */
	public void updateHead(OtherStoreDetailEntity otherStoreDetail) {
		ImageUtils.setSmallImg(sdvOtherStoreDetailBg, otherStoreDetail.getCover());
		ImageUtils.setSmallImg(sdvOtherStoreDetailLogo, otherStoreDetail.getLogo());
		tvOtherStoreName.setText(otherStoreDetail.getName());
		tvOtherStoreStock.setText(getString(R.string.product_detail_sale_num, otherStoreDetail.getStock()));
	}
}
