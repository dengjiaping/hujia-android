package com.ihujia.hujia.activity.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.activity.adapter.ActivityAdapter;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.login.controller.LoginActivity;
import com.ihujia.hujia.network.entities.ActivityEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2016/12/16.
 * 活动
 */
public class ActivityFragment extends BaseFragment implements RefreshRecyclerView.OnRefreshAndLoadMoreListener,View.OnClickListener {

	private RefreshRecyclerView rrvActivityList;
	private View mView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.frag_activity, null);
			initView();
		}
		ViewGroup mViewParent = (ViewGroup) mView.getParent();
		if (mViewParent != null) {
			mViewParent.removeView(mView);
		}
		return mView;
	}

	private void initView() {
		mView.findViewById(R.id.left_button).setVisibility(View.GONE);
		TextView toolbarTitle = (TextView) mView.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.main_tab_activity));
		toolbarTitle.setVisibility(View.VISIBLE);
		rrvActivityList = (RefreshRecyclerView) mView.findViewById(R.id.rrv_activity_list);
		rrvActivityList.setHasFixedSize(true);//使RecycleView保持固定大小
		rrvActivityList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvActivityList.setLayoutManager(new LinearLayoutManager(getActivity()));
		rrvActivityList.setOnRefreshAndLoadMoreListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
	}

	private void loadData() {
		//添加测试数据
		List<ActivityEntity> activityEntities = new ArrayList<>();
		ActivityEntity activityEntity1 = new ActivityEntity("1", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/7c327f4fb8ca4367b81af3caeb7eccfb.jpg", "活动一", "linkUrl");
		ActivityEntity activityEntity2 = new ActivityEntity("2", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/b514457b2fd04b90839d2ef04b1c57e8.jpg", "活动二", "linkUrl");
		ActivityEntity activityEntity3 = new ActivityEntity("3", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/25ddb0a06e2d425889418856aba68ad3.jpg", "活动三", "linkUrl");
		ActivityEntity activityEntity4 = new ActivityEntity("4", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/9cbf5192d5cf4cec80fcaf7701c6a36a.jpg", "活动四", "linkUrl");
		ActivityEntity activityEntity5 = new ActivityEntity("5", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/7c327f4fb8ca4367b81af3caeb7eccfb.jpg", "活动五", "linkUrl");
		ActivityEntity activityEntity6 = new ActivityEntity("6", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/b514457b2fd04b90839d2ef04b1c57e8.jpg", "活动六", "linkUrl");
		ActivityEntity activityEntity7 = new ActivityEntity("7", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/25ddb0a06e2d425889418856aba68ad3.jpg", "活动七", "linkUrl");
		ActivityEntity activityEntity8 = new ActivityEntity("8", "http://7xouy6.com2.z0.glb.qiniucdn.com/wjika-java/9cbf5192d5cf4cec80fcaf7701c6a36a.jpg", "活动八", "linkUrl");
		activityEntities.add(activityEntity1);
		activityEntities.add(activityEntity2);
		activityEntities.add(activityEntity3);
		activityEntities.add(activityEntity4);
		activityEntities.add(activityEntity5);
		activityEntities.add(activityEntity6);
		activityEntities.add(activityEntity7);
		activityEntities.add(activityEntity8);
		ActivityAdapter adapter = new ActivityAdapter(activityEntities, this);
		rrvActivityList.setAdapter(adapter);
		rrvActivityList.setCanAddMore(false);
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public void onLoadMore() {
		loadData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.fl_activity_layout:
				//活动点击跳转活动页
				ActivityEntity activityEntity = (ActivityEntity) view.getTag();
				if (activityEntity != null) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			break;
		}
	}
}
