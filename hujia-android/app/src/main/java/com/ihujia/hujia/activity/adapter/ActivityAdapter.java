package com.ihujia.hujia.activity.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.StringUtil;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ActivityEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2016/12/18.
 * 活动
 */
public class ActivityAdapter extends BaseRecycleAdapter<ActivityEntity> {

	private View.OnClickListener onClickListener;

	public ActivityAdapter(List<ActivityEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_activity);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		ActivityEntity activityEntity = getItemData(position);
		SimpleDraweeView sdvActivityImg = holder.getView(R.id.sdv_activity_img);
		if (!StringUtil.isEmpty(activityEntity.getPicUrl())) {
			sdvActivityImg.setImageURI(Uri.parse(activityEntity.getPicUrl()));
		}
		holder.setText(R.id.tv_activity_name, activityEntity.getName());
		holder.setTag(R.id.fl_activity_layout, activityEntity);
		holder.setOnClickListener(R.id.fl_activity_layout, onClickListener);
	}
}
