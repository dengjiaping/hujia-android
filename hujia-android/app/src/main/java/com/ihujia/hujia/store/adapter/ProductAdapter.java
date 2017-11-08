package com.ihujia.hujia.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.LayoutUtil;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/6.
 * 商品列表
 */
public class ProductAdapter extends BaseRecycleAdapter<ProductEntity> {

	private View.OnClickListener onClickListener;
	private Context context;
	private int fromWhich;

	public ProductAdapter(Context context,List<ProductEntity> datas, View.OnClickListener onClickListener,int fromWhich) {
		super(datas);
		this.onClickListener = onClickListener;
		this.context = context;
		this.fromWhich = fromWhich;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.home_recommend_cloth_item);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		ProductEntity productEntity = getItemData(position);
		SimpleDraweeView img = holder.getView(R.id.home_recommend_cloth);
		switch (fromWhich) {
			case 1:
				LayoutUtil.setHeightAsWidth(context,img,2,5);
				break;
			case 2:
				LayoutUtil.setHeightAsWidth(context,img,3,5);
				break;
		}

		ImageUtils.setSmallImg(holder.getView(R.id.home_recommend_cloth),productEntity.getPicUrl());

		holder.setText(R.id.home_recommend_brand, productEntity.getBrand());
		holder.setText(R.id.home_recommend_name, productEntity.getName());
		holder.setText(R.id.home_recommend_price, holder.getContext().getString(R.string.product_price,  NumberFormatUtil.formatMoney(productEntity.getPrice())));
		holder.setTag(R.id.home_recommend_layout, productEntity);
		holder.setOnClickListener(R.id.home_recommend_layout, onClickListener);
	}
}
