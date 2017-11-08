package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.person.controller.OrderDetailActivity;
import com.ihujia.hujia.person.controller.OrderListActivity;
import com.ihujia.hujia.person.controller.OrderListFragment;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单列表
 */

public class OrderListAdapter extends BaseAdapterNew<OrderItemEntity> {
	private Context context;
	private View.OnClickListener onClickListener;
	private List<ShopCarGoodEntity> goodEntities;
	private RelativeLayout orderSingle;
	private LinearLayout orderMulti;
	private LinearLayout orderMore;
	private TextView orderMoreText;
	private SimpleDraweeView clothImg;
	private TextView clothName;
	private TextView clothColor;
	private TextView clothSize;
	private TextView clothPrice;
	private TextView clothNum;
	private SimpleDraweeView multiItem1, multiItem2, multiItem3;

	public OrderListAdapter(Context context, List<OrderItemEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.context = context;
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.person_order_list_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		OrderItemEntity entity = getItem(position);
		TextView storeName = ViewHolder.get(convertView, R.id.order_item_store);
		TextView orderStatus = ViewHolder.get(convertView, R.id.order_item_state);
		orderSingle = ViewHolder.get(convertView, R.id.order_item_single);
		orderMulti = ViewHolder.get(convertView, R.id.order_item_multi);
		multiItem1 = ViewHolder.get(convertView, R.id.order_item_multi_item1);
		multiItem2 = ViewHolder.get(convertView, R.id.order_item_multi_item2);
		multiItem3 = ViewHolder.get(convertView, R.id.order_item_multi_item3);
		orderMore = ViewHolder.get(convertView, R.id.order_item_more);
		orderMoreText = ViewHolder.get(convertView, R.id.order_item_more_text);
		clothImg = ViewHolder.get(convertView, R.id.order_item_img);
		clothName = ViewHolder.get(convertView, R.id.order_item_name);
		clothColor = ViewHolder.get(convertView, R.id.order_item_color);
		clothSize = ViewHolder.get(convertView, R.id.order_item_size);
		clothPrice = ViewHolder.get(convertView, R.id.order_item_price);
		clothNum = ViewHolder.get(convertView, R.id.order_item_num);
		TextView totalNum = ViewHolder.get(convertView, R.id.order_item_totalnum);
		TextView totalPrice = ViewHolder.get(convertView, R.id.order_item_totalprice);
		TextView button1 = ViewHolder.get(convertView, R.id.order_item_button1);
		TextView button2 = ViewHolder.get(convertView, R.id.order_item_button2);

		if (entity != null) {
			goodEntities = entity.getGoodsList();
			storeName.setText(context.getString(R.string.person_order_order_number, entity.getOrderStoreId()));
			storeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			setSingOrMulti();

			totalNum.setText(context.getString(R.string.confirm_order_goodsnum, entity.getAllNum()));
			totalPrice.setText(context.getString(R.string.confirm_order_allprice,  NumberFormatUtil.formatMoney(entity.getAllPrice())));

			//订单状态： 1，待支付；2，待发货；3，待收货；4已完成；9取消；10，全部
			String status = entity.getState();
			orderStatus.setText(entity.getStatusName());
			if (String.valueOf(OrderListActivity.ORDERTYPE_PAYING).equals(status)) {//待支付
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.VISIBLE);
				button2.setText(getContext().getString(R.string.order_pay_now));
				button2.setTextColor(ContextCompat.getColor(context, R.color.primary_color_red));
				button2.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_red));
			} else if (String.valueOf(OrderListActivity.ORDERTYPE_DELIVER).equals(status)) {//待发货
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
				button2.setText(getContext().getString(R.string.order_cancel));
				button2.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
				button2.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg));
			} else if (String.valueOf(OrderListActivity.ORDERTYPE_GETING).equals(status)) {//待收货
				button1.setVisibility(View.VISIBLE);
				button2.setVisibility(View.VISIBLE);
				button1.setText(getContext().getString(R.string.order_see_logistics));
				button2.setText(getContext().getString(R.string.order_confirm));
				button2.setTextColor(ContextCompat.getColor(context, R.color.primary_color_red));
				button2.setBackground(ContextCompat.getDrawable(context, R.drawable.person_order_button_bg_red));
			} else if (String.valueOf(OrderListActivity.ORDERTYPE_FINISH).equals(status)) {
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
			} else if (String.valueOf(OrderListActivity.ORDERTYPE_CLOSE).equals(status)) {
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
			} else {
				button1.setVisibility(View.GONE);
				button2.setVisibility(View.GONE);
			}

			orderMulti.setOnClickListener(v -> toOrderDetail(entity));
			orderSingle.setOnClickListener(v -> toOrderDetail(entity));
			button1.setTag(entity);
			button1.setOnClickListener(onClickListener);
			button2.setTag(entity);
			button2.setOnClickListener(onClickListener);
		}
	}

	private void setSingOrMulti() {
		if (goodEntities != null && goodEntities.size() > 0) {
			if (goodEntities.size() == 1) {
				orderMulti.setVisibility(View.GONE);
				orderSingle.setVisibility(View.VISIBLE);

				ShopCarGoodEntity goodEntity = goodEntities.get(0);
				String url = goodEntity.getGoodsPic();
				if (!StringUtil.isEmpty(url)) {
					ImageUtils.setSmallImg(clothImg, url);
				}
				clothName.setText(goodEntity.getGoodsName());
				String color = null;
				String size = null;
				List<GoodsAttrEntity> attrEntities = goodEntity.getAttrList();
				for (GoodsAttrEntity attrEntity : attrEntities) {
					if ("尺码".equals(attrEntity.getAttrName())) {
						size = attrEntity.getAttrValue();
					} else if ("颜色".equals(attrEntity.getAttrName())) {
						color = attrEntity.getAttrValue();
					}
				}
				clothColor.setText(context.getString(R.string.shopcar_color, color));
				clothSize.setText(context.getString(R.string.shopcar_size, size));
				clothPrice.setText(context.getString(R.string.price, goodEntity.getGoodsPrice()));
				clothNum.setText(context.getString(R.string.num, goodEntity.getCount()));
			} else if (goodEntities.size() > 1) {
				orderSingle.setVisibility(View.GONE);
				orderMulti.setVisibility(View.VISIBLE);

				List<String> urls = new ArrayList<>();
				for (ShopCarGoodEntity goodEntity : goodEntities) {
					urls.add(goodEntity.getGoodsPic());
				}

				if (urls != null && urls.size() > 0) {
					if (urls.size() >= 3) {
						notifyDataSetChanged();
						orderMore.setBackgroundResource(R.drawable.person_order_cloth_bg);
						orderMoreText.setText(getContext().getString(R.string.order_more));
						orderMoreText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.order_more_icon);
						ImageUtils.setSmallImg(multiItem1, urls.get(0));
						ImageUtils.setSmallImg(multiItem2, urls.get(1));
						ImageUtils.setSmallImg(multiItem3, urls.get(2));
					} else {
						multiItem3.setVisibility(View.GONE);
						ImageUtils.setSmallImg(multiItem1, urls.get(0));
						ImageUtils.setSmallImg(multiItem2, urls.get(1));
						orderMoreText.setText("");
						orderMoreText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
						orderMore.setBackground(null);
					}
				}
			}
		} else {
			orderMulti.setVisibility(View.GONE);
			orderSingle.setVisibility(View.VISIBLE);
			ImageUtils.setSmallImg(clothImg, "");
			clothName.setText("");
			clothColor.setText(context.getString(R.string.shopcar_color, ""));
			clothSize.setText(context.getString(R.string.shopcar_size, ""));
			clothPrice.setText(context.getString(R.string.price, ""));
			clothNum.setText(context.getString(R.string.num, ""));
		}
	}

	private void toOrderDetail(OrderItemEntity entity) {
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra(OrderListFragment.EXTRA_ORDER_ITEM, entity);
		context.startActivity(intent);
	}
}
