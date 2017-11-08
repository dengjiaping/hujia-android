package com.ihujia.hujia.store.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.common.utils.AnalysisUtil;
import com.common.widget.RefreshRecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.network.entities.StoreEntity;
import com.ihujia.hujia.store.adapter.StoreAdapter;
import com.ihujia.hujia.store.maputils.ClusterItem;
import com.ihujia.hujia.store.maputils.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2016/12/16.
 * 店铺
 */
public class StoreFragment extends BaseFragment implements View.OnClickListener,RefreshRecyclerView.OnRefreshAndLoadMoreListener {

	private View mView;
	private RefreshRecyclerView rrvStoreList;
	private MapView mvStoreMap;
	private BaiduMap map;
	private ImageView rightButton;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.frag_store, null);
			initView();
			initMapView();
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mView.findViewById(R.id.left_button).setVisibility(View.GONE);
		TextView mTxtLeft = (TextView) mView.findViewById(R.id.left_text);
		mTxtLeft.setText("北京");
		mTxtLeft.setOnClickListener(this);
		mTxtLeft.setVisibility(View.VISIBLE);
		TextView toolbarTitleCenter = (TextView) mView.findViewById(R.id.toolbar_title);
		toolbarTitleCenter.setText("店铺");
		toolbarTitleCenter.setVisibility(View.VISIBLE);
		rightButton = (ImageView) mView.findViewById(R.id.right_button);
		rightButton.setImageResource(R.drawable.list_icon);
		rightButton.setOnClickListener(this);
		rightButton.setVisibility(View.VISIBLE);

		rrvStoreList = (RefreshRecyclerView) mView.findViewById(R.id.rrv_store_list);
		rrvStoreList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvStoreList.setCanAddMore(false);
		rrvStoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
		rrvStoreList.setOnRefreshAndLoadMoreListener(this);
	}

	private void initMapView() {
		mvStoreMap = (MapView) mView.findViewById(R.id.mv_store_map);
		//必须按照3 2 1 的顺序，否则会报错
//		mvStoreMap.removeViewAt(3);//比例尺
//		mvStoreMap.removeViewAt(2);//放大缩小按钮
		mvStoreMap.removeViewAt(1);//百度地图图标
		mvStoreMap.showZoomControls(false);//放大缩小按钮
		mvStoreMap.showScaleControl(false);//比例尺

		LatLng target = new LatLng(39.923643, 116.418512);//王府井天主教堂
		map = mvStoreMap.getMap();
		map.setMaxAndMinZoomLevel(21, 10);//设置最大最小可缩放级别

		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setCompassEnabled(false);
		uiSettings.setOverlookingGesturesEnabled(false);
		uiSettings.setRotateGesturesEnabled(false);

//		map.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.zoom(16);
		builder.target(target);//设置地图中心点
//		builder.targetScreen();//设置地图操作中心点在屏幕的坐标, 只有在 OnMapLoadedCallback.onMapLoaded() 之后设置才生效
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
		map.animateMapStatus(mapStatusUpdate);

		//需要判断商家点是否在范围内，在则显示，还需要监听处理
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_map_pop, null);
		view.findViewById(R.id.rl_map_pop_layout).setOnClickListener(this);
		SimpleDraweeView sdvMapPopImg = (SimpleDraweeView) view.findViewById(R.id.sdv_map_pop_img);
		TextView tvMapPopTitle = (TextView) view.findViewById(R.id.tv_map_pop_title);
		TextView tvMapPopAddress = (TextView) view.findViewById(R.id.tv_map_pop_address);
		//第三个参数为Y轴偏移量
		InfoWindow infoWindow = new InfoWindow(view, target, -90);
//		map.showInfoWindow(infoWindow);

		LatLng target1 = new LatLng(39.924143, 116.418562);
		LatLng target2 = new LatLng(39.924643, 116.418612);
		LatLng target3 = new LatLng(39.925143, 116.418662);
		LatLng target4 = new LatLng(39.925643, 116.418712);
		BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromView(view);
		BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromView(view);
		BitmapDescriptor bitmapDescriptor3 = BitmapDescriptorFactory.fromView(view);
		BitmapDescriptor bitmapDescriptor4 = BitmapDescriptorFactory.fromView(view);
		//anchor锚点，指实际经纬度对应覆盖物的位置，默认是在覆盖物图形的正中间(0.5f,0.5f)
		OverlayOptions overlayOptions1 = new MarkerOptions().position(target).icon(bitmapDescriptor1).anchor(0.15f, 0.8f);
		//zIndex表示标注的级别，数值越大级别越高，级别高的会覆盖在级别低的上方
		OverlayOptions overlayOptions2 = new MarkerOptions().position(target2).icon(bitmapDescriptor2).anchor(1.0f, 1.0f).zIndex(1);
		OverlayOptions overlayOptions3 = new MarkerOptions().position(target3).icon(bitmapDescriptor3).anchor(0.5f, 0.5f);
		OverlayOptions overlayOptions4 = new MarkerOptions().position(target4).icon(bitmapDescriptor4).anchor(0.8f, 0.8f);
//		final Marker marker1 = (Marker) map.addOverlay(overlayOptions1);
//		final Marker marker2 = (Marker) map.addOverlay(overlayOptions2);
//		final Marker marker3 = (Marker) map.addOverlay(overlayOptions3);
//		final Marker marker4 = (Marker) map.addOverlay(overlayOptions4);
		map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				/*if (marker == marker1) {
					Log.e("TEST", "我是marker1");
				} else if (marker == marker2) {
					Log.e("TEST", "我是marker2");
				} else if (marker == marker3) {
					Log.e("TEST", "我是marker3");
				} else if (marker == marker4) {
					Log.e("TEST", "我是marker4");
				} else {
					Log.e("TEST", "我是marker");
				}*/
				return true;
			}
		});

		// 初始化点聚合管理类
		ClusterManager<MyItem> mClusterManager = new ClusterManager<>(getActivity(), map);
		// 向点聚合管理类中添加Marker实例
		List<MyItem> items = new ArrayList<>();
		items.add(new MyItem(target));
		items.add(new MyItem(target1));
		items.add(new MyItem(target2));
		items.add(new MyItem(target3));
		items.add(new MyItem(target4));
		mClusterManager.addItems(items);
		map.setOnMapStatusChangeListener(mClusterManager);

		map.setOnMapLoadedCallback(() -> {
			LatLngBounds bound = map.getMapStatus().bound;//当前屏幕显示范围内的地理范围
//				获取地图投影坐标转换器, 当地图初始化完成之前返回 null，在 OnMapLoadedCallback.onMapLoaded() 之后才能正常
			Projection projection = map.getProjection();//用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			Point point = projection.toScreenLocation(new LatLng(39.923643, 116.408512));
		});

		BaiduMapOptions options = new BaiduMapOptions();
		options.compassEnabled(false);//指南针
		options.overlookingGesturesEnabled(false);//手势切换俯视图
		options.rotateGesturesEnabled(false);//手势旋转
		options.scaleControlEnabled(false);//比例尺
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData(false);
	}

	private void loadData(boolean isMore) {
		List<StoreEntity> storeEntities = new ArrayList<>();
		StoreEntity storeEntity1 = new StoreEntity("1", "店铺一", "北京市东城区京宝大厦304", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/7c327f4fb8ca4367b81af3caeb7eccfb.jpg", "100km");
		StoreEntity storeEntity2 = new StoreEntity("2", "店铺二", "北京市东城区京宝大厦304", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/b514457b2fd04b90839d2ef04b1c57e8.jpg", "200km");
		StoreEntity storeEntity3 = new StoreEntity("3", "店铺三", "北京市东城区京宝大厦304", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/25ddb0a06e2d425889418856aba68ad3.jpg", "300km");
		StoreEntity storeEntity4 = new StoreEntity("4", "店铺四", "北京市东城区京宝大厦304", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/9cbf5192d5cf4cec80fcaf7701c6a36a.jpg", "400km");
		storeEntities.add(storeEntity1);
		storeEntities.add(storeEntity2);
		storeEntities.add(storeEntity3);
		storeEntities.add(storeEntity4);
		StoreAdapter adapter = new StoreAdapter(storeEntities, this);
		rrvStoreList.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.left_text:
				break;
			case R.id.right_button:
				AnalysisUtil.onEvent(getActivity(), "test");
				if (mvStoreMap.getVisibility() == View.VISIBLE) {
					startScaleXAnimator(mvStoreMap, 1, 0, new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							rightButton.setImageResource(R.drawable.seat_icon);
							mvStoreMap.setVisibility(View.GONE);
							rrvStoreList.setVisibility(View.VISIBLE);
							mvStoreMap.clearAnimation();
							startScaleXAnimator(rrvStoreList, 0, 1, null);
						}
					});
				} else {
					startScaleXAnimator(rrvStoreList, 1, 0, new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							rightButton.setImageResource(R.drawable.list_icon);
							rrvStoreList.setVisibility(View.GONE);
							mvStoreMap.setVisibility(View.VISIBLE);
							rrvStoreList.clearAnimation();
							startScaleXAnimator(mvStoreMap, 0, 1, null);
						}
					});
				}
				break;
			case R.id.rl_map_pop_layout:
				break;
			case R.id.fl_store_layout:
				StoreEntity storeEntity = (StoreEntity) view.getTag();
				if (storeEntity != null) {
					StoreDetailActivity.startStoreDetailActivity(getActivity(), storeEntity.getId());
				}
				break;
		}
	}

	private void startScaleXAnimator(View view, float start, float end, Animator.AnimatorListener listener) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "scaleX", start, end);
		objectAnimator.setDuration(200);
		if (listener != null) {
			objectAnimator.addListener(listener);
		} else {
			view.clearAnimation();
		}
		objectAnimator.start();
	}

	@Override
	public void onRefresh() {
		loadData(false);
	}

	@Override
	public void onLoadMore() {
		loadData(true);
	}

	/**
	 * 每个Marker点，包含Marker点坐标以及图标
	 */
	public class MyItem implements ClusterItem {
		private final LatLng mPosition;

		public MyItem(LatLng latLng) {
			mPosition = latLng;
		}

		@Override
		public LatLng getPosition() {
			return mPosition;
		}

		@Override
		public BitmapDescriptor getBitmapDescriptor() {
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_map_pop, null);
			return BitmapDescriptorFactory.fromView(view);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mvStoreMap.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mvStoreMap.onPause();
	}

	@Override
	public void onDestroy() {
		mvStoreMap.onDestroy();
		mvStoreMap = null;
		super.onDestroy();
	}
}
