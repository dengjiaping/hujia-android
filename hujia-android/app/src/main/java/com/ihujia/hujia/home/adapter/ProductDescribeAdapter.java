package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ProductDescEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/9.
 * 商品详情商品描述
 */

public class ProductDescribeAdapter extends BaseAdapterNew<ProductDescEntity> {
	private int size;
	private Context context;

	public ProductDescribeAdapter(Context context, List<ProductDescEntity> mDatas) {
		super(context, mDatas);
		this.context = context;
		size = mDatas.size();
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.home_product_describe_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ProductDescEntity entity = getItem(position);
		View view1 = ViewHolder.get(convertView, R.id.product_describe_view1);
		View view2 = ViewHolder.get(convertView, R.id.product_describe_view2);
		TextView describeType = ViewHolder.get(convertView, R.id.product_describe_type);
		TextView describeDetail = ViewHolder.get(convertView, R.id.product_describe_detail);
		if (0 == position) {
			view1.setVisibility(View.VISIBLE);
		} else {
			view1.setVisibility(View.GONE);
		}
		if (size - 1 == position) {
			view2.setVisibility(View.VISIBLE);
		} else {
			view2.setVisibility(View.GONE);
		}

		if (entity!=null) {
			if (!StringUtil.isEmpty(entity.getDescribeType())) {
				describeType.setText(context.getString(R.string.product_detail_describe, entity.getDescribeType()));
			}
			describeDetail.setText(entity.getDescribeDetails());
		}
	}
}
