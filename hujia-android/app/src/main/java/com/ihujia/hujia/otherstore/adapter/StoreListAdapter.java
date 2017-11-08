package com.ihujia.hujia.otherstore.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.OtherStoreEntity;
import com.ihujia.hujia.utils.ImageUtils;

import java.util.List;

/**
 * Created by zww on 2017/6/27.
 * 店铺列表
 */

public class StoreListAdapter extends BaseAdapterNew<OtherStoreEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public StoreListAdapter(Context context, List<OtherStoreEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_other_store;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OtherStoreEntity otherStoreEntity = getItem(position);

        SimpleDraweeView cover = ViewHolder.get(convertView, R.id.sdv_other_store_cover);
        SimpleDraweeView logo = ViewHolder.get(convertView, R.id.sdv_other_store_logo);
        TextView storeName = ViewHolder.get(convertView, R.id.tv_other_store_name);
        TextView storeStock = ViewHolder.get(convertView, R.id.tv_other_store_stock);
        CardView storeLayout = ViewHolder.get(convertView,R.id.cv_other_store_layout);

        if (otherStoreEntity != null) {
            ImageUtils.setSmallImg(cover, otherStoreEntity.getCover());
            ImageUtils.setSmallImg(logo, otherStoreEntity.getLogo());
            storeName.setText(otherStoreEntity.getName());
            storeStock.setText(context.getString(R.string.product_detail_sale_num, otherStoreEntity.getStock()));
        }
        storeLayout.setTag(R.id.tag_store_item,otherStoreEntity);
        storeLayout.setOnClickListener(onClickListener);
    }
}
