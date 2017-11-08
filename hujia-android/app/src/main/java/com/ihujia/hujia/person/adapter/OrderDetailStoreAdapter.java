package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.OrderStoreEntity;
import com.ihujia.hujia.utils.ListViewUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/28.
 */

public class OrderDetailStoreAdapter extends BaseAdapterNew<OrderStoreEntity> {
	private View.OnClickListener onClickListener;
	private Context context;
	public OrderDetailStoreAdapter(Context context, List<OrderStoreEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.onClickListener = onClickListener;
		this.context = context;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.confirm_order_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		OrderStoreEntity entity = getItem(position);
		TextView storeName = ViewHolder.get(convertView,R.id.confirm_order_store_name);
		LinearLayout storeLayout = ViewHolder.get(convertView,R.id.confirm_order_store_layout);
		ListView goodsList = ViewHolder.get(convertView,R.id.confirm_order_list);
		LinearLayout storeFreight = ViewHolder.get(convertView,R.id.confirm_order_freight);

		storeFreight.setVisibility(View.GONE);

		storeLayout.setTag(entity.getStoreId());
		storeLayout.setTag(entity.getStoreName());
		storeLayout.setOnClickListener(onClickListener);
		storeName.setText(entity.getStoreName());
		OrderDetailAdapter adapter = new OrderDetailAdapter(context, entity.getGoodsList());
		goodsList.setAdapter(adapter);
		ListViewUtil.setListViewHeightBasedOnChildren(goodsList);
	}
}
