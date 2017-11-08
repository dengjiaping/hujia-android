package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/5.
 * 申请退款或者退款退货的订单中的所有商品
 */

public class ToRefundAdapter extends BaseAdapterNew<ShopCarGoodEntity> {
	private List<ShopCarGoodEntity> mDatas;
	private View.OnClickListener onClickListener;
	private Context context;

	public ToRefundAdapter(Context context, List<ShopCarGoodEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.mDatas = mDatas;
		this.onClickListener = onClickListener;
		this.context = context;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.person_apply_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ShopCarGoodEntity entity = getItem(position);
		View line = ViewHolder.get(convertView, R.id.apply_line);
		SimpleDraweeView clothImg = ViewHolder.get(convertView, R.id.order_item_img);
		TextView clothName = ViewHolder.get(convertView, R.id.order_item_name);
		TextView clothColor = ViewHolder.get(convertView, R.id.order_item_color);
		TextView clothSize = ViewHolder.get(convertView, R.id.order_item_size);
		TextView clothPrice = ViewHolder.get(convertView, R.id.order_item_price);
		TextView clothNum = ViewHolder.get(convertView, R.id.order_item_num);
		TextView refundApply = ViewHolder.get(convertView, R.id.order_item_customer_service);

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
			clothPrice.setText(context.getString(R.string.price,  NumberFormatUtil.formatMoney(entity.getGoodsPrice())));
			clothNum.setText(context.getString(R.string.num, entity.getCount()));

			refundApply.setVisibility(View.VISIBLE);
			switch (entity.getGoodsStatus()) {
				case "20":
					refundApply.setText(context.getString(R.string.person_order_apply_refund));
					refundApply.setTag(entity);
					refundApply.setOnClickListener(onClickListener);
					refundApply.setEnabled(true);
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg));
					break;
				case "21":

				case "22":

				case "23":

				case "24":
					refundApply.setText(context.getString(R.string.person_order_status_refunding));
					refundApply.setTextColor(ContextCompat.getColor(context, R.color.person_check_order_textcolor));
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_gray));
					refundApply.setOnClickListener(null);
					refundApply.setEnabled(false);
					break;
				case "25":

				case "32":
					refundApply.setText(context.getString(R.string.person_order_status_finished));
					refundApply.setTextColor(ContextCompat.getColor(context, R.color.person_check_order_textcolor));
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_gray));
					refundApply.setOnClickListener(null);
					refundApply.setEnabled(false);
					break;
				case "26":

				case "27":

				case "28":

				case "33":

				case "34":
					refundApply.setText(context.getString(R.string.person_order_status_close));
					refundApply.setTextColor(ContextCompat.getColor(context, R.color.person_check_order_textcolor));
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_gray));
					refundApply.setOnClickListener(null);
					refundApply.setEnabled(false);
					break;
				case "30":
					refundApply.setText(context.getString(R.string.person_order_apply_refund_money));
					refundApply.setTag(entity);
					refundApply.setOnClickListener(onClickListener);
					refundApply.setEnabled(true);
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg));
					break;
				case "31":
					refundApply.setText(context.getString(R.string.person_order_status_handling));
					refundApply.setTextColor(ContextCompat.getColor(context, R.color.person_check_order_textcolor));
					refundApply.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_gray));
					refundApply.setOnClickListener(null);
					refundApply.setEnabled(false);
					break;
			}
		}
		if (position == mDatas.size() - 1) {
			line.setVisibility(View.GONE);
		} else {
			line.setVisibility(View.VISIBLE);
		}
	}
}
