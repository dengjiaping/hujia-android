package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.StringUtil;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.RecommendItemEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.LayoutUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 */

public class CollectionAdapter extends BaseRecycleAdapter<RecommendItemEntity> {
	private List<RecommendItemEntity> datas;
	private View.OnClickListener onclickListener;
	private Context context;
	public CollectionAdapter(Context context,List<RecommendItemEntity> datas, View.OnClickListener onclickListener) {
		super(datas);
		this.datas = datas;
		this.onclickListener = onclickListener;
		this.context = context;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.home_recommend_cloth_item);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		RecommendItemEntity entity = getItemData(position);
		FrameLayout layout = holder.getView(R.id.home_recommend_layout);
		SimpleDraweeView clothImg = holder.getView(R.id.home_recommend_cloth);
		TextView brand = holder.getView(R.id.home_recommend_brand);
		TextView clothName = holder.getView(R.id.home_recommend_name);
		TextView clothPrice = holder.getView(R.id.home_recommend_price);
		ImageView delete = holder.getView(R.id.cloth_item_delete);

		LayoutUtil.setHeightAsWidth(context,clothImg,3,5);
		String url = entity.getImgUrl();
		if (!StringUtil.isEmpty(url)) {
			ImageUtils.setSmallImg(clothImg, url);
		}
		brand.setText(entity.getBrandName());
		clothName.setText(entity.getClothName());
		clothPrice.setText(entity.getSalePrice());

		if (entity.isShowDelete()) {
			delete.setVisibility(View.VISIBLE);
			delete.setTag(position);
			delete.setOnClickListener(onclickListener);
		} else {
			delete.setVisibility(View.GONE);
		}

		layout.setTag(entity);
		layout.setOnClickListener(onclickListener);
	}

	public void showDelete(boolean isShow) {
		if (isShow) {
			for (RecommendItemEntity entity : datas) {
				entity.setShowDelete(true);
			}
		} else {
			for (RecommendItemEntity entity : datas) {
				entity.setShowDelete(false);
			}
		}
		notifyDataSetChanged();
	}
}
