package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CityEntity;

import java.util.List;

/**
 * Created by Liu_ZhiChao on 2015/9/24 21:05.
 * 城市列表 R.home_morestore.address_list_item
 */
public class CityListAdapter extends BaseAdapterNew<CityEntity> {

	public CityListAdapter(Context context, List<CityEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.address_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CityEntity item = getItem(position);
		TextView addressItemName = ViewHolder.get(convertView, R.id.address_item_name);
		addressItemName.setText(item.getName());
	}
}
