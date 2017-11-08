package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/3.
 * 退货列表adapter
 */

public class CustomerServiceAdapter extends BaseAdapterNew<OrderItemEntity> {
    private View.OnClickListener onClickListener;
    private Context context;

    public CustomerServiceAdapter(Context context, List<OrderItemEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.person_customer_service_item;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderItemEntity entity = getItem(position);
        TextView number = ViewHolder.get(convertView, R.id.order_item_store);
        TextView state = ViewHolder.get(convertView, R.id.order_item_state);
        SimpleDraweeView img = ViewHolder.get(convertView, R.id.order_item_img);
        TextView name = ViewHolder.get(convertView, R.id.order_item_name);
        TextView clothColor = ViewHolder.get(convertView, R.id.order_item_color);
        TextView clothSize = ViewHolder.get(convertView, R.id.order_item_size);
        TextView price = ViewHolder.get(convertView, R.id.order_item_price);
        TextView num = ViewHolder.get(convertView, R.id.order_item_num);
        TextView refundTime = ViewHolder.get(convertView, R.id.refundapply_item_time);
        TextView progressQuery = ViewHolder.get(convertView, R.id.order_item_progress_query);

        if (entity != null) {
            ShopCarGoodEntity shopCarGoodEntity = entity.getGoodsList().get(0);
            number.setText(context.getString(R.string.person_refund_number, entity.getRefundId()));
            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person_order_store, 0, 0, 0);
            if (StringUtil.isEmpty(entity.getState())) {
                state.setText(getContext().getString(R.string.person_info_is_not_set));
            } else {
                state.setText(entity.getStatusName());
            }
            refundTime.setText(entity.getRefundTime());

            ImageUtils.setSmallImg(img, shopCarGoodEntity.getGoodsPic());
            name.setText(shopCarGoodEntity.getGoodsName());
            String color = null;
            String size = null;
            List<GoodsAttrEntity> attrEntities = shopCarGoodEntity.getAttrList();
            for (GoodsAttrEntity attrEntity : attrEntities) {
                if ("尺码".equals(attrEntity.getAttrName())) {
                    size = attrEntity.getAttrValue();
                } else if ("颜色".equals(attrEntity.getAttrName())) {
                    color = attrEntity.getAttrValue();
                }
            }
            clothColor.setText(context.getString(R.string.shopcar_color, color));
            clothSize.setText(context.getString(R.string.shopcar_size, size));
            price.setText(context.getString(R.string.price, NumberFormatUtil.formatMoney(shopCarGoodEntity.getGoodsPrice())));
            num.setText(context.getString(R.string.num, shopCarGoodEntity.getCount()));

            progressQuery.setTag(entity);
            progressQuery.setOnClickListener(onClickListener);
        }
    }
}
