package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CategoryEntity;
import com.ihujia.hujia.utils.ImageUtils;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/18.
 *
 */
public class HomeTypeAdapter extends BaseAdapterNew<CategoryEntity>{

	public HomeTypeAdapter(Context context, List<CategoryEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.home_type_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryEntity categoryEntity = getItem(position);

		SimpleDraweeView logo = ViewHolder.get(convertView,R.id.home_type_item_img);
		TextView name = ViewHolder.get(convertView,R.id.home_type_item_name);

		if (categoryEntity != null) {
			String url = categoryEntity.getImgUrl();
			ImageUtils.setImgUrl(logo, url);
			name.setText(categoryEntity.getName());
		}
	}
}
