package com.ihujia.hujia.main.controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.common.utils.AnalysisUtil;
import com.common.utils.ToastUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseTabsDrawerActivity;
import com.ihujia.hujia.custom.controller.CustomActivity;
import com.ihujia.hujia.home.controller.HomeFragment;
import com.ihujia.hujia.login.controller.LoginActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.otherstore.controller.StoreListFragment;
import com.ihujia.hujia.person.controller.PersonFragment;
import com.ihujia.hujia.person.controller.ShopCarFragment;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.LocationUtils;
import com.ihujia.hujia.utils.PermissionUtils;

public class MainActivity extends BaseTabsDrawerActivity implements BDLocationListener {

	public static final String EXTRA_WHICH_TAB = "extra_which_tab";
	public static final long DIFF_DEFAULT_BACK_TIME = 2000;

	private long mBackTime = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isHasFragment = true;
		checkUpdateVersion();
		PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_CODE) {
			//如果取消了，结果数组将会为0，结果数组数量对应请求权限的个数
			if (grantResults.length < 1) {
				ToastUtil.shortShow(this, getString(R.string.get_permission_failed));
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int whichTab = intent.getIntExtra(EXTRA_WHICH_TAB, 0);
		setCurrentTab(whichTab);
	}

	@Override
	protected void addTabs() {
		addTab(initTabView(R.drawable.navigation_ic_home_selector, R.string.main_tab_home), HomeFragment.class, null);
//		addTab(initTabView(R.drawable.navigation_ic_store_selector, R.string.main_tab_store), StoreFragment.class, null);
		addTab(initTabView(R.drawable.tab_custom_normal, R.string.main_tab_custom), null, getString(R.string.main_tab_custom), null);
//		addTab(initTabView(R.drawable.navigation_ic_activity_selector, R.string.main_tab_activity), ActivityFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_store_selector, R.string.main_tab_store), StoreListFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_shopcar_selector, R.string.main_tab_cart), ShopCarFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_person_selector, R.string.main_tab_my), PersonFragment.class, null);
	}

	private View initTabView(int tabIcon, int tabText) {
		ViewGroup tab = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.main_tab_item, null);
		ImageView imageView = (ImageView) tab.findViewById(R.id.navigation);
		imageView.setImageResource(tabIcon);

		TextView textView = (TextView) tab.findViewById(R.id.txt_navigation);
		textView.setText(tabText);
		return tab;
	}

	@Override
	public void onTabChanged(String tabId) {
		super.onTabChanged(tabId);
		AnalysisUtil.onEvent(this, "caidan" + (currentIndex + 1));
		switch (currentIndex) {
			case 1:
				setCurrentTab(preIndex);
				if (UserCenter.isLogin(this)) {
					CustomActivity.startCustomActivity(this, "");
				} else {
					LoginActivity.startLoginActivity(this);
				}
				break;
			case 3:
				if (!UserCenter.isLogin(this)) {
					setCurrentTab(preIndex);
					LoginActivity.startLoginActivity(this);
				}
				break;
		}
	}

	private void startLocal() {
		LocationUtils.startLocation(this);
	}

	@Override
	public void onBackPressed() {
		long nowTime = System.currentTimeMillis();
		long diff = nowTime - mBackTime;
		if (diff >= DIFF_DEFAULT_BACK_TIME) {
			mBackTime = nowTime;
			Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
		} else {
			ExitManager.instance.exit();
		}
	}

	@Override
	public void onReceiveLocation(BDLocation bdLocation) {
//		Log.e("TEST", "定位结束了，latitude=" + bdLocation.getLatitude() + ",longitude=" + bdLocation.getLongitude());
		//距离计算
		LatLng location = new LatLng(Double.parseDouble(LocationUtils.getLocationLatitude(this)), Double.parseDouble(LocationUtils.getLocationLongitude(this)));
		LatLng target = new LatLng(39.923643, 116.418512);//王府井天主教堂
//		Log.e("TEST", "distance=" + DistanceUtil.getDistance(location, target));//单位:米s

		LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("ACTION"));
	}
}
