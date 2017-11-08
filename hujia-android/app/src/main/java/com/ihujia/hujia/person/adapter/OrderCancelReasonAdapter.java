package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ListDialogEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/27.
 */

public class OrderCancelReasonAdapter extends BaseAdapterNew<ListDialogEntity> {
	private Resources resources;
	public OrderCancelReasonAdapter(Context context, List<ListDialogEntity> mDatas) {
		super(context, mDatas);
		resources = context.getResources();
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.person_order_cancel_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ListDialogEntity entity = getItem(position);
		TextView textView = ViewHolder.get(convertView,R.id.order_cancel_item_text);
		textView.setText(entity.getReasonContent());
		if (entity.isSelected()) {
			textView.setTextColor(resources.getColor(R.color.primary_color_red));
		} else {
			textView.setTextColor(resources.getColor(R.color.primary_color));
		}
	}
}
