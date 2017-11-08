package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.LogisticEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/12.
 */

public class LogisticDetailAdapter extends BaseAdapterNew<LogisticEntity> {
	private int lastPosition;
	private Resources resources;

	public LogisticDetailAdapter(Context context, List<LogisticEntity> mDatas) {
		super(context, mDatas);
		resources = context.getResources();
		lastPosition = mDatas.size() - 1;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.person_logistic_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		LogisticEntity entity = getItem(position);
		View view1 = ViewHolder.get(convertView, R.id.logistic_item_view1);
		ImageView img = ViewHolder.get(convertView, R.id.logistic_item_img);
		View view2 = ViewHolder.get(convertView, R.id.logistic_item_view2);
		TextView describe = ViewHolder.get(convertView, R.id.logistic_item_describe);
		TextView time = ViewHolder.get(convertView, R.id.logistic_item_time);
		if (0 == position) {
			view1.setVisibility(View.INVISIBLE);
			view2.setVisibility(View.VISIBLE);
			describe.setTextColor(resources.getColor(R.color.primary_color));
			time.setTextColor(resources.getColor(R.color.primary_color));
			img.setImageResource(R.drawable.logistics_yellow);
		} else if (lastPosition == position) {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.INVISIBLE);
			describe.setTextColor(resources.getColor(R.color.person_check_order_textcolor));
			time.setTextColor(resources.getColor(R.color.person_check_order_textcolor));
			img.setImageResource(R.drawable.logistics_gray);
		} else {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			describe.setTextColor(resources.getColor(R.color.person_check_order_textcolor));
			time.setTextColor(resources.getColor(R.color.person_check_order_textcolor));
			img.setImageResource(R.drawable.logistics_gray);
		}

		describe.setText(entity.getDescribe());
		time.setText(entity.getTime());
	}
}
