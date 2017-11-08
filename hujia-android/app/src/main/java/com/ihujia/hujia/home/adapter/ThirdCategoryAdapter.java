package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CategoryEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/4/18.
 * 三级分类adapter
 */
class ThirdCategoryAdapter extends BaseAdapterNew<CategoryEntity> {

	private View.OnClickListener onClickListener;

	ThirdCategoryAdapter(Context context, List<CategoryEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_third_category;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryEntity categoryEntity = getItem(position);
		if (categoryEntity != null) {
			TextView name = (TextView) convertView;
			name.setText(categoryEntity.getName());
			convertView.setTag(categoryEntity);
			convertView.setOnClickListener(onClickListener);
		}
	}
}
