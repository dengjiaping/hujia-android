package com.ihujia.hujia.otherstore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.OtherStoreEntity;
import com.ihujia.hujia.utils.ImageUtils;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/8.
 * 第三方店铺adapter
 */
public class OtherStoreAdapter extends BaseRecycleAdapter<OtherStoreEntity> {

	private View.OnClickListener onClickListener;
	private Context context;

	public OtherStoreAdapter(Context context, List<OtherStoreEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
		this.context = context;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_other_store);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		OtherStoreEntity otherStoreEntity = getItemData(position);

		ImageUtils.setSmallImg(holder.getView(R.id.sdv_other_store_cover), otherStoreEntity.getCover());
		ImageUtils.setSmallImg(holder.getView(R.id.sdv_other_store_logo), otherStoreEntity.getLogo());
		holder.setText(R.id.tv_other_store_name, otherStoreEntity.getName());
		holder.setText(R.id.tv_other_store_stock, context.getString(R.string.product_detail_sale_num, otherStoreEntity.getStock()));
		holder.setTag(R.id.cv_other_store_layout, otherStoreEntity);
		holder.setOnClickListener(R.id.cv_other_store_layout, onClickListener);
	}
}
