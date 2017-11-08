package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CategoryEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/6.
 *
 */
public class DetailCategoryAdapter extends BaseAdapterNew<CategoryEntity> {

	public DetailCategoryAdapter(Context context, List<CategoryEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_detail_category;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryEntity categoryEntity = getItem(position);
		TextView tvDetailCategoryName = ViewHolder.get(convertView, R.id.tv_detail_category_name);
		SimpleDraweeView simpleDraweeView = ViewHolder.get(convertView, R.id.sdv_detail_category_img);
		if (categoryEntity != null) {
			tvDetailCategoryName.setText(categoryEntity.getName());
			if (!StringUtil.isEmpty(categoryEntity.getImgUrl())) {
				simpleDraweeView.setImageURI(categoryEntity.getImgUrl());
			}
		}
	}
}
