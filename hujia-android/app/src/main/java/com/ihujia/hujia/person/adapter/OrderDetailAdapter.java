package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/31.
 * 订单详情中的列表
 */

public class OrderDetailAdapter extends BaseAdapterNew<ShopCarGoodEntity> {
    private Context context;

    public OrderDetailAdapter(Context context, List<ShopCarGoodEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.person_orderdetail_list_item;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ShopCarGoodEntity entity = getItem(position);
        RelativeLayout clothLayout = ViewHolder.get(convertView, R.id.order_detail_cloth_layout);
        SimpleDraweeView clothImg = ViewHolder.get(convertView, R.id.order_item_img);
        TextView clothName = ViewHolder.get(convertView, R.id.order_item_name);
        TextView clothColor = ViewHolder.get(convertView, R.id.order_item_color);
        TextView clothSize = ViewHolder.get(convertView, R.id.order_item_size);
        TextView clothPrice = ViewHolder.get(convertView, R.id.order_item_price);
        TextView clothNum = ViewHolder.get(convertView, R.id.order_item_num);

        if (entity != null) {
            ImageUtils.setSmallImg(clothImg, entity.getGoodsPic());
            String goodSize = null, goodsColor = null;
            List<GoodsAttrEntity> attrEntities = entity.getAttrList();
            for (GoodsAttrEntity attrEntity : attrEntities) {
                if ("尺码".equals(attrEntity.getAttrName())) {
                    goodSize = attrEntity.getAttrValue();
                } else if ("颜色".equals(attrEntity.getAttrName())) {
                    goodsColor = attrEntity.getAttrValue();
                }
            }
            clothName.setText(entity.getGoodsName());
            clothColor.setText(context.getString(R.string.shopcar_color, goodsColor));
            clothSize.setText(context.getString(R.string.shopcar_size, goodSize));
            clothPrice.setText(context.getString(R.string.price, NumberFormatUtil.formatMoney(entity.getGoodsPrice())));
            clothNum.setText(context.getString(R.string.num, entity.getCount()));

            clothLayout.setOnClickListener(v -> ProductDetailActivity.startProductDetailActivity(context, entity.getGoodsId()));
        }
    }
}
