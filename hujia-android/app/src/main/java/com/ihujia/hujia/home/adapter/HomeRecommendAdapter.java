package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.HomeRecommendEntity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.LayoutUtil;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/19.
 * 首页下半部分adapter
 */
public class HomeRecommendAdapter extends BaseAdapterNew<HomeRecommendEntity> {

    private View.OnClickListener onClickListener;
    private Context context;

    public HomeRecommendAdapter(Context context, List<HomeRecommendEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.home_recommend_list_item;
    }

    @Override
    protected void setViewData(final View convertView, int position) {
        HomeRecommendEntity entity = getItem(position);
        if (entity != null) {
            TextView title = ViewHolder.get(convertView, R.id.home_recommend_title);
            title.setText(entity.getName());
            View view = ViewHolder.get(convertView, R.id.home_commend_more);
            view.setTag(entity);
            view.setTag(R.id.home_recommend, String.valueOf(position + 2));
            view.setOnClickListener(onClickListener);

            List<ProductEntity> productEntities = entity.getProductEntities();
            if (productEntities != null && productEntities.size() > 0) {
                ProductEntity productEntity1 = productEntities.get(0);
                View cloth1 = ViewHolder.get(convertView, R.id.recommend_cloth1);
                cloth1.setTag(productEntity1);
                cloth1.setTag(R.id.home_recommend, String.valueOf(1 + 3 * position));
                cloth1.setOnClickListener(onClickListener);
                showData(productEntity1, cloth1);

                if (productEntities.size() > 1) {
                    ProductEntity productEntity2 = productEntities.get(1);
                    View cloth2 = ViewHolder.get(convertView, R.id.recommend_cloth2);
                    cloth2.setTag(productEntity2);
                    cloth2.setTag(R.id.home_recommend, String.valueOf(2 + 3 * position));
                    cloth2.setOnClickListener(onClickListener);
                    showData(productEntity2, cloth2);
                }

                if (productEntities.size() > 2) {
                    ProductEntity productEntity3 = productEntities.get(2);
                    View cloth3 = ViewHolder.get(convertView, R.id.recommend_cloth3);
                    cloth3.setTag(productEntity3);
                    cloth3.setTag(R.id.home_recommend, String.valueOf(3 + 3 * position));
                    cloth3.setOnClickListener(onClickListener);
                    showData(productEntity3, cloth3);
                }
            } else {
                ViewHolder.get(convertView, R.id.home_recommend_products).setVisibility(View.GONE);
            }
        }
    }

    private void showData(ProductEntity productEntity, View cloth) {
        SimpleDraweeView img1 = (SimpleDraweeView) cloth.findViewById(R.id.home_recommend_cloth);
        TextView brand = (TextView) cloth.findViewById(R.id.home_recommend_brand);
        TextView name = (TextView) cloth.findViewById(R.id.home_recommend_name);
        TextView price = (TextView) cloth.findViewById(R.id.home_recommend_price);

        LayoutUtil.setHeightAsWidth(context, img1, 3, 5);
        ImageUtils.setSmallImg(img1, productEntity.getPicUrl());
        brand.setText(productEntity.getBrand());
        name.setText(productEntity.getName());

        price.setText(getContext().getString(R.string.product_price, NumberFormatUtil.formatMoney(productEntity.getPrice())));

        cloth.setTag(productEntity);
        cloth.setOnClickListener(onClickListener);
    }
}
