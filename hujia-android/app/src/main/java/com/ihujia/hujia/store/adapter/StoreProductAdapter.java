package com.ihujia.hujia.store.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ProductEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/5.
 * 商品橱窗
 */
public class StoreProductAdapter extends BaseAdapterNew<ProductEntity> {

	public StoreProductAdapter(Context context, List<ProductEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_store_product;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ProductEntity productEntity = getItem(position);
		SimpleDraweeView sdvStoreProductImg = ViewHolder.get(convertView, R.id.sdv_store_product_img);
		TextView tvStoreProductName = ViewHolder.get(convertView, R.id.tv_store_product_name);
		TextView tvStoreProductPrice = ViewHolder.get(convertView, R.id.tv_store_product_price);

		if (productEntity != null) {
			sdvStoreProductImg.setImageURI(productEntity.getPicUrl());
			tvStoreProductName.setText(productEntity.getName());
			tvStoreProductPrice.setText(productEntity.getPrice());
		}
	}
}
