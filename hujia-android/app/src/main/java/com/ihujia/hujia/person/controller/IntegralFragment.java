package com.ihujia.hujia.person.controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.network.entities.IntegralEntity;
import com.ihujia.hujia.person.adapter.IntegralListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class IntegralFragment extends BaseFragment implements View.OnClickListener{
	private int state;

	public void setArgs(int state) {
		this.state = state;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_frag_integral, null);
		initLoadingView(this,view);
		setLoadingStatus(LoadingStatus.GONE);
		RefreshRecyclerView recyclerView = (RefreshRecyclerView) view.findViewById(R.id.integral_recycleview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		List<IntegralEntity> entities = new ArrayList<>();
		IntegralEntity entity = new IntegralEntity("120","2016-12-23 12:20:30","积分兑换","积分兑换时间");
		for (int i = 0; i < 20; i++) {
			entities.add(entity);
		}
		IntegralListAdapter adapter = new IntegralListAdapter(entities);
		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				super.getItemOffsets(outRect, view, parent, state);
			}
		});
		return view;
	}

	@Override
	public void onClick(View view) {

	}
}
