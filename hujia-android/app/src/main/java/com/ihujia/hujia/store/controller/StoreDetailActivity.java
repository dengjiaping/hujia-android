package com.ihujia.hujia.store.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.common.view.GridViewForInner;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.store.adapter.StoreCoverAdapter;
import com.ihujia.hujia.store.adapter.StoreProductAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2016/12/20.
 * 店铺详情
 */
public class StoreDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

	@ViewInject(R.id.iv_store_detail_back)
	private View ivStoreDetailBack;
	@ViewInject(R.id.vp_store_detail_cover)
	private ViewPager vpStoreDetailCover;
	@ViewInject(R.id.tv_store_detail_index)
	private TextView tvStoreDetailIndex;
	@ViewInject(R.id.tv_store_detail_map)
	private View tvStoreDetailMap;
	@ViewInject(R.id.iv_store_detail_phone)
	private View ivStoreDetailPhone;
	@ViewInject(R.id.gvfi_store_detail_product)
	private GridViewForInner gvfiStoreDetailProduct;
	@ViewInject(R.id.tv_store_detail_more)
	private TextView tvStoreDetailMore;

	private StoreCoverAdapter adapter;
	private List<ProductEntity> productEntities;

	public static void startStoreDetailActivity(Context context, String id) {
		Intent intent = new Intent(context, StoreDetailActivity.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_detail);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		ivStoreDetailBack.setOnClickListener(v -> finish());

		List<String> urls = new ArrayList<>();
		urls.add("http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/7c327f4fb8ca4367b81af3caeb7eccfb.jpg");
		urls.add("http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/b514457b2fd04b90839d2ef04b1c57e8.jpg");
		urls.add("http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/25ddb0a06e2d425889418856aba68ad3.jpg");
		urls.add("http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/9cbf5192d5cf4cec80fcaf7701c6a36a.jpg");
		adapter = new StoreCoverAdapter(urls);
		tvStoreDetailIndex.setText("1/" + adapter.getCount());
		vpStoreDetailCover.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				tvStoreDetailIndex.setText(++position + "/" + adapter.getCount());
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		vpStoreDetailCover.setAdapter(adapter);

		productEntities = new ArrayList<>();
		StoreProductAdapter storeProductAdapter = new StoreProductAdapter(this, productEntities);
		gvfiStoreDetailProduct.setOnItemClickListener(this);
		gvfiStoreDetailProduct.setAdapter(storeProductAdapter);

		tvStoreDetailMap.setOnClickListener(this);
		ivStoreDetailPhone.setOnClickListener(this);
		tvStoreDetailMore.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_store_detail_map:
				startNavi();
				break;
			case R.id.iv_store_detail_phone:
				tell("15600720501");
				break;
			case R.id.tv_store_detail_more:
				ProductActivity.startProductActivity(this, "", 0, "");
				break;
		}
	}

	/**
	 * 启动百度地图导航(Native)
	 */
	public void startNavi() {
		// 天安门坐标
		double mLat1 = 39.915291;
		double mLon1 = 116.403857;
		// 百度大厦坐标
		double mLat2 = 40.056858;
		double mLon2 = 116.308194;
		LatLng pt1 = new LatLng(mLat1, mLon1);
		LatLng pt2 = new LatLng(mLat2, mLon2);

		// 构建 导航参数
		NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("天安门").endName("百度大厦");
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			showDialog();
		}
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", (dialog, which) -> {
			dialog.dismiss();
			OpenClientUtil.getLatestBaiduMapApp(this);
		});

		builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
		builder.create().show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (productEntities != null && productEntities.size() > 0) {
			ProductEntity productEntity = productEntities.get(position);
			if (productEntity != null) {
				ProductDetailActivity.startProductDetailActivity(this, productEntity.getId());
			}
		}
	}
}
