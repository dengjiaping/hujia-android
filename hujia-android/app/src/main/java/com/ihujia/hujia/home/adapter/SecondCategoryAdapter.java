package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CategoryEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/4/17.
 * 二级分类Adapter
 */
public class SecondCategoryAdapter extends BaseAdapterNew<CategoryEntity> {

	private View.OnClickListener onClickListener;

	public SecondCategoryAdapter(Context context, List<CategoryEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_second_category;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryEntity categoryEntity = getItem(position);
		if (categoryEntity != null) {
			TextView secondCategoryName = (TextView) convertView.findViewById(R.id.second_category_name);
			GridView secondCategoryDetail = (GridView) convertView.findViewById(R.id.second_category_detail);

			secondCategoryName.setText(categoryEntity.getName());
			ThirdCategoryAdapter thirdAdapter = new ThirdCategoryAdapter(getContext(), categoryEntity.getCategoryEntities(), onClickListener);
			secondCategoryDetail.setAdapter(thirdAdapter);
		}
	}
}
