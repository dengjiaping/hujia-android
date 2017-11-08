package com.ihujia.hujia.person.adapter;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.adapter.ViewHolder;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.IntegralEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class IntegralListAdapter extends BaseRecycleAdapter<IntegralEntity> {
	public IntegralListAdapter(List<IntegralEntity> datas) {
		super(datas);
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.person_integral_item);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		IntegralEntity entity = getItemData(position);
		TextView value = holder.getView(R.id.integral_item_value);
		TextView time = holder.getView(R.id.integral_item_time);
		TextView type = holder.getView(R.id.integral_item_type);
		TextView timeInfo = holder.getView(R.id.integral_item_timeinfo);
		value.setText(entity.getValue());

	}
}
