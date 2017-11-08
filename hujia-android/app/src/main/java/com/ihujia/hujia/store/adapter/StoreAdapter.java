package com.ihujia.hujia.store.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.StoreEntity;
import com.ihujia.hujia.utils.ImageUtils;

import java.util.List;

/**
 * Created by liuzhichao on 2016/12/30.
 * 商家
 */
public class StoreAdapter extends BaseRecycleAdapter<StoreEntity> {

	private final View.OnClickListener onClickListener;

	public StoreAdapter(List<StoreEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_store);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		StoreEntity storeEntity = getItemData(position);
		ImageUtils.setSmallImg(holder.getView(R.id.sdv_store_img),storeEntity.getPicUrl());

		holder.setText(R.id.tv_store_name, storeEntity.getName());
		holder.setText(R.id.tv_store_address, storeEntity.getAddress());
		holder.setText(R.id.tv_store_distance, storeEntity.getDistance());
		holder.setTag(R.id.fl_store_layout, storeEntity);
		holder.setOnClickListener(R.id.fl_store_layout, onClickListener);
	}
}
